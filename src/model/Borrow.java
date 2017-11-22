package model;

public class Borrow {
	private String member;
	private Book book;
	
	public Borrow(String member, Book book) {
		super();
		this.member = member;
		this.book = book;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	
}
