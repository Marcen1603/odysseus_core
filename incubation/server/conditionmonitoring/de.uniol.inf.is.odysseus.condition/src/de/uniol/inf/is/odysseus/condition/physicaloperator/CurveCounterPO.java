package de.uniol.inf.is.odysseus.condition.physicaloperator;

import de.uniol.inf.is.odysseus.condition.logicaloperator.CurveCounterAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings("rawtypes")
public class CurveCounterPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private int curveTupleCounter;
	private boolean isStarted;

	private String stateAttributeName;
	private String startMessage;

	public CurveCounterPO(CurveCounterAO ao) {
		curveTupleCounter = 0;
		stateAttributeName = ao.getStateAttributeName();
		startMessage = ao.getStartMessage();
	}

	@Override
	protected void process_next(T tuple, int port) {

		if (port == 0 && isStarted) {
			// Append the counter and transfer the tuple
			Tuple newTuple = tuple.append(curveTupleCounter);
			transfer(newTuple);
			curveTupleCounter++;
		} else if (port == 1) {
			// Curve start and end messages

			// Reset the counter
			curveTupleCounter = 0;

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
