package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.logicaloperator.SequenceCounterAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator gives every tuple of a sequence a number. Therefore it needs
 * messages that indicate, when a sequence starts and ends. The tuples must be
 * on port 0, the start and end messages for the sequences on port 1. To get a
 * live sequence, punctuations are needed
 * 
 * @author Tobias Brandt
 */
@SuppressWarnings("rawtypes")
public class SequenceCounterPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	public static final int DATA_PORT = 0;
	public static final int SEQUENCE_PORT = 1;

	private int sequenceTupleCounter;

	private String stateAttributeName;
	private String startMessage;

	private List<TimeInterval> times;
	private PointInTime lastStart;
	private PointInTime lastPunctuation;
	private List<T> tuples;

	public SequenceCounterPO(SequenceCounterAO ao) {
		this.times = new ArrayList<TimeInterval>();
		this.tuples = new ArrayList<T>();
		this.sequenceTupleCounter = 0;
		this.stateAttributeName = ao.getStateAttributeName();
		this.startMessage = ao.getStartMessage();
	}

	@Override
	protected void process_next(T tuple, int port) {

		if (port == DATA_PORT && isInSequence(tuple)) {
			sendToNextOperator(tuple);
		} else if (port == SEQUENCE_PORT) {
			// Sequence start and end messages

			// Save the state
			String message = getStateAttribute(tuple);
			if (message.equals(startMessage)) {
				if (lastStart == null) {
					lastStart = tuple.getMetadata().getStart();
				}
				// Maybe we have some tuples in the storage that are now in
				// a sequence
				workThroughTupleList(this.tuples, lastStart, lastStart);
			} else {
				if (lastStart != null) {
					TimeInterval interval = new TimeInterval(lastStart, tuple.getMetadata().getStart());
					times.add(interval);
					PointInTime prevLastStart = lastStart;
					lastStart = null;
					workThroughTupleList(this.tuples, prevLastStart, tuple.getMetadata().getStart());
				}
			}
		}
	}

	/**
	 * Transfers the tuple to the next operator on port 0 with the calculated
	 * sequence number
	 * 
	 * @param tuple
	 *            The tuple to transfer (without sequence number)
	 */
	private void sendToNextOperator(T tuple) {
		// Append the counter and transfer the tuple
		Tuple newTuple = tuple.append(sequenceTupleCounter);
		transfer(newTuple);
		sequenceTupleCounter++;
	}

	/**
	 * Goes through the list of saved tuples and sends them to the next operator
	 * if they are in the latest sequence. Deletes them if they were send
	 * further or if they are sure not to be in a sequence.
	 * 
	 * @param tupleList
	 *            The list of saved tuples
	 * @param lastStart
	 *            The start-timeStamp of the latest sequence
	 * @param until
	 *            The timeStamp from which we know that the current sequence
	 *            goes at least until it
	 */
	private void workThroughTupleList(List<T> tupleList, PointInTime lastStart, PointInTime until) {
		Iterator<T> iter = tupleList.iterator();
		while (iter.hasNext()) {
			T next = iter.next();
			if (next.getMetadata().getStart().afterOrEquals(lastStart)
					&& next.getMetadata().getStart().beforeOrEquals(until)) {
				// If we got here, the tuple is within the next sequence,
				// because it is after the last start but before the last
				// timeStamp from the heartbeat: therefore send it to the next
				// operator
				sendToNextOperator(next);

				// We sent it to the next operator, we don't need this tuple any
				// longer
				iter.remove();
				continue;
			}

			// If the tuple was before the last start, is was in an area without
			// a sequence. Therefore, we don't need this tuple
			if (next.getMetadata().getStart().before(lastStart)) {
				iter.remove();
				// Reset the counter, because there was a tuple which is in NO
				// (not just not the current) sequence
				this.sequenceTupleCounter = 0;
			}
		}
	}

	/**
	 * Checks, if the tuple is in a sequence. Therefore it will be checked, if
	 * the tuple is in the current sequence or in one of the old sequences. If
	 * the tuple is after an old sequence, the old sequence will be removed from
	 * the list.
	 * 
	 * If the tuple is in a future we don't know about, it will be stored to
	 * check it later.
	 * 
	 * @param tuple
	 *            The new tuple which we have to check
	 * @return true, if the tuple is in a sequence we know and false, if it's
	 *         not
	 */
	private boolean isInSequence(T tuple) {
		PointInTime start = tuple.getMetadata().getStart();
		if (this.lastStart != null && start.afterOrEquals(this.lastStart) && this.lastPunctuation != null
				&& start.beforeOrEquals(this.lastPunctuation)) {
			// We can only look until the last punctuation we got, cause maybe
			// there will be a stop soon
			return true;
		}

		// Ok, it's not within the current sequence, but maybe the data stream
		// lags a bit and this tuple is within an old TimeInterval of tuples
		// which we have to send further

		Iterator<TimeInterval> iter = times.iterator();
		while (iter.hasNext()) {
			TimeInterval next = iter.next();

			// Check, if the tuple is within this (old) TimeInterval
			if (next.includes(start)) {
				return true;
			}

			// Check, if it is in one TimeInterval. If the startTimestamp of
			// this tuple is after the end of an interval in the list, we can
			// delete the interval from the list (cause there won't be any
			// tuples in this sequence in the future)
			if (start.after(next.getEnd())) {
				iter.remove();
			}
		}

		// The tuple was in no sequence we know so far. If it's after the last
		// punctuation we got, maybe it will be in a future sequence
		PointInTime newestSequenceEnd = this.times.size() > 0 ? this.times.get(this.times.size() - 1).getEnd() : null;
		PointInTime newestInfo = null;
		if (newestSequenceEnd != null && this.lastPunctuation != null) {
			newestInfo = newestSequenceEnd.after(this.lastPunctuation) ? newestSequenceEnd : this.lastPunctuation;
		} else if (newestSequenceEnd != null) {
			newestInfo = newestSequenceEnd;
		} else {
			newestInfo = this.lastPunctuation;
		}

		if (newestInfo == null || (newestInfo != null && start.afterOrEquals(newestInfo))) {
			// It's after the last info we got, therefore we have to save it
			// Or we don't know where in time we are. Then we have to save it,
			// too.
			tuples.add(tuple);
		} else {
			// We have a tuple that won't be in any sequence, therefore we have
			// to reset the counter
			this.sequenceTupleCounter = 0;
		}

		return false;
	}

	/**
	 * Searches for the state attribute in the given tuple and returns it. State
	 * attribute holds the message whether the sequence starts or stops
	 * 
	 * @param tuple The tuple in which we look for the attribute
	 * @return The state attribute or an empty string, if it's not found
	 */
	private String getStateAttribute(T tuple) {
		int valueIndex = getInputSchema(SEQUENCE_PORT).findAttributeIndex(stateAttributeName);
		if (valueIndex >= 0) {
			String value = "" + tuple.getAttribute(valueIndex);
			return value;
		}
		return "";
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (port == SEQUENCE_PORT) {
			// These punctuations are important, because they show us that there
			// was no "end-sequence"-message until now
			if (this.lastStart != null) {
				this.lastPunctuation = punctuation.getTime();
				workThroughTupleList(this.tuples, this.lastStart, this.lastPunctuation);
			}
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
}
