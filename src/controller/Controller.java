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
		Book maxEtLili2 = new Book("Jacques","Max et Lili en vacances",6);
		Book bouleEtBill = new Book("Michel","Boule et Bill en vacances",2);
		Book bouleEtBill2 = new Book("Paul","Boule et Bill en vacances",20);
		Book mich = new Book("Michel", "Boule et Bill à l'école", 14);
		Book leslie = new Book("Leslie", "Max et Lili à l'école", 8);
		ArrayList<Book> books = new ArrayList<Book>();
		books.add(maxEtLili);
		books.add(bouleEtBill);
		books.add(leslie);
		books.add(bouleEtBill2);
		books.add(maxEtLili2);
		books.add(mich);
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		ArrayList<Borrow> borrows = new ArrayList<Borrow>();
		Library library = new Library(books,borrows,reservations, users); 		
		borrows.add(new Borrow((Member)library.getUsers().get(1), maxEtLili));
		borrows.add(new Borrow((Member)library.getUsers().get(1), bouleEtBill));
		borrows.add(new Borrow((Member)library.getUsers().get(1), mich));
		borrows.add(new Borrow((Member)library.getUsers().get(1), maxEtLili2));
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
				search(request, response);
				
			break;
			
			case "actionLibrarian":
				switch (request.getParameter("submit")) {
				case "Ajouter/Supprimer livre/exemplaire":
					request.setAttribute("addDelete", "success");;
					break;
					
				case "Emprunter/Restituer un livre":
					request.setAttribute("borrowRestitute", "success");
					break;

				default:
					break;
				}
				request.getRequestDispatcher("ConnectedLibrarian.jsp").forward(request, response);
			break;
			
			case "addDeleteBook":
				switch (request.getParameter("submit")) {
				case "Ajouter":
					addBook(request, response);
					break;
					
				case "Supprimer":
					deleteBook(request, response);
					break;

				default:
					break;
				}
			break;
			
			case "borrowRestituteBook":
				switch (request.getParameter("submit")) {
				case "Emprunter":
					borrowBook(request, response);
					break;
					
				case "Restituer":
					bookRestitution(request, response);
					break;

				default:
					break;
				}
			break;
			
			case "actionMember":
				switch (request.getParameter("submit")) {
				case "Reserver":
						bookingBook(request,response);
					break;
					
				case "Annuler reservation":
						cancelBookingBook(request,response);
					break;

				default:
					break;
				}
			break;
					
		}
			
	}

	private void cancelBookingBook(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		Book book =  library.getBook(request.getParameter("title"), request.getParameter("author"));
		Member member = (Member)library.getUserFromLogin(session.getAttribute("login").toString());
		Reservation reservation = library.getReservation(member, book); 
		if (reservation != null){
			library.getReservations().remove(reservation);
			request.setAttribute("bookingCancelation", "success");
		}
		else {
			request.setAttribute("bookingCancelation", "failed");
		}
		request.getRequestDispatcher("ConnectedMember.jsp").forward(request, response);
	}

	private void bookingBook(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		Book book =  library.getBook(request.getParameter("title"), request.getParameter("author"));
		int remaining = library.nbCopyRemaining(book);
		if (remaining >= 1){
			library.getReservations().add(new Reservation((Member)library.getUserFromLogin(session.getAttribute("login").toString()),book));
			request.setAttribute("reservation", "success");
		}
		else {
			request.setAttribute("reservation", "failed");
		}
		request.getRequestDispatcher("ConnectedMember.jsp").forward(request, response);
	}

	private void bookRestitution(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Book book =  library.getBook(request.getParameter("title"), request.getParameter("author"));
		Member member = (Member)library.getUserFromLogin(request.getParameter("login"));
		Borrow borrow = library.getBorrow(member, book); 
		if (borrow != null){
			library.getBorrows().remove(borrow);
			request.setAttribute("bookRestitution", "success");
		}
		else {
			request.setAttribute("bookRestitution", "failed");
		}
		request.getRequestDispatcher("ConnectedLibrarian.jsp").forward(request, response);
	}

	private void borrowBook(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Book book = library.getBook(request.getParameter("title"), request.getParameter("author"));
		Member member = (Member) library.getUserFromLogin(request.getParameter("login"));
		boolean reserved = false;
		if (library.getReservation(member, book) != null) {
			if (library.getReservation(member, book).getBook().equals(book)) {
				library.getReservations().remove(library.getReservation(member, book));
				reserved = true;
			}				
		}
		if (library.nbCopyRemaining(book) > 0 && book != null || reserved && book != null) {
			library.getBorrows().add(new Borrow(member, book));
			request.setAttribute("borrow", "success");
		}

		else {
			request.setAttribute("borrow", "failed");
		}
		request.getRequestDispatcher("ConnectedLibrarian.jsp").forward(request, response);
	}

	private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		int nbOfCopy = 0;
		boolean borrowed = false;
		boolean reserved = false;		
		Book book = library.getBook(request.getParameter("title"), request.getParameter("author"));
		if(library.getBook(title, author) == null ){
			request.setAttribute("delete", "failed");
		}
		else{
			if (!request.getParameter("nbOfCopy").equals("")){
				 nbOfCopy = Integer.parseInt(request.getParameter("nbOfCopy"));
				 if (nbOfCopy <= library.nbCopyRemaining(book)){
					 request.setAttribute("delete", "success");
					library.deleteCopyBook(book, nbOfCopy);
				 }
				 else{
					 request.setAttribute("delete", "failed"); 
				 }
			}
			else{
				for (Borrow borrow : library.getBorrows()) {
					if (borrow.getBook().equals(library.getBook(title, author))){
						borrowed = true;
						break;
						}
				for (Reservation reservation : library.getReservations()) {
					if (reservation.getBook().equals(library.getBook(title, author))){
						reserved = true;
						break;
						}
					}
				}
				if ( !borrowed && !reserved){
					request.setAttribute("delete", "success");
					library.deleteBook(book);
				}
				else{
					request.setAttribute("delete", "failed");
				}
			}		
		}
		request.getRequestDispatcher("ConnectedLibrarian.jsp").forward(request, response);		
	}

	private void addBook(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		int nbOfCopy = Integer.parseInt(request.getParameter("nbOfCopy"));
		if (library.getBook(title, author) == null) {
			Book book = new Book(author, title, nbOfCopy);
			library.addBook(book);
			request.setAttribute("add", "success");
		} else {
			if (nbOfCopy > 0) {
				Book book = library.getBook(title, author);
				book.setNbCopy(book.getNbCopy() + nbOfCopy);
				request.setAttribute("add", "success");
			}
			else{
				request.setAttribute("add", "failed");
			}
		}
		request.getRequestDispatcher("ConnectedLibrarian.jsp").forward(request, response);
	}

	private void searchByAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ArrayList<Book> listOfBooks = new ArrayList<>();
		String author = request.getParameter("author");
		for (Book book : library.getBooks()) {
			if(author.equals(book.getAuthor())){
				listOfBooks.add(book);
			}
		}
		HttpSession session = request.getSession();
		if(listOfBooks.size() > 0){
			request.setAttribute("search", "success");
			request.setAttribute("listOfBooks", listOfBooks);
			request.setAttribute("library", library);
			if(session.getAttribute("userType") != null){
				request.getRequestDispatcher(session.getAttribute("userType").toString()+".jsp").forward(request, response);
			}else{ 
				request.getRequestDispatcher("Home.jsp").forward(request, response);
			}
		}else{
			request.setAttribute("search", "failed");
			if(session.getAttribute("userType") != null){
				request.getRequestDispatcher(session.getAttribute("userType").toString()+".jsp").forward(request, response);
			}else{
				request.getRequestDispatcher("Home.jsp").forward(request, response);
			}
		}
	}

	private void searchByTitle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ArrayList<Book> listOfBooks = new ArrayList<>();
		String title = request.getParameter("title");
		for (Book book : library.getBooks()) {
			if(title.equals(book.getTitle())){
				listOfBooks.add(book);
			}
		}
		HttpSession session = request.getSession();
		if(listOfBooks.size() > 0){
			request.setAttribute("search", "success");
			request.setAttribute("listOfBooks", listOfBooks);
			request.setAttribute("library", library);
			if(session.getAttribute("userType") != null){
				request.getRequestDispatcher(session.getAttribute("userType").toString()+".jsp").forward(request, response);
			}else{ 
				request.getRequestDispatcher("Home.jsp").forward(request, response);
			}
			
		}else{
			request.setAttribute("search", "failed");
			if(session.getAttribute("userType") != null){
				request.getRequestDispatcher(session.getAttribute("userType").toString()+".jsp").forward(request, response);
			}else{
				request.getRequestDispatcher("Home.jsp").forward(request, response);
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
	
	private void search(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("title").isEmpty() && request.getParameter("author").isEmpty()){
			request.setAttribute("search", "failed");
		}
		else if ( !request.getParameter("title").isEmpty() && request.getParameter("author").isEmpty()){
			searchByTitle(request, response);
		}
		else if ( request.getParameter("title").isEmpty() && !request.getParameter("author").isEmpty()){
			searchByAuthor(request, response);
		}
		else{
			searchByTitleAndAuthor(request,response);
		}
		
	}
	
	private void searchByTitleAndAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Book> listOfBooks = new ArrayList<>();
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		for (Book book : library.getBooks()) {			
			if(title.equals(book.getTitle()) && author.equals(book.getAuthor())){
				listOfBooks.add(book);
			}
		}
		HttpSession session = request.getSession();
		if(listOfBooks.size() > 0){
			request.setAttribute("search", "success");
			request.setAttribute("listOfBooks", listOfBooks);
			request.setAttribute("library", library);
			if(session.getAttribute("userType") != null){
				request.getRequestDispatcher(session.getAttribute("userType").toString()+".jsp").forward(request, response);
			}else{ 
				request.getRequestDispatcher("Home.jsp").forward(request, response);
			}
			
		}else{
			request.setAttribute("search", "failed");
			if(session.getAttribute("userType") != null){
				request.getRequestDispatcher(session.getAttribute("userType").toString()+".jsp").forward(request, response);
			}else{
				request.getRequestDispatcher("Home.jsp").forward(request, response);
			}
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