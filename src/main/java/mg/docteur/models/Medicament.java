package mg.docteur.models;

import mg.docteur.connection.Connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Medicament
{
    int id_medicament;
    String nom;

    public int getId_medicament() {
        return id_medicament;
    }

    public void setId_medicament(int id_medicament) {
        this.id_medicament = id_medicament;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public static Medicament getMedicamentById(Connection connection, int id_medicament)
    {
        Medicament valiny = new Medicament();
        boolean isOuvert = false;
        String query = "select * from medicament where id_medicament = "+id_medicament+";";
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
                Medicament temp = new Medicament();
                temp.setId_medicament(resultSet.getInt("id_medicament"));
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
            System.out.println("Medicament getMedicamentById issues !");
            e.printStackTrace();
        }
        return valiny;
    }
}
