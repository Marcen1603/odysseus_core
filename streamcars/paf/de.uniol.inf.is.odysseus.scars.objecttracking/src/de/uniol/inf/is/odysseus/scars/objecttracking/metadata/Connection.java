package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

public abstract class Connection<L, R, W extends java.lang.Number> implements IConnection<L, R, W> {
	
	private L left;
	private R right;
	private W rating;
	
	public L getLeft() {
		return this.left;
	}
	
	public R getRight() {
		return this.right;
	}
	
	public void setLeft(L newleft) {
		this.left = newleft;
	}
	
	public void setRight(R newright) {
		this.right = newright;
	}
	
	public W getRating() {
		return this.rating;
	}
	
	public void setRating(W newrating) {
		this.rating = newrating;
	}

}
