package model;

import java.util.ArrayList;

public class Book {
	private String author;
	private String title;
	private ArrayList<Copy> copies;
	
	public Book(String author, String title, ArrayList<Copy> copies) {
		this.author = author;
		this.title = title;
		this.copies = copies;
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

	public ArrayList<Copy> getCopies() {
		return copies;
	}

	public void setCopies(ArrayList<Copy> copies) {
		this.copies = copies;
	}
	
	public int getNumberOfCopyRemaining(){
		int res = 0;
		int borrowed = 0;
		for (Copy copy : this.copies) {
			if(copy.isBorrowed()){
				borrowed++;
			}
		}
		res = this.copies.size() - borrowed;
		return res;
	}
	
}
