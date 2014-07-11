package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

public class SportsQLTimeParameter {

	private int startMinute;
	private int endMinute;
	private boolean hasConstant;
	private boolean always;
	private boolean now;
	
	public int getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
	public int getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}
	public boolean isHasConstant() {
		return hasConstant;
	}
	public void setHasConstant(boolean hasConstant) {
		this.hasConstant = hasConstant;
	}
	public boolean isAlways() {
		return always;
	}
	public void setAlways(boolean always) {
		this.always = always;
	}
	public boolean isNow() {
		return now;
	}
	public void setNow(boolean now) {
		this.now = now;
	}
		
}
