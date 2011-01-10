package de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class defines an anchor of a state which can hold one end of a
 * connection.
 * 
 * @author Christian
 */
public class Anchor extends AbstractConnectionAnchor {

	// holds the location of this anchor
	protected Point place;

	/**
	 * This is the constructor.
	 * 
	 * @param owner
	 */
	public Anchor(IFigure owner) {
		super(owner);
	}

	/**
	 * This method returns the location of this anchor.
	 * 
	 * @param point
	 *            is not used.
	 * @return Returns the location of this anchor.
	 */
	public Point getLocation(Point point) {
		Rectangle rect = getOwner().getBounds();
		int x = rect.x + place.x * rect.width / 2;
		int y = rect.y + place.y * rect.height / 2;
		Point location = new PrecisionPoint(x, y);
		getOwner().translateToAbsolute(location);
		return location;
	}

}