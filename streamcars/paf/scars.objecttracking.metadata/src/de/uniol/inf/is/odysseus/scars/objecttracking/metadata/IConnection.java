package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

/**
 * This class represents an interface for an rated connection between two objects. It could be used to define the
 * connections between cars within the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public interface IConnection {

	public int[] getLeftPath();
	public int[] getRightPath();

	public void setLeftPath(int[] newleft);
	public void setRightPath(int[] newright);

	public double getRating();
	public void setRating(double newrating);

}
