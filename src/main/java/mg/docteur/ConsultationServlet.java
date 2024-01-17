package mg.docteur;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.docteur.connection.Connect;
import mg.docteur.models.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "consultationServlet", value = "/consultation-servlet")
public class ConsultationServlet extends HttpServlet
{
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
//            connection
            Connection connection = Connect.connectToPostgre();

//            parametre
            int id_patient = Integer.parseInt(request.getParameter("patient"));
            Patient patient = Patient.getPatientById(connection, id_patient);
            int age = patient.getAgePatient();
            List<Maladie_patient> allMaladiePatient = Maladie_patient.getMaladiePatient(connection, id_patient, age);
            List<Medicament_parametre> allMedicamentParametre = Medicament_parametre.getAllMedicamentFromParametre(connection, id_patient);
            List<Medicament_quantite_prix> allMedicamentParametreAvecPrix = Medicament_quantite_prix.getMedicamentTotalUse(connection, allMedicamentParametre);
            List<Medicament_quantite_prix> medicamentParametrePatient = Medicament_quantite_prix.removeDuplicateMedicamentParametres2(allMedicamentParametreAvecPrix);
            List<Medicament_quantite_prix> valinyList1 = Medicament_quantite_prix.removeDuplicateMedicament(medicamentParametrePatient);

            List<Medicament_parametre> listMedicament_medicamentParametrePatient = Medicament_parametre.getAllMedicamentFromParametreEffetSecondaire(connection, medicamentParametrePatient);
            List<Medicament_quantite_prix> listMedicament_medicamentParametrePatientAvecPrix = Medicament_quantite_prix.getMedicamentTotalUse(connection, listMedicament_medicamentParametrePatient);
            List<Medicament_quantite_prix> medicamentParametrePatientEffetSecondaire = Medicament_quantite_prix.removeDuplicateMedicamentParametres2(listMedicament_medicamentParametrePatientAvecPrix);
            List<Medicament_quantite_prix> valinyList2 = Medicament_quantite_prix.removeDuplicateMedicament(medicamentParametrePatientEffetSecondaire);

            medicamentParametrePatient.addAll(medicamentParametrePatientEffetSecondaire);
            valinyList1.addAll(valinyList2);
//            for (int i = 0; i < allMedicamentParametreAvecPrix.size(); i++)
//            {
//                System.out.println("parametre : "+allMedicamentParametreAvecPrix.get(i).getId_parametre()+" | medicament : "+allMedicamentParametreAvecPrix.get(i).getId_medicament()+" | pu : "+allMedicamentParametreAvecPrix.get(i).getPu());
//            }
            System.out.println("Maladie du patient : "+allMaladiePatient.get(0).getPatient_name());
            for (int i = 0; i < allMaladiePatient.size(); i++)
            {
                System.out.println("    Maladie : "+allMaladiePatient.get(i).getMaladie_name());
            }
            System.out.println("---------------------------------------------------------------------");
            for (int i = 0; i < valinyList1.size(); i++)
            {
                String paramatre = Parametre.getParametreById(connection, valinyList1.get(i).getId_parametre()).getNom();
                String maladie = Medicament.getMedicamentById(connection, valinyList1.get(i).getId_medicament()).getNom();
                System.out.println("Parametre : "+paramatre+" | Medicament : "+maladie+" | pu : "+valinyList1.get(i).getPu()+" | qte necessaire : "+valinyList1.get(i).getQte_medicament()+" | prix total : "+valinyList1.get(i).getPrix_total());
            }

//            setAttribute
            request.setAttribute("age", age);
            request.setAttribute("allMaladiePatient", allMaladiePatient);
            request.setAttribute("medicamentParametrePatient", medicamentParametrePatient);
            request.setAttribute("valiny", valinyList1);

            RequestDispatcher dispatcher = request.getRequestDispatcher("consultation.jsp");
            dispatcher.forward(request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
