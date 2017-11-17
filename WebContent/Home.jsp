<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>
</head>
<body>
<h1>Bienvenue à la bibliothèque</h1>
<div style="display:inline-block">
	<%@ include file="Search.html" %>
</div>
<div style="display:inline-block; float:right; clear:left" >
	<h3>Connexion</h3>
	<form action='Controller' method='POST' >
		Identifiant: <input type='text' name='login' /><br>
		Mot de passe: <input type='password' name='password' /><br>
		<input name="form" value="connection" type="hidden">
		<input type='submit' value='Se Connecter' />
	</form>
</div>

<% if(request.getParameter("search") != null && request.getParameter("search").equals("success")){ %>
<jsp:include page="SearchResult.jsp">
	<jsp:param value="<%= request.getParameter("title") %>" name="title"/>
	<jsp:param value="<%= request.getParameter("author") %>" name="author"/>
	<jsp:param value="<%= request.getParameter("nbOfCopy") %>" name="nbOfCopy"/>
	<jsp:param value="<%= request.getParameter("remaining") %>" name="remaining"/>
</jsp:include>
<% }else{
	if(request.getParameter("search") != null && request.getParameter("search").equals("failed")){ %>
	<h3>Aucun livre ne correspond à votre recherhce</h3>	
<% }
} %>

</body>
</html>