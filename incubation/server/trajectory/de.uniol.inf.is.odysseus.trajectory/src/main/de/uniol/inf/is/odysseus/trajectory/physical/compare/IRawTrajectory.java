package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

public interface IRawTrajectory {

	public List<Point> getPoints();
	
	public Map<String, String> getTextualAttributes();
}
