package mg.docteur.models;

import mg.docteur.connection.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Medicament_parametre
{
    int id_medicament;
    double efficacite_medicament;
    double pu_medicament;
    int id_patient;
    int id_parametre;
    double level_parametre;

    public int getId_medicament() {
        return id_medicament;
    }

    public void setId_medicament(int id_medicament) {
        this.id_medicament = id_medicament;
    }

    public double getEfficacite_medicament() {
        return efficacite_medicament;
    }

    public void setEfficacite_medicament(double efficacite_medicament) {
        this.efficacite_medicament = efficacite_medicament;
    }

    public double getPu_medicament() {
        return pu_medicament;
    }

    public void setPu_medicament(double pu_medicament) {
        this.pu_medicament = pu_medicament;
    }

    public int getId_patient() {
        return id_patient;
    }

    public void setId_patient(int id_patient) {
        this.id_patient = id_patient;
    }

    public int getId_parametre() {
        return id_parametre;
    }

    public void setId_parametre(int id_parametre) {
        this.id_parametre = id_parametre;
    }

    public double getLevel_parametre() {
        return level_parametre;
    }

    public void setLevel_parametre(double level_parametre) {
        this.level_parametre = level_parametre;
    }

    public static List<Medicament_parametre> getAllMedicamentFromParametre(Connection connection, int id_patient)
    {
        List<Medicament_parametre> valiny = new ArrayList<>();
        boolean isOuvert = false;
        String query = "with parametre_medicament_patient as (\n" +
                "    select\n" +
                "        *\n" +
                "    from\n" +
                "        parametre_medicament pm\n" +
                "            join parametre_patient pp on pp.id_parametre = pm.id_parametre\n" +
                "    where pp.id_patient = "+id_patient+"\n" +
                ")\n" +
                "select\n" +
                "    *\n" +
                "from\n" +
                "    parametre_medicament_patient pmp\n" +
                "        join medicament m on m.id_medicament = pmp.id_medicament\n" +
                "order by\n" +
                "    m.prix asc;";
        try
        {
            if (connection == null)
            {
                connection = Connect.connectToPostgre();
                isOuvert = true;
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                Medicament_parametre temp = new Medicament_parametre();
                temp.setId_medicament(resultSet.getInt("id_medicament"));
                temp.setEfficacite_medicament(resultSet.getDouble("efficacite"));
                temp.setPu_medicament(resultSet.getDouble("prix"));
                temp.setId_patient(resultSet.getInt("id_patient"));
                temp.setId_parametre(resultSet.getInt("id_parametre"));
                temp.setLevel_parametre(resultSet.getDouble("level"));
                valiny.add(temp);
            }
            resultSet.close();
            statement.close();
            if (isOuvert)
            {
                connection.close();
            }
        }
        catch (Exception e)
        {
            System.out.println("Medicament_parametre getAllMedicamentFromParametre issue !");
            e.printStackTrace();
        }
        return valiny;
    }

    public static List<Medicament_parametre> getMedicamentParametrePatient(List<Medicament_parametre> allMedicamentParametre)
    {
        List<Medicament_parametre> valiny = new ArrayList<>();
        valiny.add(allMedicamentParametre.get(0));
        for (int i = 0; i < allMedicamentParametre.size(); i++)
        {
            for (int j = 0; j < valiny.size(); j++)
            {
                System.out.println(allMedicamentParametre.get(i).getId_parametre() +"!="+ valiny.get(j).getId_parametre());
                if (allMedicamentParametre.get(i).getId_parametre() != valiny.get(j).getId_parametre())
                {
                    System.out.println("if(): "+allMedicamentParametre.get(i).getId_parametre() +"!="+ valiny.get(j).getId_parametre());
                    valiny.add(allMedicamentParametre.get(i));
                }
            }

        }
        return valiny;
    }

    public static List<Medicament_parametre> removeDuplicateMedicamentParametres(List<Medicament_parametre> allMedicamentParametre)
    {
        Set<Integer> idSet = new HashSet<>();
        List<Medicament_parametre> uniqueList = new ArrayList<>();

        for (Medicament_parametre medicamentParametre : allMedicamentParametre)
        {
            if (idSet.add(medicamentParametre.getId_parametre()))
            {
                uniqueList.add(medicamentParametre);
            }
        }

        return uniqueList;
    }

    private static boolean containsParametreId(List<Medicament_parametre> list, int id) {
        for (Medicament_parametre medicamentParametre : list) {
            if (medicamentParametre.getId_parametre() == id) {
                return true;
            }
        }
        return false;
    }

    public static List<Medicament_parametre> getAllMedicamentFromParametreEffetSecondaire(Connection connection, List<Medicament_quantite_prix> listMedoc) {
        List<Medicament_parametre> valiny = new ArrayList<>();
        boolean isOuvert = false;
        String query = "with parametre_medicament_patient as (\n" +
                "    select\n" +
                "        *\n" +
                "    from\n" +
                "        parametre_medicament pm\n" +
                "    where\n" +
                "        pm.id_parametre in (select effetSecondaire_medicament.id_parametre from effetSecondaire_medicament where id_medicament in (" + String.join(",", Collections.nCopies(listMedoc.size(), "?")) + "))\n" +
                ")\n" +
                "select\n" +
                "    *,\n" +
                "    (m.prix * (eSm.efficacite / pmp.efficacite)) as prix_total\n" +
                "from\n" +
                "    parametre_medicament_patient pmp\n" +
                "        join medicament m on m.id_medicament = pmp.id_medicament\n" +
                "        join effetSecondaire_medicament eSm on pmp.id_parametre = eSm.id_parametre\n" +
                "order by\n" +
                "    prix_total asc;";

        try {
            if (connection == null) {
                connection = Connect.connectToPostgre();
                isOuvert = true;
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters for the prepared statement
            for (int i = 0; i < listMedoc.size(); i++) {
                preparedStatement.setInt(i + 1, listMedoc.get(i).getId_medicament());
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set and populate the list
            while (resultSet.next()) {
                Medicament_parametre temp = new Medicament_parametre();
                temp.setId_medicament(resultSet.getInt(1));
                temp.setEfficacite_medicament(resultSet.getDouble(4));
                temp.setPu_medicament(resultSet.getDouble(7));
                temp.setId_parametre(resultSet.getInt(9));
                temp.setLevel_parametre(resultSet.getDouble(11));
                valiny.add(temp);
            }

            resultSet.close();
            preparedStatement.close();

            // Close the connection if it was opened within this method
            if (isOuvert) {
                connection.close();
            }
        } catch (Exception e) {
            System.out.println("Medicament_parametre getAllMedicamentFromParametreEffetSecondaire issue !");
            e.printStackTrace();
        }

        return valiny;
    }
}
