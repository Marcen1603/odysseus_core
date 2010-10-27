package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

/**
 * This class represents a rated connection between two objects. It could be used to define the connections between cars within
 * the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public class Connection implements IConnection {

	private int[] leftPath;
	private int[] rightPath;
	private double rating;

	public Connection(int[] leftPath, int[] rightPath, double rating) {
		this.leftPath = leftPath;
		this.rightPath = rightPath;
		this.rating = rating;
	}

	public Connection(Connection copy) {
		this.leftPath = new int[copy.getLeftPath().length];
		this.rightPath = new int[copy.getRightPath().length];
		                         
		System.arraycopy(copy.getLeftPath(), 0, this.leftPath, 0, copy.getLeftPath().length);
		System.arraycopy(copy.getRightPath(), 0, this.rightPath, 0, copy.getRightPath().length);
	}

	@Override
	public int[] getLeftPath() {
		return this.leftPath;
	}

	@Override
	public int[] getRightPath() {
		return this.rightPath;
	}

	@Override
	public void setLeftPath(int[] newleft) {
		this.leftPath = newleft;
	}

	@Override
	public void setRightPath(int[] newright) {
		this.rightPath = newright;
	}

	@Override
	public double getRating() {
		return this.rating;
	}

	@Override
	public void setRating(double newrating) {
		this.rating = newrating;
	}

}
