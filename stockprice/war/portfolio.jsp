<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="javax.jdo.Query" %>
<%@ page import="ng.StockItem" %>
<%@ page import="ng.PMF" %>

<html>
  <body>


<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
    	
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
    	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
%>

<%
    }
%>

<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery("select from " + StockItem.class.getName() +" where user == '"+user.getNickname()+"'") ;
    List<StockItem> StockItems = (List<StockItem>) query.execute();
    if (StockItems.isEmpty()) {
%>
<p>The guestbook has no messages.</p>
<%
    } else {
        for (StockItem g : StockItems) {
            if (g.getUser() == null) {
%>
<p>An anonymous person wrote:</p>
<%
            } else {
%>
<p><b><%= g.getUser() %></b> wrote:</p>
<%
            }
%>
<blockquote><%= g.getStockCode() %></blockquote>
<%
        }
    }
    pm.close();
%>

    <form action="/sign" method="post">
      <div><textarea name="content" rows="3" cols="60"></textarea></div>
      <div><input type="submit" value="Post StockItem" /></div>
    </form>

  </body>
</html>