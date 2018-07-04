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

/**
 * Physical operator as a black box solution to do the following with 104
 * messages (see OJ104 library). <br />
 * <br />
 * It changes all {@link ITimeTag}s in a list of {@link IInformationObject}.
 * This list must be a attribute of the input tuples. For the first time tag in
 * the first tuple, it sets the time tag to a given time stamp. For all other
 * time tags in the same list of information objects or in subsequent tuples, it
 * sets the time tag so that the original difference between the time tags is
 * preserved. <br />
 * <br />
 * Example: <br />
 * First and only original time tag (in ms) in the first tuple is 0. First and
 * only original time tag (in ms) in the second tuple is 2. The given time stamp
 * (baseline) is 1000. Then, the new time tag in the first tuple is 1000 and the
 * new time tag in the second tuple is 1002. <br />
 * <br />
 * Further it can delay the transmission of the results according to the
 * difference in the first time tags of the current tuple and the previous one.
 * With this behavior, the original delay of the messages can be preserved.
 * Another option is to accelerate the messages by using an acceleration factor.
 * It influences the new time tags as well as the delay. <br />
 * <br />
 * Example with acceleration: <br />
 * First and only original time tag (in ms) in the first tuple is 0. First and
 * only original time tag (in ms) in the second tuple is 2. The given time stamp
 * (baseline) is 1000 and the acceleration factor is 2.0. Then, the new time tag
 * in the first tuple is 1000 and the new time tag in the second tuple is 1001.
 * 
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class Adjust104TimeTagsToBaselinePO extends AbstractPipe<Tuple<IMetaAttribute>, Tuple<IMetaAttribute>> {

	private static final Logger logger = LoggerFactory.getLogger(Adjust104TimeTagsToBaselinePO.class);

	private final int iosAttributePos;

	private final double acceleration;

	private final boolean delay;

	// initialized with the baseline argument
	private long previousBaselinedTS;

	private long previousOriginalTS = -1;

	private long newPreviousBaselinedTS = -1;

	private long newPreviousOriginalTS = -1;

	public Adjust104TimeTagsToBaselinePO(int iosAttributePos, double acceleration, boolean delay, long baseline) {
		this.iosAttributePos = iosAttributePos;
		this.acceleration = acceleration;
		this.delay = delay;
		this.previousBaselinedTS = baseline;
	}

	public Adjust104TimeTagsToBaselinePO(Adjust104TimeTagsToBaselinePO other) {
		iosAttributePos = other.iosAttributePos;
		acceleration = other.acceleration;
		delay = other.delay;
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
		if (delay) {
			try {
				if(timebetweenBaselines < 0) {
					logger.debug("Telegram out of order respecting smallest time tags! '{}'", object);
				}
				Thread.sleep(Math.abs(timebetweenBaselines));
			} catch (InterruptedException e) {
				logger.error("Error while sleeping to simulate the timeshift between the messages", e);
			}
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
			((SequenceInformationObject) io).getInformationElementSequences().forEach(ies -> adjustTimeTags(ies));
		}
	}

	private void adjustTimeTags(InformationElementSequence io) {
		if (!io.getTimeTag().isPresent()) {
			return;
		}

		// current original timestamp
		long originalTS = io.getTimeTag().get().getTimestamp();

		// difference to minimal original timestamp of the last tuple
		long diffToPreviousBaselinedTS = previousOriginalTS == -1 ? 0
				: (long) ((originalTS - previousOriginalTS) / acceleration);

		// new baselined timestamp is the baselined timestamp of the previous tuple +
		// the calculated difference
		long baselinedTS = previousBaselinedTS + diffToPreviousBaselinedTS;

		adjustTimeTags(io, baselinedTS);

		// set newPreviousOriginalTS and newPreviousBaselinedTS
		newPreviousOriginalTS = newPreviousOriginalTS == -1 ? originalTS : Math.min(originalTS, newPreviousOriginalTS);
		newPreviousBaselinedTS = newPreviousBaselinedTS == -1 ? baselinedTS : Math.min(baselinedTS, newPreviousBaselinedTS);
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