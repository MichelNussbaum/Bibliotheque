package model;

import java.io.IOException;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Local
public interface ConnectionSystemInterface {
	
	public void deconnection(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
	public void connection(HttpServletRequest request,HttpServletResponse response, Library library) throws IOException;
}
