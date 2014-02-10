package de.uniol.inf.is.odysseus.wrapper.nmea.data;

public enum GpsQuality {
	INVALID(0),
	GPS(1),
	DGPS(2),
	ESTIMATE(6);
	
	private int value;
	
	GpsQuality(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static GpsQuality parse(String s) {
		if (s.equals("1")) {
			return GPS;
		} else if (s.equals("2")) {
			return DGPS;
		} else if (s.equals("6")) {
			return ESTIMATE;
		} else {
			return INVALID;
		}
	}
}
