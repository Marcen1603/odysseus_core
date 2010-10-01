package de.uniol.inf.is.odysseus.new_transformation.costmodel.base;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;

public interface ICostCalculator {
	public ICost calculateCost(TempTransformationOperator operator);

	/**
	 * Merges the {@link StreamCharacteristicCollection}s of the incoming
	 * streams into a new one which represents the metadata of the outgoing
	 * stream
	 * 
	 * @param incomingStreamMetadata
	 *            - the {@link List} of the incoming
	 *            {@link StreamCharacteristicCollection}
	 * @param logicalOperator
	 *            - the transformed operator
	 * @return the outgoing {@link StreamCharacteristicCollection}
	 */
	public StreamCharacteristicCollection mergeStreamMetadata(
			List<StreamCharacteristicCollection> incomingStreamMetadata, ILogicalOperator logicalOperator);
}
