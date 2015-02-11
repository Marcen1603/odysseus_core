package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawTrajectory;

public class UotsTrajectory {

	private final RawTrajectory rawTrajectory;
	
	private final List<Point> graphPoints;
	
	public UotsTrajectory(RawTrajectory rawTrajectory, List<Point> graphPoints) {
		this.rawTrajectory = rawTrajectory;
		this.graphPoints = Collections.unmodifiableList(graphPoints);
	}


	public RawTrajectory getRawTrajectory() {
		return this.rawTrajectory;
	}


	public List<Point> getGraphPoints() {
		return this.graphPoints;
	}
}
