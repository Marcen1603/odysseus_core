package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

/**
 * This class represents an interface for an rated connection between two objects. It could be used to define the
 * connections between cars within the association process in the objecttracking architecture.
 *
 * @author Volker Janz
 *
 */
public interface IConnection {

	public TupleIndexPath getLeftPath();
	public TupleIndexPath getRightPath();

	public void setLeftPath(TupleIndexPath newleft);
	public void setRightPath(TupleIndexPath newright);

	public double getRating();
	public void setRating(double newrating);

}
