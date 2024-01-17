<%@ page import="java.util.List" %>
<%@ page import="mg.docteur.models.Maladie_patient" %>
<%@ page import="mg.docteur.models.Medicament_quantite_prix" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int age = (int) request.getAttribute("age");
    List<Maladie_patient> listMaladiePatient = (List<Maladie_patient>) request.getAttribute("allMaladiePatient");
    List<Medicament_quantite_prix> listMedicamentParametre = (List<Medicament_quantite_prix>) request.getAttribute("medicamentParametrePatient");
    List<Medicament_quantite_prix> medicamentUse = (List<Medicament_quantite_prix>) request.getAttribute("valiny");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <title>Docteur</title>
    <link href="./assets/css/styles.css" rel="stylesheet" />
    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
<%--    <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />--%>
</head>
<body>
<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
    <!-- Navbar Brand-->
    <a class="navbar-brand ps-3" href="index.html">Docteur</a>
    <!-- Sidebar Toggle-->
    <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>
    <!-- Navbar Search-->
    <form class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
        <div class="input-group">
            <input class="form-control" type="text" placeholder="Search for..." aria-label="Search for..." aria-describedby="btnNavbarSearch" />
            <button class="btn btn-primary" id="btnNavbarSearch" type="button"><i class="fas fa-search"></i></button>
        </div>
    </form>
    <!-- Navbar-->
    <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"><i class="fas fa-user fa-fw"></i></a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                <li><a class="dropdown-item" href="#!">Settings</a></li>
                <li><a class="dropdown-item" href="#!">Activity Log</a></li>
                <li><hr class="dropdown-divider" /></li>
                <li><a class="dropdown-item" href="#!">Logout</a></li>
            </ul>
        </li>
    </ul>
</nav>
<div id="layoutSidenav">
    <div id="layoutSidenav_nav">
        <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
            <div class="sb-sidenav-menu">
                <div class="nav">
                    <div class="sb-sidenav-menu-heading">Core</div>
                    <a class="nav-link" href="index.html">
                        <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                        Home
                    </a>
                </div>
            </div>
            <div class="sb-sidenav-footer">
                <div class="small">Logged in as:</div>
                Judicael
            </div>
        </nav>
    </div>
    <div id="layoutSidenav_content">
        <main>
            <%--      container     --%>
            <div class="container-fluid px-4">
                <br>
                <h3>Patient : <%=listMaladiePatient.get(0).getPatient_name()%> | Age : <%=age%></h3><hr>

                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-table me-1"></i>
                                Liste des maladies du patient
                            </div>
                            <div class="card-body">
                                <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                                    <div class="datatable-top">
                                        <div class="datatable-dropdown">
                                            <label>
                                                <select class="datatable-selector"><option value="5">5</option><option value="10" selected="">10</option><option value="15">15</option><option value="20">20</option><option value="25">25</option></select> entries per page
                                            </label>
                                        </div>
                                        <div class="datatable-search">
                                            <input class="datatable-input" placeholder="Search..." type="search" title="Search within table" aria-controls="datatablesSimple">
                                        </div>
                                    </div>
                                    <div class="datatable-container">
                                        <table id="datatablesSimple" class="datatable-table">
                                            <thead>
                                            <tr>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Numero</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Nom</a></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                                <% for (int i = 0; i < listMaladiePatient.size(); i++) { %>
                                                    <tr data-index="<%=i%>">
                                                        <td><%=i+1%></td>
                                                        <td><%=listMaladiePatient.get(i).getMaladie_name()%></td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-table me-1"></i>
                                Liste de medicament pour chaque parametre
                            </div>
                            <div class="card-body">
                                <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                                    <div class="datatable-top">
                                        <div class="datatable-dropdown">
                                            <label>
                                                <select class="datatable-selector"><option value="5">5</option><option value="10" selected="">10</option><option value="15">15</option><option value="20">20</option><option value="25">25</option></select> entries per page
                                            </label>
                                        </div>
                                        <div class="datatable-search">
                                            <input class="datatable-input" placeholder="Search..." type="search" title="Search within table" aria-controls="datatablesSimple">
                                        </div>
                                    </div>
                                    <div class="datatable-container">
                                        <table id="datatablesSimple1" class="datatable-table">
                                            <thead>
                                            <tr>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Parametre</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Medicament</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">PU</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Qte necessaire</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Prix total</a></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                                <% for (int i = 0; i < listMedicamentParametre.size(); i++) { %>
                                                    <tr data-index="<%=i%>">
                                                        <td><%=listMedicamentParametre.get(i).getNom_parametre()%></td>
                                                        <td><%=listMedicamentParametre.get(i).getNom_medicament()%></td>
                                                        <td><%=listMedicamentParametre.get(i).getPu()%></td>
                                                        <td><%=listMedicamentParametre.get(i).getQte_medicament()%></td>
                                                        <td><%=listMedicamentParametre.get(i).getPrix_total()%></td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card mb-4">
                            <div class="card-header">
                                <i class="fas fa-table me-1"></i>
                                Liste de medicament a acheter
                            </div>
                            <div class="card-body">
                                <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                                    <div class="datatable-top">
                                        <div class="datatable-dropdown">
                                            <label>
                                                <select class="datatable-selector"><option value="5">5</option><option value="10" selected="">10</option><option value="15">15</option><option value="20">20</option><option value="25">25</option></select> entries per page
                                            </label>
                                        </div>
                                        <div class="datatable-search">
                                            <input class="datatable-input" placeholder="Search..." type="search" title="Search within table" aria-controls="datatablesSimple">
                                        </div>
                                    </div>
                                    <div class="datatable-container">
                                        <table id="datatablesSimple2" class="datatable-table">
                                            <thead>
                                            <tr>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Medicament</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">PU</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Qte necessaire</a></th>
                                                <th data-sortable="true"><a href="#" class="datatable-sorter">Prix total</a></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <% for (int i = 0; i < medicamentUse.size(); i++) { %>
                                            <tr data-index="<%=i%>">
                                                <td><%=medicamentUse.get(i).getNom_medicament()%></td>
                                                <td><%=medicamentUse.get(i).getPu()%></td>
                                                <td><%=medicamentUse.get(i).getQte_medicament()%></td>
                                                <td><%=medicamentUse.get(i).getPrix_total()%></td>
                                            </tr>
                                            <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

            </div>
            <%--      container     --%>
        </main>
        <footer class="py-4 bg-light mt-auto">
            <div class="container-fluid px-4">
                <div class="d-flex align-items-center justify-content-between small">
                    <div class="text-muted">Copyright &copy; Website 2024</div>
                    <div>
                        <a href="#">Privacy Policy</a>
                        &middot;
                        <a href="#">Terms &amp; Conditions</a>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="./assets/js/scripts.js"></script>
<%--<script src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js" crossorigin="anonymous"></script>--%>
<%--<script src="./assets/js/datatables-simple-demo.js"></script>--%>
</body>
</html>
