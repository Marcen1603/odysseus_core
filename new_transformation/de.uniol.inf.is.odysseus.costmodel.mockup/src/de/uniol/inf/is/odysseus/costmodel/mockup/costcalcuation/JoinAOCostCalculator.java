package de.uniol.inf.is.odysseus.costmodel.mockup.costcalcuation;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.base.ICost;
import de.uniol.inf.is.odysseus.costmodel.base.ICostCalculator;
import de.uniol.inf.is.odysseus.costmodel.mockup.MockupCost;
import de.uniol.inf.is.odysseus.costmodel.mockup.streamCharacteristic.TimeDistance;
import de.uniol.inf.is.odysseus.costmodel.mockup.streamCharacteristic.TimeIntervalLength;
import de.uniol.inf.is.odysseus.costmodel.streamCharacteristic.StreamCharacteristicCollection;

public class JoinAOCostCalculator implements ICostCalculator {
	@Override
	public ICost calculateCost(List<StreamCharacteristicCollection> incomingStreamCharacteristics) {
		return new MockupCost(0);
	}

	@Override
	public StreamCharacteristicCollection mergeStreamMetadata(
			List<StreamCharacteristicCollection> incomingStreamMetadata) {

		StreamCharacteristicCollection leftStream = incomingStreamMetadata.get(0);
		StreamCharacteristicCollection rightStream = incomingStreamMetadata.get(1);

		double d1 = leftStream.getMetadata(TimeDistance.class).getTimeDistance();
		double l1 = leftStream.getMetadata(TimeIntervalLength.class).getTimeIntervalLength();

		double d2 = rightStream.getMetadata(TimeDistance.class).getTimeDistance();
		double l2 = rightStream.getMetadata(TimeIntervalLength.class).getTimeIntervalLength();

		double d = (d1 * d2) / (l1 + l2);
		double l = (l1 * l2) / (l1 + l2);

		StreamCharacteristicCollection collection = new StreamCharacteristicCollection();
		collection.putMetadata(new TimeDistance(d));
		collection.putMetadata(new TimeIntervalLength(l));
		return collection;
	}
}
