package de.uniol.inf.is.odysseus.trajectory.physical.compare.test;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

public class UotsData {

	private final List<Point> expansionCenters;
	
	public UotsData(List<Point> expansionCenters) {
		this.expansionCenters = expansionCenters;
	}

	public List<Point> getExpansionCenters() {
		return this.expansionCenters;
	}
}
