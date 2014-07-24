package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

public class SportsQLTimeParameter implements ISportsQLParameter{
	
	public static final String TIME_PARAMETER_ALWAYS = "always";
	public static final String TIME_PARAMETER_NOW = "now";

	// TODO: time-unit (seconds, minutes, ...)
	int start;
	int end;
	String time; // For "now", "always", etc.
	
	public SportsQLTimeParameter(int start, int end, String time) {
		super();
		this.start = start;
		this.end = end;
		this.time = time;
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
	
}
