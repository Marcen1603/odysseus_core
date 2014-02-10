package de.uniol.inf.is.odysseus.wrapper.nmea.data;

public enum SignalIntegrity {
	AUTONOMOUS_MODE("A"),
	DIFFERENTIAL_MODE("D"),
	ESTIMATED_MODE("E"),
	MANUAL_INPUT_MODE("M"),
	SIMULATED_MODE("S"),
	NOT_VALID("N"),
	NULL("");
	
	private String shortName;
	
	SignalIntegrity(String shortName) {
		this.shortName = shortName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public static SignalIntegrity parse(String s) {
		if (s.equals("A")) {
			return AUTONOMOUS_MODE;
		} else if (s.equals("D")) {
			return DIFFERENTIAL_MODE;
		} else if (s.equals("E")) {
			return ESTIMATED_MODE;
		} else if (s.equals("M")) {
			return MANUAL_INPUT_MODE;
		} else if (s.equals("S")) {
			return SIMULATED_MODE;
		} else if (s.equals("N")) {
			return NOT_VALID;
		} else {
			return NULL;
		}
	}
}
