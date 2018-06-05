package de.uniol.inf.is.odysseus.relational_interval.replacement;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class RelationalReplacementPO
		extends
		AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {

	private Tuple<? extends ITimeInterval> lastObject;
	private PointInTime lastTransfer;

	final private long timeIntervalSize;
	final private int timestampAttributePos;
	final private int valueAttributePos;
	final private int qualityAttributePos;
	final private IReplacement<Tuple<? extends ITimeInterval>> replacementMethod;

	public RelationalReplacementPO(long timeIntervalSize,
			int timestampAttributePos, int valueAttributePos,
			int qualityAttributePos,IReplacement<Tuple<? extends ITimeInterval>> replacementMethod) {
		this.timeIntervalSize = timeIntervalSize;
		this.timestampAttributePos = timestampAttributePos;
		this.valueAttributePos = valueAttributePos;
		this.qualityAttributePos = qualityAttributePos;
		this.replacementMethod = replacementMethod;
	}

	public RelationalReplacementPO(RelationalReplacementPO replacementPO) {
		super(replacementPO);
		this.timeIntervalSize = replacementPO.timeIntervalSize;
		this.timestampAttributePos = replacementPO.timestampAttributePos;
		this.valueAttributePos = replacementPO.valueAttributePos;
		this.qualityAttributePos = replacementPO.qualityAttributePos;
		this.replacementMethod = replacementPO.replacementMethod;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		// Test for missing elements
		if (lastTransfer != null) {
			long difference = object.getMetadata().getStart()
					.minus(lastTransfer).getMainPoint();

			long noMissingValues = difference / timeIntervalSize;
			if (noMissingValues > 1) {
				List<Object> replacementValues = null;
				if (valueAttributePos >= 0) {
					replacementValues = replacementMethod.determineReplacements(lastObject,
							object, (int) noMissingValues, valueAttributePos);
				}

				for (int i = 1; i < noMissingValues; i++) {
					// Test with simple resend
					@SuppressWarnings("rawtypes")
					Tuple newTuple = lastObject.clone();
					ITimeInterval metadata = (ITimeInterval) lastObject
							.getMetadata().clone();
					long newStart = lastTransfer.getMainPoint()
							+ (i * timeIntervalSize);
					metadata.setStartAndEnd(new PointInTime(newStart),
							PointInTime.getInfinityTime());
					newTuple.setMetadata(metadata);
					if (timestampAttributePos >= 0) {
						newTuple.setAttribute(timestampAttributePos, newStart);
					}
					// TODO: What should this value be, configurable?
					if (qualityAttributePos >= 0) {
						newTuple.setAttribute(qualityAttributePos, "derived");
					}
					if (replacementValues != null) {
						if (valueAttributePos >= 0) {
							newTuple.setAttribute(valueAttributePos, replacementValues.get(i - 1));
						}
					}
					transfer(newTuple);
				}
			}
		}

		lastObject = object;
		lastTransfer = object.getMetadata().getStart();
		transfer(object);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
