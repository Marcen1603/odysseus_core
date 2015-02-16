package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Point;

public abstract class AbstractRawTrajectory implements IRawTrajectory {

	private final List<Point> points;
	
	private final Map<String, String> textualAttributes;

	public AbstractRawTrajectory(final List<Point> points, final Map<String, String> textualAttributes) {
		this.points = Collections.unmodifiableList(points);
		this.textualAttributes = Collections.unmodifiableMap(textualAttributes);
	}

	@Override
	public List<Point> getPoints() {
		return this.points;
	}
	
	public Map<String, String> getTextualAttributes() {
		return this.textualAttributes;
	}
}