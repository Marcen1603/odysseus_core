package de.uniol.inf.is.odysseus.sports.sportsql.parser.parameter;

import java.math.BigInteger;

public class SportsQLTimeParameter implements ISportsQLParameter{
	
	public static final String TIME_PARAMETER_ALWAYS = "always";
	public static final String TIME_PARAMETER_NOW = "now";
	public static final String TIME_PARAMTER_GAME = "game";

	public enum TimeUnit {
		minutes,
		seconds,
		milliseconds,
		picoseconds
	}
	
	BigInteger start;
	BigInteger end;
	String time; // For "now", "always", etc.
	TimeUnit unit; // for minutes, seconds etc.
	
	public SportsQLTimeParameter(BigInteger start, BigInteger end, String time, TimeUnit timeUnit) {
		super();
		this.start = start;
		this.end = end;
		this.time = time;
		this.unit = timeUnit;
	}
	
	public SportsQLTimeParameter() {

	}
	
	public BigInteger getStart() {
		return start;
	}
	public void setStart(BigInteger start) {
		this.start = start;
	}
	public BigInteger getEnd() {
		return end;
	}
	public void setEnd(BigInteger end) {
		this.end = end;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}
	
}
