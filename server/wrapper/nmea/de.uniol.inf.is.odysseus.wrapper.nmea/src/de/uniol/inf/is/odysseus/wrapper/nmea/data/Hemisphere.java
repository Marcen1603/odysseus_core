package de.uniol.inf.is.odysseus.wrapper.nmea.data;

public enum Hemisphere {
	NORTH("N", 1),
	EAST("E", 1),
	SOUTH("S", -1),
	WEST("W", -1),
	NULL("", 0);
	
	private String shortName;
	private int sign;
	
	Hemisphere(String shortName, int sign) {
		this.shortName = shortName;
		this.sign = sign;
	}
	
	public String getShortName() {
		return this.shortName;
	}
	
	public int getSign() {
		return sign;
	}
	
	public static Hemisphere parse(String s) {
		if (s.equals("N")) {
			return NORTH;
		} else if (s.equals("E")) {
			return EAST;
		} else if (s.equals("S")) {
			return SOUTH;
		} else if (s.equals("W")) {
			return WEST;
		} else {
			return NULL;
		}
	}
}
