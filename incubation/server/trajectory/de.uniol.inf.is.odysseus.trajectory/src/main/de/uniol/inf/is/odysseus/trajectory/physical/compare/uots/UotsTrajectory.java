package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.AbstractDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawDataTrajectory;

public class UotsTrajectory extends AbstractDataTrajectory {

	private final List<Point> graphPoints;
	
	public UotsTrajectory(RawDataTrajectory rawTrajectory, List<Point> graphPoints) {
		super(rawTrajectory);
		this.graphPoints = Collections.unmodifiableList(graphPoints);
	}
	
	public List<Point> getGraphPoints() {
		return this.graphPoints;
	}
}
