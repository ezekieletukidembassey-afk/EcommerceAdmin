<%-- 
    Document   : index
    Created on : Jul 1, 2026, 7:15:05 PM
    Author     : Ezekiel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <jsp:include page="WEB-INF/fragments/head.jsp"/>
  <body>
    <div class="container-scroller">
      <!-- partial:partials/_navbar.html -->
      <jsp:include page="WEB-INF/fragments/navbar.jsp"/>
      <!-- partial -->
      <div class="container-fluid page-body-wrapper">
        <!-- partial:partials/_sidebar.html -->
        <jsp:include page="WEB-INF/fragments/sidebar.jsp"/>
        <!-- partial -->
        <div class="main-panel">
          <div class="content-wrapper">
            <!-- Page Title Header Starts-->
            <jsp:include page="WEB-INF/fragments/titleheader.jsp"/>
            <!-- Page Title Header Ends-->
            <div class="row">
                <jsp:include page="WEB-INF/fragments/grid-margin.jsp"/>
            </div>
                <jsp:include page="WEB-INF/fragments/sales.jsp"/>
                <jsp:include page="WEB-INF/fragments/revenue.jsp"/>
                <jsp:include page="WEB-INF/fragments/events.jsp"/>
          </div>
          <!-- content-wrapper ends -->
          <!-- partial:partials/_footer.html -->
          <jsp:include page="WEB-INF/fragments/footer.jsp"/>
          <!-- partial -->
        </div>
        <!-- main-panel ends -->
      </div>
      <!-- page-body-wrapper ends -->
    </div>
    <!-- container-scroller -->
    <jsp:include page="WEB-INF/fragments/scripts.jsp"/>
  </body>
</html>