package controller;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Book;
import model.Borrow;
import model.Librarian;
import model.Library;
import model.Member;
import model.Reservation;
import model.User;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Library library;
	
    /**
     * Default constructor. 
     */
    public Controller() {
        // TODO Auto-generated constructor stub
    	this.library = setupTestLibrary();
    }

	private ArrayList<User> setupTestUsers() {
		ArrayList<User> users = new ArrayList<User>();
		User u1 = new Librarian("Leslie", "ros");
		User u2 = new Member("Michel","nuss");
		users.add(u1);
		users.add(u2);
		return users;
	}

	private Library setupTestLibrary() {
		// TODO Auto-generated method stub
		ArrayList<User> users = setupTestUsers();
		
		Book maxEtLili = new Book("Leslie","Max et Lili en vacances",3);
		Book bouleEtBill = new Book("Michel","Boule et Bill en vacances",2);
		ArrayList<Book> books = new ArrayList<Book>();
		books.add(maxEtLili);
		books.add(bouleEtBill);
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		ArrayList<Borrow> borrows = new ArrayList<Borrow>();
		Library library = new Library(books,borrows,reservations, users); 		
		borrows.add(new Borrow((Member)library.getUsers().get(1), maxEtLili));
		borrows.add(new Borrow((Member)library.getUsers().get(1), bouleEtBill));
		return library;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		switch(request.getParameter("form")){
			case "connection":
				connection(request,response);
			break;
			
			case "deconnection":
				deconnection(request,response);
			break;
			
			case "search":
				if(request.getParameter("typeOfSearch").equals("title")){
					searchByTitle(request,response);
				}else if(request.getParameter("typeOfSearch").equals("author")){
					searchByAuthor(request,response);
				}else{
					break;
				}
			break;
			
			case "actionLibrarian":
				switch( request.getParameter("submit")){
				case "Ajouter":
					addBook(request, response);
					break;
				
				case "Supprimer":
					deleteBook(request, response);
					break;
					
				case "Emprunter":
					borrowBook(request, response);
					break;
				}
				
				case "Restituer":
					bookRestitution(request, response);
					break;
				
		}
	}

	private void bookRestitution(HttpServletRequest request, HttpServletResponse response) {
		Book book =  library.getBook(request.getParameter("title"), request.getParameter("author"));
		
	}

	private void borrowBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Book book =  library.getBook(request.getParameter("title"), request.getParameter("author"));
		if (library.nbCopyRemaining(book) != 0){
			library.getBorrows().add(new Borrow((Member)library.getUserFromLogin(request.getParameter("login")),book));
			response.sendRedirect("ConnectedLibrarian.jsp?borrow=success");
		}
		else {
			response.sendRedirect("ConnectedLibrarian.jsp?borrow=failed");
		}
					
	}

	private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		library.deleteBook(library.getBook(request.getParameter("title"), request.getParameter("author")));
		response.sendRedirect("ConnectedLibrarian.jsp");
	}

	private void addBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Book book = new Book(request.getParameter("author"), request.getParameter("title"), Integer.parseInt(request.getParameter("nbOfCopy")));
		library.addBook(book);
		response.sendRedirect("ConnectedLibrarian.jsp");
	}

	private void searchByAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Book b = null;
		for (Book book : library.getBooks()) {
			String author = request.getParameter("titleOrAuthor");
			if(author.equals(book.getAuthor())){
				b = book;
			}
		}
		HttpSession session = request.getSession();
		if(b!=null){
			if(session.getAttribute("userType") != null){
				response.sendRedirect(session.getAttribute("userType").toString()+".jsp?search=success&title="+b.getTitle()+"&author="+b.getAuthor()+"&nbOfCopy="+b.getNbCopy()+"&remaining="+library.nbCopyRemaining(b));
			}else{ 
				response.sendRedirect("Home.jsp?search=success&title="+b.getTitle()+"&author="+b.getAuthor()+"&nbOfCopy="+b.getNbCopy()+"&remaining="+library.nbCopyRemaining(b));
			}
		}else{
			if(session.getAttribute("userType") != null){
				response.sendRedirect(session.getAttribute("userType").toString()+".jsp?search=failed");
			}else{
				response.sendRedirect("Home.jsp?search=failed");
			}
		}
	}

	private void searchByTitle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Book b = null;
		for (Book book : library.getBooks()) {
			String title = request.getParameter("titleOrAuthor");
			if(title.equals(book.getTitle())){
				b = book;
			}
		}
		HttpSession session = request.getSession();
		if(b!=null){
			
			if(session.getAttribute("userType") != null){
				response.sendRedirect(session.getAttribute("userType").toString()+".jsp?search=success&title="+b.getTitle()+"&author="+b.getAuthor()+"&nbOfCopy="+b.getNbCopy()+"&remaining="+library.nbCopyRemaining(b));
			}else{ 
				response.sendRedirect("Home.jsp?search=success&title="+b.getTitle()+"&author="+b.getAuthor()+"&nbOfCopy="+b.getNbCopy()+"&remaining="+library.nbCopyRemaining(b));
			}
			
		}else{
			if(session.getAttribute("userType") != null){
				response.sendRedirect(session.getAttribute("userType").toString()+".jsp?search=failed");
			}else{
				response.sendRedirect("Home.jsp?search=failed");
			}
		}
	}

	private void deconnection(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect("Home.jsp");
	}

	private void connection(HttpServletRequest request,HttpServletResponse response) throws IOException {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}