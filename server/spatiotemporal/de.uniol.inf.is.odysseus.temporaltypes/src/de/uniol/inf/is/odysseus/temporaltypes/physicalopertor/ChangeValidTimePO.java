package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.temporaltypes.logicaloperator.ChangeValidTimeAO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTime;

/**
 * This operator manipulates the ValidTimes metadata. It is doing this based on
 * the stream time interval. It uses the start timestamp of the stream time
 * interval (the "normal" time interval) of a stream element and adds /
 * substracts the given values to / from it to calculate the start and end
 * timestamp of the ValidTime.
 * 
 * Removes all previous information in the ValidTimes metadata.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class ChangeValidTimePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private TimeValueItem valueToAddStart;
	private TimeValueItem valueToAddEnd;
	private TimeUnit streamBaseTimeUnit;

	public ChangeValidTimePO(ChangeValidTimeAO ao) {
		this.valueToAddStart = ao.getValueToAddStart();
		this.valueToAddEnd = ao.getValueToAddEnd();
		this.streamBaseTimeUnit = ao.getBaseTimeUnit();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Do nothing
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ITimeInterval) {
			// Use the stream time as the starting point for the calculations
			ITimeInterval streamTime = (ITimeInterval) metadata;

			long convertedValueToAddStart = streamBaseTimeUnit.convert(valueToAddStart.getTime(),
					valueToAddStart.getUnit());
			long convertedValueToAddEnd = streamBaseTimeUnit.convert(valueToAddEnd.getTime(), valueToAddEnd.getUnit());

			PointInTime newValidStart = streamTime.getStart().plus(convertedValueToAddStart);
			PointInTime newValidEnd = streamTime.getStart().plus(convertedValueToAddEnd);
			if (metadata instanceof IValidTimes) {
				// Use the calculated start and end timestamps for the ValidTime
				IValidTimes validTime = (IValidTimes) metadata;
				validTime.clear();
				IValidTime newTime = new ValidTime();
				newTime.setValidStartAndEnd(newValidStart, newValidEnd);
				validTime.addValidTime(newTime);
			}
		}
		transfer(object);
	}
}
