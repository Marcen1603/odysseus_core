package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

public interface IConnection<L, R, W extends java.lang.Number> {
	
	public L getLeft();
	public R getRight();
	
	public void setLeft(L newleft);
	public void setRight(R newright);
	
	public W getRating();
	public void setRating(W newrating);

}
