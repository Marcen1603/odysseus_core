package de.uniol.inf.is.odysseus.trajectory.compare.util;

import com.vividsolutions.jts.geom.Point;

public interface IPointCreator {

	public Point createPoint(double x, double y);
}
