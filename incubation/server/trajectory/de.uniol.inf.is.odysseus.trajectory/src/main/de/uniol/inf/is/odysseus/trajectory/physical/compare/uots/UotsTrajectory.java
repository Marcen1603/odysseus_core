package de.uniol.inf.is.odysseus.trajectory.physical.compare.uots;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.physical.compare.AbstractAdvancedTrajectory;
import de.uniol.inf.is.odysseus.trajectory.physical.compare.RawTrajectory;

public class UotsTrajectory extends AbstractAdvancedTrajectory {

	private final List<Point> graphPoints;
	
	private Iterator<Point> iterator;
	
	public UotsTrajectory(RawTrajectory rawTrajectory, List<Point> graphPoints) {
		super(rawTrajectory);
		this.graphPoints = Collections.unmodifiableList(graphPoints);
	}
	
	public List<Point> getGraphPoints() {
		return this.graphPoints;
	}

	public Iterator<Point> getIterator() {
		return iterator;
	}

	public void setIterator(Iterator<Point> iterator) {
		this.iterator = iterator;
	}
}
