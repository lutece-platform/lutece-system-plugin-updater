<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="updater" scope="session" class="fr.paris.lutece.plugins.updater.web.UpdaterJspBean" />

<% 
    updater.init( request, updater.RIGHT_UPDATES_MANAGEMENT ); 
    response.sendRedirect( updater.restorePlugin( request ) );
%>
