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
import model.Library;
import model.Reservation;
import model.User;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Library library;
	private ArrayList<User> users;
    /**
     * Default constructor. 
     */
    public Controller() {
        // TODO Auto-generated constructor stub
    	this.library = setupTestLibrary();
    	this.users = setupTestUsers();
    }

	private ArrayList<User> setupTestUsers() {

		return null;
	}

	private Library setupTestLibrary() {
		// TODO Auto-generated method stub
		Book maxEtLili = new Book("Leslie","Max et Lili en vacances",3);
		Book bouleEtBill = new Book("Michel","Boule et Bill en vacances",2);
		ArrayList<Book> books = new ArrayList<Book>();
		books.add(maxEtLili);
		books.add(bouleEtBill);
		ArrayList<Borrow> borrows = new ArrayList<Borrow>();
		borrows.add(new Borrow("Leslie", maxEtLili));
		borrows.add(new Borrow("Michel", bouleEtBill));
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		Library library = new Library(books,borrows,reservations); 
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
					
				}
					 
		}
	}

	private void addBook(HttpServletRequest request, HttpServletResponse response) {
		Book book = new Book(request.getParameter("author"), request.getParameter("title"), Integer.parseInt(request.getParameter("nbOfCopy")));
		library.addBook(book);
	}

	private void searchByAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Book b = null;
		for (Book book : library.getBooks()) {
			String author = request.getParameter("titleOrAuthor");
			if(author.equals(book.getAuthor())){
				b = book;
			}
		}
		if(b!=null){
			response.sendRedirect("Home.jsp?search=success&title="+b.getTitle()+"&author="+b.getAuthor()+"&nbOfCopy="+b.getNbCopy()+"&remaining="+library.nbCopyRemaining(b));
		}else{
			response.sendRedirect("Home.jsp?search=failed");
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
		if(b!=null){
			response.sendRedirect("Home.jsp?search=success&title="+b.getTitle()+"&author="+b.getAuthor()+"&nbOfCopy="+b.getNbCopy()+"&remaining="+library.nbCopyRemaining(b));
		}else{
			response.sendRedirect("Home.jsp?search=failed");
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
		if (session.getAttribute("login").equals("leslie") && session.getAttribute("password").equals("ros")){
			response.sendRedirect("ConnectedLibrarian.jsp");
			session.setAttribute("userType", "librarian");
		}else if(session.getAttribute("login").equals("michel") && session.getAttribute("password").equals("nuss")){
			response.sendRedirect("ConnectedMember.jsp");
			session.setAttribute("userType", "member");
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