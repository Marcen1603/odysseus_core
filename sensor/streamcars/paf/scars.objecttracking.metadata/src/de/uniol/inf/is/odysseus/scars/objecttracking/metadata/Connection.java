package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

/**
 * This class represents a rated connection between two objects. It could be used to define the connections between cars within
 * the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public class Connection implements IConnection {

	private TupleIndexPath leftPath;
	private TupleIndexPath rightPath;
	private double rating;

	public Connection(TupleIndexPath leftPath, TupleIndexPath rightPath, double rating) {
		this.leftPath = leftPath;
		this.rightPath = rightPath;
		this.rating = rating;
	}

	public Connection(Connection copy) {
		this.leftPath = copy.getLeftPath().clone();
		this.rightPath = copy.getRightPath().clone();
	}

	@Override
	public TupleIndexPath getLeftPath() {
		return this.leftPath;
	}

	@Override
	public TupleIndexPath getRightPath() {
		return this.rightPath;
	}

	@Override
	public void setLeftPath(TupleIndexPath newleft) {
		this.leftPath = newleft;
	}

	@Override
	public void setRightPath(TupleIndexPath newright) {
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
