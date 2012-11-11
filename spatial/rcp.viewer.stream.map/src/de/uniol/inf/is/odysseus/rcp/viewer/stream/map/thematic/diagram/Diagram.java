package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.diagram;

import java.util.LinkedList;

import com.vividsolutions.jts.geom.Geometry;

public class Diagram {
	private Geometry location;
	private LinkedList<Integer> valueList;
	public Diagram(Geometry location, LinkedList<Integer> valueList) {
		super();
		this.location = location;
		this.valueList = valueList;
	}
	public Geometry getLocation() {
		return location;
	}
	public void setLocation(Geometry location) {
		this.location = location;
	}
	public LinkedList<Integer> getValueList() {
		return valueList;
	}
	public void setValueList(LinkedList<Integer> valueList) {
		this.valueList = valueList;
	}
}
