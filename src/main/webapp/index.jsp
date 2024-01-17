<%@ page import="java.util.List" %>
<%@ page import="mg.docteur.models.Patient" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Patient> listPatient = (List<Patient>) request.getAttribute("listPatient");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <title>Docteur</title>
</head>
<body>

    <form method="get" action="consultation-servlet">
        <div class="row">
            <div class="col-9">
                <select class="form-select" aria-label="Default select example" name="patient">
                    <option selected>Choose patient</option>
                    <% if (listPatient != null) { %>
                        <% for (int i = 0; i < listPatient.size(); i++) { %>
                            <option value="<%=listPatient.get(i).getId_patient()%>"><%=listPatient.get(i).getNom()%></option>
                        <% } %>
                    <% } %>
                </select>
            </div>
            <div class="col-3">
                <button class="btn btn-primary">Valider</button>
            </div>
        </div>
    </form>
            
</body>
</html>