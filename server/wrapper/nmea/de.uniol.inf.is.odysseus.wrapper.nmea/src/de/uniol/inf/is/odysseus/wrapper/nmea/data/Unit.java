package de.uniol.inf.is.odysseus.wrapper.nmea.data;

public enum Unit {
	METERS("M"),
	NULL("");
	
	private String shortName;
	
	Unit(String shortName) {
		this.shortName = shortName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public static Unit parse(String s) {
		if (s.equals("M")) {
			return METERS;
		} else {
			return NULL;
		}
	}
}
