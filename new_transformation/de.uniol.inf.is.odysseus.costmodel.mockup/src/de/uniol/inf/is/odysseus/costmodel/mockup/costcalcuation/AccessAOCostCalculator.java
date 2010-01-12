package de.uniol.inf.is.odysseus.costmodel.mockup.costcalcuation;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.base.ICost;
import de.uniol.inf.is.odysseus.costmodel.base.ICostCalculator;
import de.uniol.inf.is.odysseus.costmodel.mockup.MockupCost;
import de.uniol.inf.is.odysseus.costmodel.mockup.streamCharacteristic.TimeDistance;
import de.uniol.inf.is.odysseus.costmodel.mockup.streamCharacteristic.TimeIntervalLength;
import de.uniol.inf.is.odysseus.costmodel.streamCharacteristic.StreamCharacteristicCollection;

public class AccessAOCostCalculator implements ICostCalculator {
	@Override
	public ICost calculateCost(List<StreamCharacteristicCollection> incomingStreamCharacteristics) {
		return new MockupCost(0);
	}

	@Override
	public StreamCharacteristicCollection mergeStreamMetadata(
			List<StreamCharacteristicCollection> incomingStreamMetadata) {
		StreamCharacteristicCollection collection = new StreamCharacteristicCollection();
		collection.putMetadata(new TimeDistance(10));
		collection.putMetadata(new TimeIntervalLength(10));
		return collection;
	}
}
