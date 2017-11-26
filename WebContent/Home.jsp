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

<% if(request.getAttribute("search") != null && request.getAttribute("search").equals("success")){ %>
 <%@page import="java.util.ArrayList"%> 
 <%@page import="model.Book"%>
 <%@page import="model.Library"%> 
 	<h1>Voici le résultat de votre recherche : </h1>
 	<% ArrayList<Book> listOfBooks = new ArrayList<Book>(); 
 	Library library = (Library) request.getAttribute("library"); 
	listOfBooks = (ArrayList<Book>) request.getAttribute("listOfBooks");
 	for (Book book: listOfBooks) {%> 
 <jsp:include page="SearchResult.jsp">
 	<jsp:param value="<%= book.getTitle()  %>" name="title" /> 
 	<jsp:param value="<%= book.getAuthor() %>" name="author" /> 
 	<jsp:param value="<%= book.getNbCopy() %>" name="nbOfCopy" /> 
 	<jsp:param value="<%= library.nbCopyRemaining(book) %>" name="remaining"/> 
 </jsp:include> 
 		<%} %> 
<% }else{
	if(request.getAttribute("search") != null && request.getAttribute("search").equals("failed")){ %>
	<h3>Aucun livre ne correspond à votre recherche</h3>	
<% }
} %>

</body>
</html>