package mg.docteur.models;

import mg.docteur.connection.Connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Maladie_patient
{
    String patient_name;
    String maladie_name;

//    getters & setters
    public String getPatient_name() {
        return patient_name;
    }
    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }
    public String getMaladie_name() {
        return maladie_name;
    }
    public void setMaladie_name(String maladie_name) {
        this.maladie_name = maladie_name;
    }

//    function
    public static List<Maladie_patient> getMaladiePatient(Connection connection, int id_patient, int age_patient)
    {
        List<Maladie_patient> valiny = new ArrayList<>();
        boolean isOuvert = false;
        String query = "WITH ParametresMaladie AS (\n" +
                "    SELECT\n" +
                "        pm.id_maladie,\n" +
                "        pp.id_parametre,\n" +
                "        pm.level_min,\n" +
                "        pm.level_max,\n" +
                "        pm.age_min,\n" +
                "        pm.age_max\n" +
                "    FROM\n" +
                "        parametre_maladie pm\n" +
                "            JOIN parametre_patient pp ON pm.id_parametre = pp.id_parametre\n" +
                "    WHERE ("+age_patient+" BETWEEN pm.age_min AND pm.age_max) AND id_patient = "+id_patient+" AND (level BETWEEN  level_min AND level_max)\n" +
                ")\n" +
                "SELECT\n" +
                "    p.id_patient,\n" +
                "    m.id_maladie\n" +
                "FROM\n" +
                "    parametre_patient p\n" +
                "        JOIN ParametresMaladie m ON p.id_parametre = m.id_parametre " +
                "WHERE id_patient = "+id_patient+"\n" +
                "GROUP BY\n" +
                "    p.id_patient, id_maladie\n" +
                "HAVING\n" +
                "        COUNT(m.id_maladie) = (select count(id_maladie) from parametre_maladie where ("+age_patient+" BETWEEN age_min AND age_max) and id_maladie = m.id_maladie);";
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
                Maladie_patient temp = new Maladie_patient();
                temp.setPatient_name(Patient.getPatientById(connection, resultSet.getInt(1)).getNom());
                temp.setMaladie_name(Maladie.getMaladieById(connection, resultSet.getInt(2)).getNom());
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
            System.out.println("Maladie_patient getMaladiePatient issues !");
            e.printStackTrace();
        }
        return valiny;
    }
}
