<div style="">	
	<h1>Voici le résultat de votre recherche : </h1>
	<div style='border-style:solid; padding:5px'>
		<h2>Auteur : <%=request.getParameter("author") %></h2>
		<h2>Titre : <%=request.getParameter("title") %></h2>
		<h2>Nombre d'exemplaire total : <%=request.getParameter("nbOfCopy") %></h2>
		<h2>Nombre d'exemplaire restant : <%=request.getParameter("remaining") %></h2>
	</div>
</div>