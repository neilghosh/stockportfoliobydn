<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="javax.xml.parsers.DocumentBuilder"%>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import="org.w3c.dom.Document"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="org.w3c.dom.NodeList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ticker Symbol search</title>
</head>
<body>
<%
	String q = request.getParameter("q").toString();
	String ticker = null;
	String name = null;
	try {

		DocumentBuilderFactory dbf = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db
				.parse("http://testjavaneil.appspot.com/stockname?q="
						+ q);
		document.getDocumentElement().normalize();
		//System.out.println("Root element "+document.getDocumentElement().getNodeName());
		NodeList node = document.getElementsByTagName("company");
		if (node.getLength() == 0)
		{
			out.print("No Match found for <B>" + q+"</B>");
		}
		else
		{
			out.print("<B>" + node.getLength() + "</B> results found <HR>" );
		}
		for (int i = 0; i < node.getLength(); i++) {
			Node firstNode = node.item(i);

			if (firstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element element = (Element) firstNode;
				NodeList firstNameElemntList = element
						.getElementsByTagName("ticker");
				Element firstNameElement = (Element) firstNameElemntList
						.item(0);
				NodeList firstName = firstNameElement.getChildNodes();
				//System.out.println("First Name:"+ ((Node)firstName.item(0)).getNodeValue());
				ticker = ((Node) firstName.item(0)).getNodeValue();
				NodeList lastNameElementList = element
						.getElementsByTagName("name");
				Element lastNameElement = (Element) lastNameElementList
						.item(0);
				NodeList lastName = lastNameElement.getChildNodes();
				name = ((Node) lastName.item(0)).getNodeValue();
				out.println("<a href=\"#\" onclick=\"returnValue='"
						+ ticker + "';window.close()\">" + ticker
						+ "</a>" + " -- " + name);
				out.println("<HR>");

			}
		}

	} catch (Exception e) {
		System.out.print("Ex..");
	}
%>
</body>



</html>