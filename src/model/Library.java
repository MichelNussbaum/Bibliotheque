package model;

import java.util.ArrayList;

public class Library {
	private ArrayList<Book> books;

	public Library(ArrayList<Book> books) {
		super();
		this.books = books;
	}

	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

}
