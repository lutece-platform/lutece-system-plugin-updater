<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="updater" scope="session" class="fr.paris.lutece.plugins.updater.web.UpdaterJspBean" />

<% updater.init( request , updater.RIGHT_UPDATES_MANAGEMENT ); %>
<%= updater.getManageInstalled( request )%>

<%@include file="../../AdminFooter.jsp" %>