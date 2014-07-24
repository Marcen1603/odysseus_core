package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLSpaceParameter implements ISportsQLParameter {

	public static final String SPACE_PARAMETER_ALL = "all";

	int startx;
	int endx;
	int starty;
	int endy;
	String space; // For "all", ...

	public SportsQLSpaceParameter(int startx, int endx, int starty, int endy,
			String space) {
		super();
		this.startx = startx;
		this.endx = endx;
		this.starty = starty;
		this.endy = endy;
		this.space = space;
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

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}
}
