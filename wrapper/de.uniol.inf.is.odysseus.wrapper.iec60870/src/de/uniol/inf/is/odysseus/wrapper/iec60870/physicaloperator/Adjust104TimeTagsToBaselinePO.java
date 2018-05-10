package de.uniol.inf.is.odysseus.wrapper.iec60870.physicaloperator;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.ei.oj104.model.SequenceInformationObject;
import de.uniol.inf.ei.oj104.model.SingleInformationObject;
import de.uniol.inf.ei.oj104.model.timetag.SevenOctetBinaryTime;
import de.uniol.inf.ei.oj104.model.timetag.ThreeOctetBinaryTime;
import de.uniol.inf.ei.oj104.model.timetag.TwoOctetBinaryTime;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

// TODO javaDoc
public class Adjust104TimeTagsToBaselinePO extends AbstractPipe<Tuple<IMetaAttribute>, Tuple<IMetaAttribute>> {
	
	private static final Logger logger = LoggerFactory.getLogger(Adjust104TimeTagsToBaselinePO.class);

	private final int iosAttributePos;

	// initialized with the baseline argument
	private long previousBaselinedTS;

	private long previousOriginalTS = -1;
	
	private long newPreviousBaselinedTS = -1;

	private long newPreviousOriginalTS = -1;

	public Adjust104TimeTagsToBaselinePO(int iosAttributePos, long baseline) {
		this.iosAttributePos = iosAttributePos;
		this.previousBaselinedTS = baseline;
	}

	public Adjust104TimeTagsToBaselinePO(Adjust104TimeTagsToBaselinePO other) {
		iosAttributePos = other.iosAttributePos;
		previousBaselinedTS = other.previousBaselinedTS;
		previousOriginalTS = other.previousOriginalTS;
		newPreviousBaselinedTS = other.newPreviousBaselinedTS;
		newPreviousOriginalTS = other.newPreviousOriginalTS;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(Tuple<IMetaAttribute> object, int port) {
		if (previousOriginalTS == -1) {
			// first tuple; check attribute
			validateTuple(object);
		}

		@SuppressWarnings("unchecked")
		List<IInformationObject> ios = (List<IInformationObject>) object.getAttribute(iosAttributePos);		
		newPreviousOriginalTS = -1;
		newPreviousBaselinedTS = -1;
		ios.forEach(io -> adjustTimeTags(io));

		// set previousOriginalTS and previousBaselinedTS
		long timebetweenBaselines = previousBaselinedTS == -1 ? 0 : newPreviousBaselinedTS - previousBaselinedTS;
		previousOriginalTS = newPreviousOriginalTS == -1 ? previousOriginalTS : newPreviousOriginalTS;
		previousBaselinedTS = newPreviousBaselinedTS == -1 ? previousBaselinedTS : newPreviousBaselinedTS;

		// sleep to simulate the timeshift between the messages
		try {
			Thread.sleep(timebetweenBaselines);
		} catch (InterruptedException e) {
			logger.error("Error while sleeping to simulate the timeshift between the messages", e);
		}

		transfer(object);
	}

	private void validateTuple(Tuple<IMetaAttribute> object) {
		// list is validated in transformation rule
		Object attribute = object.getAttribute(iosAttributePos);
		if (!(attribute instanceof List)
				|| ((List<?>) attribute).stream().anyMatch(entry -> !(entry instanceof IInformationObject))) {
			throw new IllegalArgumentException(attribute + " is not a list of information objects!");
		}
	}

	private void adjustTimeTags(IInformationObject io) {
		if (io instanceof SingleInformationObject) {
			adjustTimeTags((InformationElementSequence) io);
		} else if (io instanceof SequenceInformationObject) {
			((SequenceInformationObject) io).getInformationElementSequences()
					.forEach(ies -> adjustTimeTags(ies));
		}
	}

	private void adjustTimeTags(InformationElementSequence io) {
		if (!io.getTimeTag().isPresent()) {
			return;
		}

		// current original timestamp
		long originalTS = io.getTimeTag().get().getTimestamp();

		// difference to original timestamp of the first information element sequence of
		// the last tuple
		long diffToPreviousBaselinedTS = previousOriginalTS == -1 ? 0 : Math.abs(originalTS - previousOriginalTS);

		// new baselined timestamp is the baselined timestamp of the previous tuple +
		// the calculated difference
		long baselinedTS = previousBaselinedTS + diffToPreviousBaselinedTS;

		adjustTimeTags(io, baselinedTS);

		// set newPreviousOriginalTS and newPreviousBaselinedTS
		newPreviousOriginalTS = newPreviousOriginalTS == -1 ? originalTS : newPreviousOriginalTS;
		newPreviousBaselinedTS = newPreviousBaselinedTS == -1 ? baselinedTS : newPreviousBaselinedTS;
	}

	private void adjustTimeTags(InformationElementSequence io, long newTS) {
		ITimeTag originalTimeTag = io.getTimeTag().get();
		ITimeTag baselinedTimeTag = null;

		if (originalTimeTag instanceof SevenOctetBinaryTime) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(newTS);
			baselinedTimeTag = new SevenOctetBinaryTime();
			((SevenOctetBinaryTime) baselinedTimeTag).fromCalendar(calendar);
			((SevenOctetBinaryTime) baselinedTimeTag)
					.setSubstituted(((SevenOctetBinaryTime) originalTimeTag).isSubstituted());
			((SevenOctetBinaryTime) baselinedTimeTag).setInvalid(((SevenOctetBinaryTime) originalTimeTag).isInvalid());
		} else if (originalTimeTag instanceof ThreeOctetBinaryTime) {
			int milliseconds = (int) (newTS % 1000);
			int seconds = (int) (newTS / 1000) % 60;
			int minutes = (int) (newTS / 60000);
			baselinedTimeTag = new ThreeOctetBinaryTime(milliseconds, seconds, minutes,
					((ThreeOctetBinaryTime) originalTimeTag).isSubstituted(),
					((ThreeOctetBinaryTime) originalTimeTag).isInvalid());
		} else if (originalTimeTag instanceof TwoOctetBinaryTime) {
			int milliseconds = (int) (newTS % 1000);
			int seconds = (int) (newTS / 1000);
			baselinedTimeTag = new TwoOctetBinaryTime(milliseconds, seconds);
		}

		io.setTimeTag(Optional.ofNullable(baselinedTimeTag));
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}