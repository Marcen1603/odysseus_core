package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLTimeParameter implements ISportsQLParameter{
	
	public static final String TIME_PARAMETER_ALWAYS = "always";
	public static final String TIME_PARAMETER_NOW = "now";
	public static final String TIME_PARAMTER_GAME = "game";
	
	public static final String TIME_UNIT_MINUTES 		= "minutes";
	public static final String TIME_UNIT_SECONDS 		= "seconds";
	public static final String TIME_UNIT_MILLISECONDS 	= "milliseconds";
	public static final String TIME_UNIT_PICOSECONDS 	= "picoseconds";

	int start;
	int end;
	String time; // For "now", "always", etc.
	String unit; // for minutes, seconds etc.
	
	public SportsQLTimeParameter(int start, int end, String time, String timeUnit) {
		super();
		this.start = start;
		this.end = end;
		this.time = time;
		this.unit = timeUnit;
	}
	
	public SportsQLTimeParameter() {

	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
