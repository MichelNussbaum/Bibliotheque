<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Enumeration"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Process3</title>
</head>
<body>
	<form action='Logout3.jsp' method='POST' >
		<h1> LoggedIn: <%= session.getAttribute("login")%> </h1>
		<input type='submit' value='Logout' />
	</form>
	<form action='Controller' method='POST' >
		Cle : <input type='text' name='cle' />
		Val : <input type='text' name='val' />
		<input type='submit' value='Add' />
	</form>
		<%Enumeration<String> attributs = session.getAttributeNames();
		String attributName = "";
		String attributValue = "";

		while(attributs.hasMoreElements()) {
			attributName = attributs.nextElement().toString();
			attributValue = session.getAttribute(attributName).toString(); %>
			<p> <%= attributName%> : <%= attributValue %></p>
		<% }%>
</body>
</html>