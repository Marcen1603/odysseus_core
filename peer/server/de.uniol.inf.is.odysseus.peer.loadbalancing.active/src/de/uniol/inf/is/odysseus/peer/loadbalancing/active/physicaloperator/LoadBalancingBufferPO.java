package de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.buffer.ArrayDequeLoadBalancingBuffer;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.movingstate.buffer.ILoadBalancingBuffer;

/**
 * Operator to store Tuples when States are copied in moving state Strategy.
 * Mostly copied from {@link RecoveryTupleStorePO}
 * @author Carsten Cordes
 *
 * @param <T>
 */
@Deprecated
public class LoadBalancingBufferPO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(LoadBalancingBufferPO.class);
	
	private enum BUFFER_STATES {
		NOT_BUFFERING,BUFFERING,CLEARING_BUFFER
	}
	
	private BUFFER_STATES bufferState = BUFFER_STATES.NOT_BUFFERING;
	
	ILoadBalancingBuffer tupleStore; 
	
	public LoadBalancingBufferPO() {
		super();
		tupleStore = new ArrayDequeLoadBalancingBuffer();
	}
	
	@Override
	public String getName() {
		return "Buffer";
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		if(bufferState==BUFFER_STATES.BUFFERING || bufferState==BUFFER_STATES.CLEARING_BUFFER) {
			tupleStore.saveTuple(object);
		}
		else {
			transfer(object,port);
		}
	}
	
	public void startBuffering() {
		LOG.debug("Start Buffering.");
		this.bufferState = BUFFER_STATES.BUFFERING;
	}
	

	@SuppressWarnings("unchecked")
	public void stopBuffering() {
		if(bufferState==BUFFER_STATES.BUFFERING) {
			this.bufferState=BUFFER_STATES.CLEARING_BUFFER;
			
			T tupleToProcess = (T) tupleStore.getNextTuple();
			while(tupleToProcess!=null) {
				transfer(tupleToProcess, 0);
				tupleToProcess = (T) tupleStore.getNextTuple();
			}
			this.bufferState=BUFFER_STATES.NOT_BUFFERING;
		}
	}

}
