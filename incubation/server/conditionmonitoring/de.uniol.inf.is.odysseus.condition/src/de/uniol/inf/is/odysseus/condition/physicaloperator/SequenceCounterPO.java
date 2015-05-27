package de.uniol.inf.is.odysseus.condition.physicaloperator;

import de.uniol.inf.is.odysseus.condition.logicaloperator.SequenceCounterAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator gives every tuple of a sequence a number. Therefore it needs
 * messages that indicate, when a sequence starts and ends. The tuples must be
 * on port 0, the start and end messages for the sequences on port 1.
 * 
 * @author Tobias Brandt
 */
@SuppressWarnings("rawtypes")
public class SequenceCounterPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private int sequenceTupleCounter;
	private boolean isStarted;

	private String stateAttributeName;
	private String startMessage;

	public SequenceCounterPO(SequenceCounterAO ao) {
		sequenceTupleCounter = 0;
		stateAttributeName = ao.getStateAttributeName();
		startMessage = ao.getStartMessage();
	}

	@Override
	protected void process_next(T tuple, int port) {

		if (port == 0 && isStarted) {
			// Append the counter and transfer the tuple
			Tuple newTuple = tuple.append(sequenceTupleCounter);
			transfer(newTuple);
			sequenceTupleCounter++;
		} else if (port == 1) {
			// Sequence start and end messages

			// Reset the counter
			sequenceTupleCounter = 0;

			// Save the state
			String message = getStartAttribute(tuple);
			if (message.equals(startMessage)) {
				isStarted = true;
			} else {
				isStarted = false;
			}
		}

	}

	private String getStartAttribute(T tuple) {
		int valueIndex = getInputSchema(1).findAttributeIndex(stateAttributeName);
		if (valueIndex >= 0) {
			String value = "" + tuple.getAttribute(valueIndex);
			return value;
		}
		return "";
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
}
