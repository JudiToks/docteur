package mg.docteur.models;

import mg.docteur.connection.Connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Parametre_patient
{
    int id_parametre_patient;
    int id_patient;
    int id_parametre;
    double level;

//    getters & setters
    public int getId_parametre_patient() {
        return id_parametre_patient;
    }
    public void setId_parametre_patient(int id_parametre_patient) {
        this.id_parametre_patient = id_parametre_patient;
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
    public double getLevel() {
        return level;
    }
    public void setLevel(double level) {
        this.level = level;
    }

//    function
    public static List<Parametre_patient> getParametrePatientByIdPatient(Connection connection, int id_patient)
    {
        List<Parametre_patient> valiny = new ArrayList<>();
        boolean isOuvert = false;
        String query = "select * from parametre_patient where id_patient = "+id_patient+";";
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
                Parametre_patient temp = new Parametre_patient();
                temp.setId_parametre_patient(resultSet.getInt(1));
                temp.setId_patient(resultSet.getInt(2));
                temp.setId_parametre(resultSet.getInt(3));
                temp.setLevel(resultSet.getDouble(4));
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
            System.out.println("Parametre_patient getParametrePatientByIdPatient issues !");
            e.printStackTrace();
        }
        return valiny;
    }
}
