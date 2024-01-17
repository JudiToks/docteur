package mg.docteur.models;

import mg.docteur.connection.Connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Parametre
{
    int id_parametre;
    String nom;

    public int getId_parametre() {
        return id_parametre;
    }

    public void setId_parametre(int id_parametre) {
        this.id_parametre = id_parametre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public static Parametre getParametreById(Connection connection, int id_parametre)
    {
        Parametre valiny = new Parametre();
        boolean isOuvert = false;
        String query = "select * from parametre where id_parametre = "+id_parametre+";";
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
                Parametre temp = new Parametre();
                temp.setId_parametre(resultSet.getInt("id_parametre"));
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
            System.out.println("Parametre getParametreById issues !");
            e.printStackTrace();
        }
        return valiny;
    }
}
