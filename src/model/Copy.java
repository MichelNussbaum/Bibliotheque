package model;

public class Copy {
	private int numberOfTheCopy;
	private boolean borrowed;

	public Copy(int numberOfTheCopy, boolean borrowed) {
		super();
		this.numberOfTheCopy = numberOfTheCopy;
		this.borrowed = borrowed;
	}

	public int getNumberOfTheCopy() {
		return numberOfTheCopy;
	}

	public void setNumberOfTheCopy(int numberOfTheCopy) {
		this.numberOfTheCopy = numberOfTheCopy;
	}
	
	public void borrowCopy(){
		this.borrowed = true;
	}
	
	public void returnCopy(){
		this.borrowed = false;
	}

	public boolean isBorrowed() {
		return borrowed;
	}

	public void setBorrowed(boolean borrowed) {
		this.borrowed = borrowed;
	}
	
	
	
	
}
