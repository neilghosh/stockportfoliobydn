<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="javax.jdo.Query"%>
<%@ page import="ng.StockItem"%>
<%@ page import="ng.PMF"%>

<html>
<body>


<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user != null) {
%>
<p>Hello, <%=user.getNickname()%>! (You can <a
	href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
out</a>.)</p>
<form action="/sign" method="post">
<table>
	<tr>
		<td align="center"><b>Symbol</b></td>
		<td align="center"><b>Qty</b></td>
		<td align="center"><b>Price</b></td>
		<td align="center"><b>Date</b></td>
	</tr>
	<tr>
		<td><input name="symbol" type="text" size="10"></input></td>
		<td><input name="qty" type="text" size="4"></input></td>
		<td><input name="price" type="text" size="10"></input></td>
		<td><input name="date" type="text" size="10"></input></td>
	</tr>
</table>
<br>


<input type="button" value="Search"
	onClick="openModal()">
<input type="submit" value="Post StockItem" /></form>
<%
	} else {
		response.sendRedirect(userService.createLoginURL(request
				.getRequestURI()));

	}

	PersistenceManager pm = PMF.get().getPersistenceManager();
	Query query = pm.newQuery("select from "
			+ StockItem.class.getName() + " where user == '"
			+ user.getNickname() + "'");
	List<StockItem> StockItems = (List<StockItem>) query.execute();
	if (StockItems.isEmpty()) {
%>
<p>You have no stocks to watch.</p>

<%
	} else {
%>
<table border="1"  >
<tr>
		<td align="center"><b>Stock</b></td>
		<td align="center"><b>Live Price</b></td>
		<td align="center"><b>Quantity</b></td>
		<td align="center"><b>Inv. Price</b></td>
		<td align="center"><b>Overall Gain</b></td>
		<td align="center"><b>Overall % Gain</b></td>
	</tr>
	<%
		for (StockItem g : StockItems) {
	%>
	
	<tr>
		<td>
		<blockquote><%=g.getStockCode()%></blockquote>
		</td>

		<td>
		<blockquote><%=g.getStockPrice()%></blockquote>
		</td>

		<td>
		<blockquote><%=g.getQuantity()%></blockquote>
		</td>

		<td>
		<blockquote><%=g.getInvPrice()%></blockquote>
		</td>

		<td>
		<blockquote>
		<%
			DecimalFormat df = new DecimalFormat("0.00");
					String a = df.format((g.getStockPrice() - g.getInvPrice())
							* g.getQuantity());
					double AA = Double.parseDouble(a);
		%><%=AA%></blockquote>
		</td>

		<td>
		<blockquote>
		<%
			df = new DecimalFormat("0.00");
					a = df.format(((g.getStockPrice() - g.getInvPrice()) * g
							.getQuantity())
							* 100 / (g.getQuantity() * g.getInvPrice()));
					AA = Double.parseDouble(a);
		%><%=AA%></blockquote>
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

<!-- Creating Pop up window bellow  -->
<script language="JavaScript" type="text/javascript">
<!--
var retVal=""
var valReturned;
function openModal() 
{
	if (document.forms[0].symbol.value == '')
	{
		alert('Please enter partial value of company t0 search');
	}
	else{	 
	retVal=showModalDialog('searchSymbol.jsp?q='+document.forms[0].symbol.value);
	valReturned=retVal;
	
	document.forms[0].symbol.value = valReturned;
	}
}
//-->
</script>

</body>
</html>