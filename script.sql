create table patient(
    id_patient serial primary key,
    nom varchar,
    dtn date
);
create table maladie(
    id_maladie serial primary key,
    nom varchar
);
create table parametre(
    id_parametre serial primary key,
    nom varchar
);
create table medicament(
    id_medicament serial primary key,
    nom varchar,
    prix double precision
);
create table parametre_medicament(
    id_parametre_medicament serial primary key,
    id_parametre int references parametre(id_parametre),
    id_medicament int references medicament(id_medicament),
    efficacite double precision
);
create table effetSecondaire_medicament(
    id_parametre_medicament serial primary key,
    id_parametre int references parametre(id_parametre),
    id_medicament int references medicament(id_medicament),
    efficacite double precision
);
create table parametre_maladie(
    id_parametre_maladie serial primary key,
    id_maladie int references maladie(id_maladie),
    id_parametre int references parametre(id_parametre),
    level_min double precision,
    level_max double precision,
    age_min int,
    age_max int
);
create table parametre_patient(
    id_parametre_patient serial primary key,
    id_patient int references patient(id_patient),
    id_parametre int references  parametre(id_parametre),
    level double precision
);

INSERT INTO maladie (nom) VALUES
    ('Grippe'),
    ('Indigestion'),
    ('Fatique');

INSERT INTO parametre (nom) VALUES
    ('Loha'),
    ('Tanana'),
    ('Tenda'),
    ('lelo'),
    ('Tongotra'),
    ('Kibo');

INSERT INTO medicament (nom, prix) VALUES
    ('Paracetamol', 200),
    ('Sirop', 23000),
    ('Doliprane', 30000),
    ('MagneB6', 35000);

INSERT INTO parametre_maladie (id_maladie, id_parametre, age_min, age_max, level_min, level_max) VALUES
    (1, 1, 0, 100, 5, 8),
    (1, 3, 0, 100, 4, 7),
    (1, 4, 0, 100, 6, 9),

    (2, 2, 0, 100, 3, 6),
    (2, 2, 0, 100, 2, 7),
    (2, 3, 0, 100, 5, 8),
    (2, 6, 0, 100, 6, 9),

    (3, 1, 0, 100, 3, 6),
    (3, 2, 0, 100, 2, 6),
    (3, 3, 0, 100, 2, 5),
    (3, 5, 0, 100, 4, 7);




INSERT INTO parametre_medicament (id_parametre, id_medicament, efficacite) VALUES
    (1, 1, 2),
    (1, 3, 3),
    (1, 4, 4),

    (2, 4, 3),
    (2, 2, 1),

    (3, 4, 3),
    (3, 2, 3),

    (4, 2, 1),
    (4, 3, 4),

    (5, 2, 2),

    (6, 2, 4);


INSERT INTO effetSecondaire_medicament (id_medicament, id_parametre, efficacite) VALUES
    (2,1,-4);



-- requete
select distinct id_maladie, min(level_max)
    from parametre_maladie
        where id_parametre = 1
        and level_min <= 5
        and level_max >= 5
        and age_min <= 10
        and age_max >= 10
    group by id_maladie;

select count(parametre_maladie.id_maladie)
    from parametre_patient
        join parametre_maladie on parametre_maladie.id_parametre = parametre_patient.id_parametre
    where id_maladie = 2
    and parametre_maladie.age_min <= 10
    and parametre_maladie.age_max >= 10;


WITH ParametresMaladie AS (
    SELECT
        pm.id_maladie,
        pp.id_parametre,
        pm.level_min,
        pm.level_max,
        pm.age_min,
        pm.age_max
    FROM
        parametre_maladie pm
            JOIN parametre_patient pp ON pm.id_parametre = pp.id_parametre
    WHERE (18 BETWEEN pm.age_min AND pm.age_max)
      AND (level BETWEEN  level_min AND level_max)
      AND id_patient = 1
)
SELECT
    p.id_patient,
    m.id_maladie
FROM
    parametre_patient p
        JOIN ParametresMaladie m ON p.id_parametre = m.id_parametre
WHERE id_patient = 1
GROUP BY
    p.id_patient, m.id_maladie
HAVING
        COUNT(m.id_maladie) = (select count(parametre_maladie.id_maladie) from parametre_maladie where (18 BETWEEN age_min AND age_max) and parametre_maladie.id_maladie = m.id_maladie);


with parametre_medicament_patient as (
    select
        pm.id_parametre,
        id_medicament,
        efficacite,
        id_patient,
        level
    from
        parametre_medicament pm
            join parametre_patient pp on pp.id_parametre = pm.id_parametre
    where pp.id_patient = 1
)
select
    id_patient,
    id_parametre,
    m.id_medicament,
    nom,
    level,
    efficacite,
    prix,
    (m.prix * (pmp.level / pmp.efficacite)) as prix_total
from
    parametre_medicament_patient pmp
        join medicament m on m.id_medicament = pmp.id_medicament
order by
    prix_total asc;


with parametre_medicament_patient as (
    select
        *
    from
        parametre_medicament pm
    where
        pm.id_parametre in (select effetSecondaire_medicament.id_parametre from effetSecondaire_medicament where id_medicament in (2, 4))
)
select
    *,
    (m.prix * (eSm.efficacite / pmp.efficacite)) as prix_total
from
    parametre_medicament_patient pmp
        join medicament m on m.id_medicament = pmp.id_medicament
        join effetSecondaire_medicament eSm on pmp.id_parametre = eSm.id_parametre
order by
    prix_total asc;



-- posibilte 1
WITH ParametreMedicamentPatient AS (
    SELECT
        pm.*,
        m.nom,
        m.prix,
        (pp.level / pm.efficacite) as qte_use,
        (m.prix * (pp.level / pm.efficacite)) AS prix_total,
        ROW_NUMBER() OVER (PARTITION BY pm.id_parametre ORDER BY (m.prix * (pp.level / pm.efficacite))) AS row_num
    FROM
        parametre_medicament pm
            JOIN parametre_patient pp ON pm.id_parametre = pp.id_parametre
            JOIN medicament m ON pm.id_medicament = m.id_medicament
    WHERE pp.id_patient = 1
)
SELECT
    id_parametre,
    id_medicament,
    nom,
    prix,
    qte_use,
    prix_total
FROM
    ParametreMedicamentPatient
WHERE
        row_num = 1
ORDER BY
    id_parametre, prix_total;