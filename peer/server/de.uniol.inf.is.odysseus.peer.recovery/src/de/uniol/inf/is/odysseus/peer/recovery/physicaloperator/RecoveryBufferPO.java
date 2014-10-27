package de.uniol.inf.is.odysseus.peer.recovery.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.recovery.tuplestore.ArrayDequeRecoveryTupleStore;
import de.uniol.inf.is.odysseus.peer.recovery.tuplestore.IRecoveryTupleStore;

public class RecoveryBufferPO<T extends IStreamObject<? extends ITimeInterval>> extends
		AbstractPipe<T, T> {

	private static final Logger LOG = LoggerFactory.getLogger(RecoveryBufferPO.class);

	private enum BUFFER_STATES {
		NOT_BUFFERING, BUFFERING, CLEARING_BUFFER
	}

	private BUFFER_STATES bufferState = BUFFER_STATES.NOT_BUFFERING;

	IRecoveryTupleStore tupleStore;

	public RecoveryBufferPO() {
		super();
		tupleStore = new ArrayDequeRecoveryTupleStore();
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		if (bufferState == BUFFER_STATES.BUFFERING || bufferState == BUFFER_STATES.CLEARING_BUFFER) {
			tupleStore.saveTuple(object);
		} else {
			transfer(object, port);
		}
	}

	public void startBuffering() {
		LOG.debug("Start Buffering.");
		this.bufferState = BUFFER_STATES.BUFFERING;
	}

	/**
	 * Stops the buffering and starts to send the saved tuples to the next operator(s)
	 */
	@SuppressWarnings("unchecked")
	public void stopBuffering() {
		if (bufferState == BUFFER_STATES.BUFFERING) {
			this.bufferState = BUFFER_STATES.CLEARING_BUFFER;

			T tupleToProcess = (T) tupleStore.getNextTuple();
			while (tupleToProcess != null) {
				super.process(tupleToProcess, 0);
				tupleToProcess = (T) tupleStore.getNextTuple();
			}
			this.bufferState = BUFFER_STATES.NOT_BUFFERING;
		}
	}

	@Override
	public String getName() {
		return "RecoveryBufferPO";
	}

}
