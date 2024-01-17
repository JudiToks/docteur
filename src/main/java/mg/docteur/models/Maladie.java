package mg.docteur.models;

import mg.docteur.connection.Connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Maladie
{
    int id_maladie;
    String nom;

//    getters & setters
    public int getId_maladie() {
        return id_maladie;
    }

    public void setId_maladie(int id_maladie) {
        this.id_maladie = id_maladie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

//    function
    public static Maladie getMaladieById(Connection connection, int id_maladie)
    {
        Maladie valiny = new Maladie();
        boolean isOuvert = false;
        String query = "select * from maladie where id_maladie = "+id_maladie+";";
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
                Maladie temp = new Maladie();
                temp.setId_maladie(resultSet.getInt("id_maladie"));
                temp.setNom(resultSet.getString("nom"));
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
            System.out.println("Maladie getMaladieById issues !");
            e.printStackTrace();
        }
        return valiny;
    }
}
