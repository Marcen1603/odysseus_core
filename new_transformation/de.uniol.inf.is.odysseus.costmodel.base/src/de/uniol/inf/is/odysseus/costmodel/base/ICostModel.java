package de.uniol.inf.is.odysseus.costmodel.base;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.costmodel.streamCharacteristic.StreamCharacteristicCollection;

public interface ICostModel {
	public ICost calculateOperatorCost(IPOTransformator<? extends ILogicalOperator> transformator,
			List<StreamCharacteristicCollection> incomingStreamMetadata) throws TransformationException;

	public StreamCharacteristicCollection mergeStreamMetadata(
			IPOTransformator<? extends ILogicalOperator> transformator,
			List<StreamCharacteristicCollection> incomingStreamMetadata) throws TransformationException;
}
