package model;

public class Book {
	private String author;
	private String title;
	private int nbCopy;
	
	public Book(String author, String title, int nbCopy) {
		this.author = author;
		this.title = title;
		this.nbCopy = nbCopy;
	}

	public int getNbCopy() {
		return nbCopy;
	}

	public void setNbCopy(int nbCopy) {
		this.nbCopy = nbCopy;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
