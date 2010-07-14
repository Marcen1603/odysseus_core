package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

public class Connection<L, R, W extends java.lang.Number> implements IConnection<L, R, W> {
	
	private L left;
	private R right;
	private W rating;
	
	public Connection(L left, R right, W rating) {
		this.left = left;
		this.right = right;
		this.rating = rating;
	}
	
	public Connection(Connection<L, R, W> copy) {
		this.left = copy.getLeft();
		this.right = copy.getRight();
		this.rating = copy.getRating();
	}
	
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
