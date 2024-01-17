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

INSERT INTO parametre
VALUES
    (default, 'Maux de tête'),
    (default, 'Toux'),
    (default, 'Fièvre'),
    (default, 'Essouflement'),
    (default, 'Douleur Musculaire');

INSERT INTO maladie
VALUES
    (default, 'Grippe'),
    (default, 'Rhume'),
    (default, 'Migraine'),
    (default, 'Asthme'),
    (default, 'Hypertension');

INSERT INTO medicament
VALUES
    (DEFAULT, 'Doliprane', 899),
    (DEFAULT, 'Fervex', 1050),
    (DEFAULT, 'Omeprazole', 1420),
    (DEFAULT, 'Amoxicilline', 1000),
    (DEFAULT, 'Aspirine', 880);

INSERT INTO parametre_maladie (id_maladie, id_parametre, age_min, age_max, level_min, level_max)
VALUES
    (1, 1, 10, 25, 5, 9),
    (1, 1, 26, 50, 3, 8),
    (1, 2, 10, 25, 1, 5),
    (1, 2, 26, 50, 2, 7),
    (1, 3, 18, 25, 1, 7),
    (1, 3, 26, 50, 2, 5),
    (1, 5, 18, 25, 1, 6),
    (1, 5, 26, 50, 3, 8),
    (2, 2, 10, 25, 1, 5),
    (2, 2, 26, 50, 1, 3),
    (2, 3, 10, 25, 5, 10),
    (2, 3, 26, 50, 2, 7),
    (3, 1, 10, 25, 5, 10),
    (3, 1, 26, 50, 4, 8),
    (4, 4, 10, 25, 5, 9),
    (4, 4, 26, 50, 3, 8),
    (4, 5, 10, 25, 2, 5),
    (4, 5, 26, 50, 2, 7),
    (5, 1, 10, 25, 2, 7),
    (5, 1, 26, 50, 4, 7),
    (5, 3, 10, 25, 3, 7),
    (5, 3, 26, 50, 5, 8),
    (5, 4, 10, 25, 1, 4),
    (5, 4, 26, 50, 2, 5);

INSERT INTO parametre_medicament (id_parametre, id_medicament, efficacite)
VALUES
    (1, 1, 4),
    (3, 1, 3),
    (5, 1, 2),
    (1, 2, 3),
    (2, 2, 4),
    (5, 2, 3),
    (1, 3, 4),
    (4, 3, 4),
    (3, 4, 2),
    (2, 4, 3),
    (1, 5, 3),
    (5, 5, 3),
    (3, 5, 3);



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
    WHERE (10 BETWEEN pm.age_min AND pm.age_max)
      AND (level BETWEEN  level_min AND level_max)
      AND id_patient = 1
)
SELECT
    p.id_patient,
    m.id_maladie
FROM
    parametre_patient p
        JOIN ParametresMaladie m ON p.id_parametre = m.id_parametre
GROUP BY
    p.id_patient, m.id_maladie
HAVING
        COUNT(m.id_maladie) = (select count(parametre_maladie.id_maladie) from parametre_maladie where (10 BETWEEN age_min AND age_max) and parametre_maladie.id_maladie = m.id_maladie);


with parametre_medicament_patient as (
    select
        *
    from
        parametre_medicament pm
            join parametre_patient pp on pp.id_parametre = pm.id_parametre
    where pp.id_patient = 1
)
select
    *,
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
            join parametre_patient pp on pp.id_parametre = pm.id_parametre
    where
        pp.id_parametre in (select effetSecondaire_medicament.id_parametre from effetSecondaire_medicament where id_medicament in (1, 2))
)
select
    *,
    (m.prix * (pmp.level / pmp.efficacite)) as prix_total
from
    parametre_medicament_patient pmp
        join medicament m on m.id_medicament = pmp.id_medicament
order by
    prix_total asc;
