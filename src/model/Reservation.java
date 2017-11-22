package model;

public class Reservation {
	private Member member;
	private Book book;
	
	public Reservation(Member member, Book book) {
		super();
		this.member = member;
		this.book = book;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	
}
