package model;

import java.io.IOException;

import javax.ejb.Stateful;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Stateful
public class ConnectionSystemBean implements ConnectionSystemInterface{
	

	public void deconnection(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect("Home.jsp");
	}

	public void connection(HttpServletRequest request,HttpServletResponse response, Library library) throws IOException {
		// TODO Auto-generated method stub
		HttpSession session;
		if (request.getParameter("login") != null){
			String login = request.getParameter("login"); 
			String password = request.getParameter("password");
			session = request.getSession();
			session.setAttribute("login", login);
			session.setAttribute("password", password);
		}
		else{
			session= request.getSession(false);
		}
		if (session.getAttribute("login") != null){
			for(User u : library.getUsers()){
				if(u.getLogin().equals(session.getAttribute("login"))){
					if(u.getPassword().equals(session.getAttribute("password"))){
						if(u instanceof Librarian){
							response.sendRedirect("ConnectedLibrarian.jsp");
							session.setAttribute("userType", "ConnectedLibrarian");
						}else if(u instanceof Member){
							response.sendRedirect("ConnectedMember.jsp");
							session.setAttribute("userType", "ConnectedMember");
						}
					}else{
						System.out.println("Mot de passe erroné");
					}
				}
			}
		}else{
			response.sendRedirect("Home.jsp");
		}
	}
}
