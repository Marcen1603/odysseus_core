package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

/**
 * A {@link AbstractFragmentPO} can be used to realize a {@link AbstractFragmentAO}.
 * @author Michael Brand
 */
public abstract class AbstractFragmentPO<T extends IStreamObject<IMetaAttribute>> 
		extends AbstractPipe<T, T> {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractFragmentPO.class);
	
	/**
	 * The number of fragments.
	 */
	protected long numFragments;

	/**
	 * Constructs a new {@link AbstractFragmentPO}.
	 * @param fragmentAO the {@link AbstractFragmentAO} transformed to this {@link AbstractFragmentPO}.
	 */
	public AbstractFragmentPO(AbstractFragmentAO fragmentAO) {
		
		super();
		this.numFragments = fragmentAO.getNumberOfFragments();
		
	}

	/**
	 * Constructs a new {@link AbstractFragmentPO} as a copy of an existing one.
	 * @param fragmentPO The {@link AbstractFragmentPO} to be copied.
	 */
	public AbstractFragmentPO(AbstractFragmentPO<T> fragmentPO) {
		
		super(fragmentPO);
		this.numFragments = fragmentPO.numFragments;
		
	}
	
	@Override
	public abstract AbstractPipe<T, T> clone() ;

	@Override
	public OutputMode getOutputMode() {
		
		return OutputMode.INPUT;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void process_next(T object, int port) {
		// TODO: DO NOT SYNCHRONIZE ON THIS!
		int outPort = this.route(object);
		AbstractFragmentPO.log.debug("Routed " + object + " to output port " + outPort);
		this.transfer(object, outPort);
		
		// Sending heartbeats to all other ports
		Heartbeat heartbeat = Heartbeat.createNewHeartbeat(((IStreamObject<? extends ITimeInterval>) object).getMetadata().getStart());
		for(int p = 0; p < this.numFragments; p++) {
			if(p != outPort) {
				this.sendPunctuation(heartbeat, p);
			}
		}

	}
	
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		
		int outPort = this.route(punctuation);
		AbstractFragmentPO.log.debug("Routed " + punctuation + " to output port " + outPort);
		this.sendPunctuation(punctuation, outPort);
		
		// Sending heartbeats to all other ports
		for(int p = 0; p < this.numFragments; p++) {
			
			if(p != outPort)
				this.sendPunctuation(Heartbeat.createNewHeartbeat(punctuation.getTime()), p);
			
		}
		
	}
	
	/**
	 * Routes an incoming object to the next output port.
	 * @param object The incoming {@link IStreamable} object.
	 * @return The output port to which <code>object</code> shall be transfered.
	 */
	protected abstract int route(IStreamable object);
	
}