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

	private boolean copyTimeInterval;
	private TimeValueItem valueToAddStart;
	private TimeValueItem valueToAddEnd;
	private boolean alingAtStreamEnd;
	private TimeUnit streamBaseTimeUnit;

	public ChangeValidTimePO(ChangeValidTimeAO ao) {
		this.valueToAddStart = ao.getValueToAddStart();
		this.valueToAddEnd = ao.getValueToAddEnd();
		this.alingAtStreamEnd = ao.isAlignAtEnd();
		this.streamBaseTimeUnit = ao.getBaseTimeUnit();
		this.copyTimeInterval = ao.isCopyTimeInterval();
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
		if (copyTimeInterval) {
			object = copyTimeInterval(object);
		} else {
			object = setTimeInterval(object, valueToAddStart, valueToAddEnd, alingAtStreamEnd);
		}
		transfer(object);
	}
	
	private T copyTimeInterval(T object) {
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ITimeInterval) {
			// Use the stream time as the starting point for the calculations
			ITimeInterval streamTime = (ITimeInterval) metadata;
			PointInTime newValidStart = streamTime.getStart();
			PointInTime newValidEnd = streamTime.getEnd();
			changeValidTime(metadata, newValidStart, newValidEnd);
		}
		return object;
	}
	
	private T setTimeInterval(T object, TimeValueItem addToStart, TimeValueItem addToEnd, boolean alignAtStreamEnd) {
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ITimeInterval) {
			// Use the stream time as the starting point for the calculations
			ITimeInterval streamTime = (ITimeInterval) metadata;

			long convertedValueToAddStart = streamBaseTimeUnit.convert(addToStart.getTime(),
					addToStart.getUnit());
			long convertedValueToAddEnd = streamBaseTimeUnit.convert(addToEnd.getTime(), addToEnd.getUnit());

			PointInTime baseTime;
			if (!alignAtStreamEnd) {
				baseTime = streamTime.getStart();
			} else {
				baseTime = streamTime.getEnd();
			}
			PointInTime newValidStart = baseTime.plus(convertedValueToAddStart);
			PointInTime newValidEnd = baseTime.plus(convertedValueToAddEnd);				
			changeValidTime(metadata, newValidStart, newValidEnd);
		}
		return object;
	}
	
	private IMetaAttribute changeValidTime(IMetaAttribute metadata, PointInTime newValidStart, PointInTime newValidEnd) {
		if (metadata instanceof IValidTimes) {
			// Use the calculated start and end timestamps for the ValidTime
			IValidTimes validTime = (IValidTimes) metadata;
			validTime.clear();
			IValidTime newTime = new ValidTime();
			newTime.setValidStartAndEnd(newValidStart, newValidEnd);
			validTime.addValidTime(newTime);
		}
		return metadata;
	}
}
