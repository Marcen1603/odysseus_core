package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

/**
 * This class represents a rated connection between two objects. It could be used to define the connections between cars within
 * the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public class Connection implements IConnection {

	private Object left;
	private Object right;
	private double rating;

	public Connection(Object left, Object right, double rating) {
		this.left = left;
		this.right = right;
		this.rating = rating;
	}

	public Connection(Connection copy) {
		this.left = copy.getLeft();
		this.right = copy.getRight();
		this.rating = copy.getRating();
	}

	public Object getLeft() {
		return this.left;
	}

	public Object getRight() {
		return this.right;
	}

	public void setLeft(Object newleft) {
		this.left = newleft;
	}

	public void setRight(Object newright) {
		this.right = newright;
	}

	public double getRating() {
		return this.rating;
	}

	public void setRating(double newrating) {
		this.rating = newrating;
	}

}
