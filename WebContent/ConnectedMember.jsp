<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Profil adhérent</title>
</head>
<body>
<h2>Vous êtes connecté en tant qu'adhérent</h2>
<div style="display:inline-block">
	<%@ include file="Search.html" %>
</div>
<div style="display:inline-block; float:right; clear:left" >
	<%@ include file="Logout.html" %>
</div>
<div>
	<h2>Action : </h2>
	<form action="Controller" method="POST">
		Titre : <input name="title" type="text"><br>
		Auteur : <input name="author" type="text"><br>
		<input type="submit" value="Reserver">
		<input type="submit" value="Annuler reservation">
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