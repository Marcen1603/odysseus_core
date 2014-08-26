package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;


public enum DDCAdvertisementType {
	ddcCreated,
	ddcUpdated;

	public static DDCAdvertisementType parse(String textValue) {
		for (DDCAdvertisementType ddcAdvertisementType : DDCAdvertisementType.class.getEnumConstants()) {
			if (ddcAdvertisementType.toString().equalsIgnoreCase(textValue)) {
				return ddcAdvertisementType;
			}
		}
		return null;
	}
	
}
