<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="javax.jdo.PersistenceManager"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="javax.jdo.Query"%>
<%@ page import="ng.Transaction"%>
<%@ page import="ng.PMF"%>

<html>
<head>
<!-- UTF-8 is the recommended encoding for your pages -->
<meta http-equiv="content-type" content="text/xml; charset=utf-8" />
<title>Portfolio</title>

<!-- Loading Theme file(s) -->
<link rel="stylesheet" href="css/bluexp.css" />

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript" src="js/zapatec.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<!-- Loading language definition file -->
<script type="text/javascript" src="js/calendar-en.js"></script>

</head>
<body>


<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user != null) {
%>
<p>Hello, <%=user.getNickname()%>! (You can <a
	href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
out</a>.)</p>
<form action="/sign" method="post" onsubmit="return validate()">
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
		<td><input name="date" type="text" id="date" size="10"></input>
		<button id="trigger">...</button>
		</td>
	</tr>
</table>
<br>


<input type="button" value="Company Search" onClick="openModal()"> <input
	type="submit" value="Save" /></form>
<%
	} else {
		response.sendRedirect(userService.createLoginURL(request
				.getRequestURI()));

	}

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
		<td align="center"><b>Live Price</b></td>
		<td align="center"><b>Quantity</b></td>
		<td align="center"><b>Inv. Price</b></td>
		<td align="center"><b>Overall Gain</b></td>
		<td align="center"><b>Overall % Gain</b></td>
	</tr>
	<%
		for (Transaction g : Transactions) {
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

		<%
			double brokerage = (g.getInvPrice()*g.getQuantity()*0.001385)+27.58;
		    double effInvPrice = g.getInvPrice()+ brokerage/g.getQuantity();
		    double effStockPrice = g.getStockPrice()- brokerage/g.getQuantity();
		%>
		</td>

		<td>
		<blockquote>
		<%
			DecimalFormat df = new DecimalFormat("0.00");
					String a = df.format((g.getStockPrice() - g.getInvPrice())
							* g.getQuantity());
					double AA = Double.parseDouble(a);
		%><%=AA%> &nbsp;[<%
			df = new DecimalFormat("0.00");
					a = df.format((effStockPrice - effInvPrice) * g
							.getQuantity());
					AA = Double.parseDouble(a);
		%><%=AA%>]</blockquote>

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
		alert('Please enter partial value of company to search');
	}
	else{	 
	retVal=showModalDialog('searchSymbol.jsp?q='+document.forms[0].symbol.value);
	valReturned=retVal;
	
	document.forms[0].symbol.value = valReturned;
	}
}
function validate()
{
	if (document.forms[0].symbol.value == '')
	{
		alert('Please enter Symbol');
		return false;
	}
	if (document.forms[0].qty.value == '')
	{
		alert('Please enter Quantity');
		return false;
	}
	else if (document.forms[0].price.value == '')
	{
		alert('Please enter price');
		return false;
	}
	else if (document.forms[0].date.value == '')
	{
		alert('Please enter Date');
		return false;
	}
}
//-->
</script>
<script type="text/javascript">//<![CDATA[
      Zapatec.Calendar.setup({
        firstDay          : 1,
        weekNumbers       : false,
        showOthers        : true,
        electric          : false,
        inputField        : "date",
        button            : "trigger",
        ifFormat          : "%d-%b-%Y",
        daFormat          : "%Y/%b/%d"
      });
    //]]></script>
</body>
</html>