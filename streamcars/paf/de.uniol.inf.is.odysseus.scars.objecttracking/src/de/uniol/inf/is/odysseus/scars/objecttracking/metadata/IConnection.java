package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

/**
 * This class represents an interface for an rated connection between two objects. It could be used to define the
 * connections between cars within the association process in the objecttracking architecture.
 * 
 * @author Volker Janz
 *
 * @param <L> Datatype of the left object
 * @param <R> Datatype of the right object
 * @param <W> Datatype of the rating - has to extend java.lang.Number (Double, Integer, ...)
 */
public interface IConnection<L, R, W extends java.lang.Number> {
	
	public L getLeft();
	public R getRight();
	
	public void setLeft(L newleft);
	public void setRight(R newright);
	
	public W getRating();
	public void setRating(W newrating);

}
