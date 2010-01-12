package de.uniol.inf.is.odysseus.costmodel.base;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.streamCharacteristic.StreamCharacteristicCollection;

public interface ICostCalculator {
	public ICost calculateCost(List<StreamCharacteristicCollection> incomingStreamCharacteristics);

	/**
	 * Merges the {@link StreamCharacteristicCollection}s of the incoming
	 * streams into a new one which represents the metadata of the outgoing
	 * stream
	 * 
	 * @param incomingStreamMetadata
	 *            - the {@link List} of the incoming
	 *            {@link StreamCharacteristicCollection}
	 * @return the outgoing {@link StreamCharacteristicCollection}
	 */
	public StreamCharacteristicCollection mergeStreamMetadata(
			List<StreamCharacteristicCollection> incomingStreamMetadata);
}
