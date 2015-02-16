package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

public class RawQueryTrajectory extends AbstractRawTrajectory {

	public RawQueryTrajectory(List<Point> points, Map<String, String> textualAttributes) {
		super(points, textualAttributes);
	}

}
