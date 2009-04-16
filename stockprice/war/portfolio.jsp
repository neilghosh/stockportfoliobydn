<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
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
 <form action="/sign" method="post">
      <div><input name="symbol" type="text"></input>
      <input type="submit" value="Post StockItem" /></div>
    </form>
<%
    } else {
    	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));

    }

    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery("select from " + StockItem.class.getName() +" where user == '"+user.getNickname()+"'") ;
    List<StockItem> StockItems = (List<StockItem>) query.execute();
    if (StockItems.isEmpty()) {
%>
<p>You have no stocks to watch.</p>

<%
    } else {
   %>
   <table>
   <% 
        for (StockItem g : StockItems) {
    
%>
<tr>
<td>
<blockquote><%= g.getStockCode() %></blockquote>
</td>
<td>
<%
//Key k = KeyFactory.createKey(StockItem.class.getSimpleName(),g.getStockCode());
//StockItem stock = pm.getObjectById(StockItem.class,g.getStockCode() );
String code = g.getStockCode();
//query = pm.newQuery("delete from " + StockItem.class.getName() +" where stockCode == '"+g.getStockCode()+"'") ; %>
<form action="/sign" method="post">
      <div><input name="symbol" type="text" value="<%= g.getStockCode() %>"></input>
      <input name="action" type="text" value="delete"></input>
      <input type="submit" value="Delete" /></div>
    </form>
</td>
</tr>
<%
        }
   %>
   </table>
   <%
   
    }
    pm.close();
%>
   

  </body>
</html>