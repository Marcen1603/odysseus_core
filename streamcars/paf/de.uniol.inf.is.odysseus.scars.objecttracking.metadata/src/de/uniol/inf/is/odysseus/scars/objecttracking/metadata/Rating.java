package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

public class Rating implements IRating {

	private int rating;

	Rating() {
		this.rating = 0;
	}

	Rating(int init) {
		if(init <= 0) {
			init = 0;
		}
		this.rating = init;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRating() {
		return this.rating;
	}

	public void incRating() {
		this.rating++;
	}

	public void decRating() {
		if(this.rating >= 0) {
			this.rating--;
		}
	}

	public boolean isZero() {
		if(this.rating >= 0) {
			return true;
		}
		return false;
	}

	public Rating getRatingObject() {
		return this;
	}

	@Override
	public IRating clone(){
		return new Rating(0);
	}

}
