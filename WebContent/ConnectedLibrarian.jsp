<% if ( session.getAttribute("userType").equals("ConnectedLibrarian")){
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Profil Bibliothécaire</title>
</head>
<body>
<h2>Vous êtes connecté en tant que bibliothécaire</h2>
<div style="display:inline-block">
	<%@ include file="Search.html" %>
</div>
<div style="display:inline-block; float:right; clear:left" >
	<%@ include file="Logout.html" %>
</div>
<div>
	<h2>Action : </h2>
	
	<form action="Controller" method="POST">
		<input name="form" value="actionLibrarian" type="hidden">
		<input type="submit" value="Ajouter/Supprimer livre/exemplaire" name="submit">
		<input type="submit" value="Emprunter/Restituer un livre" name="submit">
	</form>
<!-- 	<form action="Controller" method="POST"> -->
<!-- 		<input name="form" value="actionLibrarian" type="hidden"> -->
<!-- 		Titre : <input name="title" type="text"><br> -->
<!-- 		Auteur : <input name="author" type="text"><br> -->
<!-- 		Nombre d'exemplaires : <input name="nbOfCopy" type="number"><br> -->
<!-- 		Login du membre :<input name="login" type="text"><br> -->
<!-- 		<input type="submit" value="Ajouter" name="submit" > -->
<!-- 		<input type="submit" value="Supprimer" name="submit"> -->
<!-- 		<input type="submit" value="Emprunter" name="submit"> -->
<!-- 		<input type="submit" value="Restituer" name="submit"> -->
<!-- 	</form> -->
</div>
<% if(request.getAttribute("addDelete") != null && request.getAttribute("addDelete").equals("success")){ %>
	<%@ include file="addDeleteBook.html" %>
<%} %>
<% if(request.getAttribute("borrowRestitute") != null && request.getAttribute("borrowRestitute").equals("success")){ %>
	<%@ include file="borrowRestituteBook.html" %>
<%} %>
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
<% if(request.getAttribute("add") != null && request.getAttribute("add").equals("success")){ %>
	<h3>Livre ajouté !</h3>
<% }else{
	if(request.getAttribute("add") != null && request.getAttribute("add").equals("failed")){ %>
	<h3>Le livre existe déjà !</h3>	
<% }
} %>
<% if(request.getAttribute("delete") != null && request.getAttribute("delete").equals("success")){ %>
	<h3>Livre supprimé !</h3>
<% }else{
	if(request.getAttribute("delete") != null && request.getAttribute("delete").equals("failed")){ %>
	<h3>Impossible de supprimer le livre, soit il n'existe pas,  
	soit le nombre d'exemlplaire à supprimer est suppérieur au nombre d'exemplaire restant  !</h3>	
<% }
} %>
<% if(request.getAttribute("borrow") != null && request.getAttribute("borrow").equals("success")){ %>
	<h3>Livre emprunté !</h3>
<% }else{
	if(request.getAttribute("borrow") != null && request.getAttribute("borrow").equals("failed")){ %>
	<h3>Plus d'exemplaire disponible !</h3>	
<% }
} %>
<% if(request.getAttribute("bookRestitution") != null && request.getAttribute("bookRestitution").equals("success")){ %>
	<h3>Livre restitué !</h3>
<% }else{
	if(request.getAttribute("bookRestitution") != null && request.getAttribute("bookRestitution").equals("failed")){ %>
	<h3>Ce livre ne correspond à aucun emprunt !</h3>	
<% }
} %>
</body>
</html>
<% } else { %>
<h3>Vous n'êtes pas connecté !</h3>
<%
}%>