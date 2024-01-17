package mg.docteur.models;

import mg.docteur.connection.Connect;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Patient
{
    int id_patient;
    String nom;
    Date dtn;

//    getters & setters
    public int getId_patient() {
        return id_patient;
    }
    public void setId_patient(int id_patient) {
        this.id_patient = id_patient;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Date getDtn() {
        return dtn;
    }
    public void setDtn(Date dtn) {
        this.dtn = dtn;
    }

//    function
    public static List<Patient> getAllPatient(Connection connection)
    {
        List<Patient> valiny = new ArrayList<>();
        boolean isOuvert = false;
        String query = "select * from patient";
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
                Patient temp = new Patient();
                temp.setId_patient(resultSet.getInt("id_patient"));
                temp.setNom(resultSet.getString("nom"));
                temp.setDtn(resultSet.getDate("dtn"));
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
            System.out.println("Patient getAllPatient issues !");
            e.printStackTrace();
        }
        return valiny;
    }

    public static Patient getPatientById(Connection connection, int id_patient)
    {
        Patient valiny = new Patient();
        boolean isOuvert = false;
        String query = "select * from patient where id_patient = "+id_patient+";";
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
                Patient temp = new Patient();
                temp.setId_patient(resultSet.getInt("id_patient"));
                temp.setNom(resultSet.getString("nom"));
                temp.setDtn(resultSet.getDate("dtn"));
                valiny = temp;
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
            System.out.println("Patient getPatientById issues !");
            e.printStackTrace();
        }
        return valiny;
    }

    public int getAgePatient()
    {
        int valiny = 0;
        LocalDate dateDeNaissance = this.getDtn().toLocalDate();
        LocalDate aujourdhui = LocalDate.now();
        int age = Period.between(dateDeNaissance, aujourdhui).getYears();
        valiny = age;
        return valiny;
    }
}
