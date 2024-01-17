package mg.docteur.models;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Medicament_quantite_prix
{
    int id_patient;
    int id_parametre;
    String nom_parametre;
    int id_medicament;
    String nom_medicament;
    int qte_medicament;
    double pu;
    double prix_total;

    public String getNom_parametre() {
        return nom_parametre;
    }

    public void setNom_parametre(String nom_parametre) {
        this.nom_parametre = nom_parametre;
    }

    public String getNom_medicament() {
        return nom_medicament;
    }

    public void setNom_medicament(String nom_medicament) {
        this.nom_medicament = nom_medicament;
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

    public int getId_medicament() {
        return id_medicament;
    }

    public void setId_medicament(int id_medicament) {
        this.id_medicament = id_medicament;
    }

    public int getQte_medicament() {
        return qte_medicament;
    }

    public void setQte_medicament(int qte_medicament) {
        this.qte_medicament = qte_medicament;
    }

    public double getPu() {
        return pu;
    }

    public void setPu(double pu) {
        this.pu = pu;
    }

    public double getPrix_total() {
        return prix_total;
    }

    public void setPrix_total(double prix_total) {
        this.prix_total = prix_total;
    }

    public static List<Medicament_quantite_prix> getMedicamentTotalUse(Connection connection, List<Medicament_parametre> allMedicamentParametre)
    {
        List<Medicament_quantite_prix> valiny = new ArrayList<>();
        try
        {
            for (int i = 0; i < allMedicamentParametre.size(); i++)
            {
                double nbr_traitement = Math.abs(allMedicamentParametre.get(i).getLevel_parametre()) / allMedicamentParametre.get(i).getEfficacite_medicament();
                Medicament_quantite_prix temp = new Medicament_quantite_prix();
                temp.setId_patient(allMedicamentParametre.get(i).getId_patient());
                temp.setId_medicament(allMedicamentParametre.get(i).getId_medicament());
                temp.setNom_medicament(Medicament.getMedicamentById(connection, allMedicamentParametre.get(i).getId_medicament()).getNom());
                temp.setId_parametre(allMedicamentParametre.get(i).getId_parametre());
                temp.setNom_parametre(Parametre.getParametreById(connection, allMedicamentParametre.get(i).getId_parametre()).getNom());
                temp.setQte_medicament((int) Math.ceil(nbr_traitement));
                temp.setPu(allMedicamentParametre.get(i).getPu_medicament());
                temp.setPrix_total(temp.getQte_medicament() * temp.getPu());
                valiny.add(temp);
            }
            Collections.sort(valiny, Comparator.comparingDouble(medicament -> medicament.prix_total));
        }
        catch (Exception e)
        {
            System.out.println("Medicament_quantite_prix getMedicamentTotalUse issues !");
            e.printStackTrace();
        }
        return valiny;
    }

    public static List<Medicament_quantite_prix> removeDuplicateMedicamentParametres2(List<Medicament_quantite_prix> allMedicamentParametre) {
        List<Medicament_quantite_prix> uniqueList = new ArrayList<>();

        for (Medicament_quantite_prix medicamentParametre : allMedicamentParametre) {
            if (!containsParametreId(uniqueList, medicamentParametre.getId_parametre())) {
                uniqueList.add(medicamentParametre);
            }
        }

        return uniqueList;
    }

    private static boolean containsParametreId(List<Medicament_quantite_prix> list, int id) {
        for (Medicament_quantite_prix medicamentParametre : list) {
            if (medicamentParametre.getId_parametre() == id) {
                return true;
            }
        }
        return false;
    }
}
