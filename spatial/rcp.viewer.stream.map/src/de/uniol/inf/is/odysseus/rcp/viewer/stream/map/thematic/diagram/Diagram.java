package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.diagram;

import java.util.LinkedList;

import com.vividsolutions.jts.geom.Point;

public class Diagram {
	private Point location;
	private LinkedList<Integer> valueList;
	public Diagram(Point location, LinkedList<Integer> valueList) {
		super();
		this.location = location;
		this.valueList = valueList;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public LinkedList<Integer> getValueList() {
		return valueList;
	}
	public void setValueList(LinkedList<Integer> valueList) {
		this.valueList = valueList;
	}
}
