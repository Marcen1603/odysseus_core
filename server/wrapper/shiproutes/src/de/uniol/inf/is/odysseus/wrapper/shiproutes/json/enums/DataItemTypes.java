package de.uniol.inf.is.odysseus.wrapper.shiproutes.json.enums;

public enum DataItemTypes {
	Route,
	Manoeuvre,
	Prediction;
	
	public static DataItemTypes parse(String value) {
		for (DataItemTypes iecElement : DataItemTypes.class.getEnumConstants()) {
			if (iecElement.toString().equalsIgnoreCase(value)) {
				return iecElement;
			}
		}
		return null;
	}
}
