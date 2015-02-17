package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

public class UotsData {

	private final List<Point> graphPoints;
	
	public UotsData(final List<Point> grapgPoints) {
		this.graphPoints = grapgPoints;
	}

	public List<Point> getGraphPoints() {
		return this.graphPoints;
	}
}
