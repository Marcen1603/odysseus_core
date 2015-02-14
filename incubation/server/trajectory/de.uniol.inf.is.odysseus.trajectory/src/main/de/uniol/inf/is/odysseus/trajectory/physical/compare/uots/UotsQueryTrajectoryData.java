package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

public class UotsQueryTrajectoryData {

	private final List<Point> expansionCenters;
	
	public UotsQueryTrajectoryData(List<Point> expansionCenters) {
		this.expansionCenters = expansionCenters;
	}

	public List<Point> getExpansionCenters() {
		return expansionCenters;
	}
}
