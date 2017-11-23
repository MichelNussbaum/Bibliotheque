package model;

import java.util.ArrayList;

public class Library {
	private ArrayList<Book> books;
	private ArrayList<Borrow> borrows;
	private ArrayList<Reservation> reservations;

	public Library(ArrayList<Book> books,ArrayList<Borrow> borrows, ArrayList<Reservation> reservations) {
		super();
		this.books = books;
		this.borrows = borrows;
		this.reservations = reservations;
	}

	
	public ArrayList<Borrow> getBorrows() {
		return borrows;
	}


	public void setBorrows(ArrayList<Borrow> borrows) {
		this.borrows = borrows;
	}


	public ArrayList<Reservation> getReservations() {
		return reservations;
	}


	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}


	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}
	
	public int nbCopyRemaining(Book book){
		int nbOfCopyRemaining = book.getNbCopy();
		for (Borrow borrow : borrows) {
			if(borrow.getBook() == book){
				nbOfCopyRemaining--;
			}
		}
		return nbOfCopyRemaining;
	}
	
	public void addBook(Book book){
		books.add(book);
	}
}
