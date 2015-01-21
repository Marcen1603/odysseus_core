package de.uniol.inf.is.odysseus.trajectory.physical;

import java.util.List;

import com.vividsolutions.jts.geom.Point;


public class Trajectory {
	
	private final String id;
	
	private final List<Point> points;
	
	private final long begin;
	
	private final long end;

	public Trajectory(String id, List<Point> points, long begin, long end) {
		this.id = id;
		this.points = points;
		this.begin = begin;
		this.end = end;
	}

	public String getId() {
		return id;
	}

	public List<Point> getPoints() {
		return points;
	}

	public long getBegin() {
		return begin;
	}

	public long getEnd() {
		return end;
	}
}