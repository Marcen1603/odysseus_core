package de.uniol.inf.is.odysseus.new_transformation.costmodel.base;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;

/**
 * Every implementation of this Interface should be able to calculate the cost
 * of n {@link TempTransformationOperator} and merge the incoming stream
 * characteristics of an operator.
 * 
 */
public interface ICostModel {
	public StreamCharacteristicCollection mergeStreamMetadata(TempTransformationOperator transformator,
			ILogicalOperator logicalOperator, List<StreamCharacteristicCollection> incomingStreamMetadata)
			throws TransformationException;

	public ICost calculateCost(TempTransformationOperator... operators);
}
