package model;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Stateless
public class SearchBean implements SearchInterface{
	
	
	@Override
	public void search(HttpServletRequest request, HttpServletResponse response, Library library) throws ServletException, IOException {
		System.out.println("library search: " + library);
		if (request.getParameter("title").isEmpty() && request.getParameter("author").isEmpty()){
			request.setAttribute("search", "failed");
		}
		else if ( !request.getParameter("title").isEmpty() && request.getParameter("author").isEmpty()){
			searchByTitle(request, response, library);
		}
		else if ( request.getParameter("title").isEmpty() && !request.getParameter("author").isEmpty()){
			searchByAuthor(request, response, library);
		}
		else{
			searchByTitleAndAuthor(request,response, library);
		}
	}

	@Override
	public void searchByTitleAndAuthor(HttpServletRequest request, HttpServletResponse response, Library library) throws ServletException, IOException {
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

	@Override
	public void searchByAuthor(HttpServletRequest request, HttpServletResponse response, Library library) throws IOException, ServletException {
		ArrayList<Book> listOfBooks = new ArrayList<>();
		String author = request.getParameter("author");
		System.out.println("library.getBooks() "+library.getBooks());
		System.out.println("library author "+library);
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

	@Override
	public void searchByTitle(HttpServletRequest request, HttpServletResponse response, Library library) throws IOException, ServletException {
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
	
}
