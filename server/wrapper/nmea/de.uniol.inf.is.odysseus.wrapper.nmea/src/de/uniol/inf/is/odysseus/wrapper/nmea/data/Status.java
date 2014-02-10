package de.uniol.inf.is.odysseus.wrapper.nmea.data;

public enum Status {
	OK("A"),
	WARNING("V"),
	NULL("");
	
	private String shortName;
	
	Status(String shortName) {
		this.shortName = shortName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public static Status parse(String s) {
		if (s.equals("A")) {
			return OK;
		} else if (s.equals("V")) {
			return WARNING;
		} else {
			return NULL;
		}
	}
}
