package controller;
import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Book;
import model.Borrow;
import model.ConnectionSystemInterface;
import model.Librarian;
import model.Library;
import model.Member;
import model.Reservation;
import model.SearchBean;
import model.SearchInterface;
import model.User;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Library library;
	
	@EJB
	SearchInterface search;
	
	@EJB
	ConnectionSystemInterface connectionSystemInterface;
	
    /**
     * Default constructor. 
     */
    public Controller() {
    	init();
    }

	public void init() {
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
		borrows.add(new Borrow((Member)users.get(1), maxEtLili));
		borrows.add(new Borrow((Member)users.get(1), bouleEtBill));
		borrows.add(new Borrow((Member)users.get(1), mich));
		borrows.add(new Borrow((Member)users.get(1), maxEtLili2));

		library = new Library(books, borrows, reservations, users);
	}

	private ArrayList<User> setupTestUsers() {
		ArrayList<User> users = new ArrayList<User>();
		User u1 = new Librarian("Leslie", "ros");
		User u2 = new Member("Michel","nuss");
		users.add(u1);
		users.add(u2);
		return users;
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		switch(request.getParameter("form")){
			case "connection":
				connectionSystemInterface.connection(request,response, library);
			break;
			
			case "deconnection":
				connectionSystemInterface.deconnection(request,response);
			break;
			
			case "search":
				search.search(request, response, library);
				
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
		if( library.getUsers().contains(member)){
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
		}
		else{
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
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}