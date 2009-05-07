<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.List"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="javax.jdo.Query"%>
<%@ page import="ng.Transaction"%>
<%@ page import="ng.util"%>
<%@ page import="ng.PMF"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transaction History</title>
</head>
<body>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	PersistenceManager pm = PMF.get().getPersistenceManager();
	Query query = pm.newQuery("select from "
			+ Transaction.class.getName() + " where user == '"
			+ user.getNickname() + "'");
	List<Transaction> Transactions = (List<Transaction>) query.execute();
	if (Transactions.isEmpty()) {
%>
<p>You have no stocks to watch.</p>

<%
	} else {
		
%>
<table border="1">
	<tr>
		<td align="center"><b>Stock</b></td>
		<td align="center"><b>Type</b></td>
		<td align="center"><b>Quantity</b></td>
		<td align="center"><b>Inv. Price</b></td>
		<td align="center"><b>Date</b></td>
		
	</tr>
	<%
		for (Transaction g : Transactions) {
	%>

	<tr>
		<td>
		<blockquote><%=g.getStockCode()%></blockquote>
		</td>

		<td>
		<blockquote><%=g.getTransactionType()%></blockquote>
		</td>

		<td>
		<blockquote><%=g.getQuantity() %></blockquote>
		</td>

		<td>
		<blockquote><%=g.getInvPrice() %></blockquote>
		</td>
		
		<td>
		<blockquote><%
		
		DateFormat df =DateFormat.getDateInstance(DateFormat.DEFAULT);
		
		String dateString = df.format(g.getDate());
		out.println(dateString);
		


		 %></blockquote>
		</td>
		<td>
			<form action="/sign" method="post">
			<div><input name="symbol" type="hidden"
				value="<%=g.getStockCode()%>"></input> <input name="action"
				type="hidden" value="delete"></input> <input name="id" type="hidden"
				value="<%=g.getId()%>"></input> <input type="submit" value="Delete" /></div>
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