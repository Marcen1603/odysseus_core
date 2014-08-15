package de.uniol.inf.is.odysseus.sports.sportsql.parser.model;

import java.awt.Point;

public class Space {
	
	private Point start;
	private Point end;
	
	public Space(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
	
}