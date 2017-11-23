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
		Titre : <input name="title" type="text"><br>
		Auteur : <input name="author" type="text"><br>
		Nombre d'exemplaires : <input name="nbOfCopy" type="number"><br>
		Login du membre :<input name="login" type="text"><br>
		<input type="submit" value="Ajouter" name="submit" >
		<input type="submit" value="Supprimer" name="submit">
		<input type="submit" value="Emprunter" name="submit">
		<input type="submit" value="Restituer" name="submit">
	</form>
</div>
</body>
</html>