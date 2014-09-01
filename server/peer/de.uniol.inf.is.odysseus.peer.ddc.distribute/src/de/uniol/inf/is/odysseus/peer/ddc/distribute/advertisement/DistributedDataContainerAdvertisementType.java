package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement;

/**
 * Enum for different types of DDCAdvertisements
 * 
 * @author ChrisToenjesDeye
 * 
 */
public enum DistributedDataContainerAdvertisementType {
	initialDistribution, changeDistribution;

	public static DistributedDataContainerAdvertisementType parse(
			String textValue) {
		for (DistributedDataContainerAdvertisementType ddcAdvertisementType : DistributedDataContainerAdvertisementType.class
				.getEnumConstants()) {
			if (ddcAdvertisementType.toString().equalsIgnoreCase(textValue)) {
				return ddcAdvertisementType;
			}
		}
		return null;
	}

}
