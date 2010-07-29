package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

/**
 * This class represents an interface for an rated connection between two objects. It could be used to define the
 * connections between cars within the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public interface IConnection {

	public Object getLeft();
	public Object getRight();

	public void setLeft(Object newleft);
	public void setRight(Object newright);

	public double getRating();
	public void setRating(double newrating);

}
