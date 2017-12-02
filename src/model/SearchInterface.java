package model;

import java.io.IOException;

import javax.ejb.Local;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Local
public interface SearchInterface {
	
	public void search(HttpServletRequest request,HttpServletResponse response, Library library) throws ServletException, IOException;

	public void searchByTitleAndAuthor(HttpServletRequest request, HttpServletResponse response, Library library) throws ServletException, IOException;
	
	public void searchByAuthor(HttpServletRequest request, HttpServletResponse response, Library library) throws IOException, ServletException; 
	
	public void searchByTitle(HttpServletRequest request, HttpServletResponse response, Library library) throws IOException, ServletException; 
}
