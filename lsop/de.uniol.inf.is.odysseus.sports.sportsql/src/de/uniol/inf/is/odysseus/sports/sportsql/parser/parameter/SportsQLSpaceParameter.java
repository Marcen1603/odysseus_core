package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLSpaceParameter implements ISportsQLParameter {

	public enum SpaceType {
		all,
		field,
		left_half,
		right_half,
		left_third,
		middle_third,
		right_third,
		top_half,
		bottom_half,
		top_fifth,
		bottom_fifth,
		quarter_field_right,
		quarter_field_left,
	}
	
	public enum SpaceUnit {
        meters,
        centimeters,
        millimeters
    }

	int startx;
	int endx;
	int starty;
	int endy;
	SpaceType space; // For "all", ...
	SpaceUnit unit;

	public SportsQLSpaceParameter(int startx, int endx, int starty, int endy,
			SpaceType space, SpaceUnit unit) {
		super();
		this.startx = startx;
		this.endx = endx;
		this.starty = starty;
		this.endy = endy;
		this.space = space;
		this.unit = unit;
	}
	
	public SportsQLSpaceParameter() {
	}

	public int getStartx() {
		return startx;
	}

	public void setStartx(int startx) {
		this.startx = startx;
	}

	public int getEndx() {
		return endx;
	}

	public void setEndx(int endx) {
		this.endx = endx;
	}

	public int getStarty() {
		return starty;
	}

	public void setStarty(int starty) {
		this.starty = starty;
	}

	public int getEndy() {
		return endy;
	}

	public void setEndy(int endy) {
		this.endy = endy;
	}

	public SpaceType getSpace() {
		return space;
	}

	public void setSpace(SpaceType space) {
		this.space = space;
	}

	public SpaceUnit getUnit() {
		return unit;
	}

	public void setUnit(SpaceUnit unit) {
		this.unit = unit;
	}
	
}
