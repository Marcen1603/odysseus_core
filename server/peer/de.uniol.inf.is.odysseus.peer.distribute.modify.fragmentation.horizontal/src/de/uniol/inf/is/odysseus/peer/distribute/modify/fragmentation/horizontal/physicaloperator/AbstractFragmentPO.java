package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

/**
 * A {@link AbstractFragmentPO} can be used to realize a
 * {@link AbstractFragmentAO}.
 * 
 * @author Michael Brand
 */
public abstract class AbstractFragmentPO<T extends IStreamObject<IMetaAttribute>> extends AbstractPipe<T, T> {

	private static final Logger log = LoggerFactory.getLogger(AbstractFragmentPO.class);
	
	/**
	 * The rate heartbeats are send.
	 */
	private final int heartbeatRate = 10;
	
	/**
	 * The current amount of heartbeats.
	 */
	private int[] heartbeatCounter;

	/**
	 * The number of fragments.
	 */
	protected int numFragments;

	/**
	 * Constructs a new {@link AbstractFragmentPO}.
	 * 
	 * @param fragmentAO
	 *            the {@link AbstractFragmentAO} transformed to this
	 *            {@link AbstractFragmentPO}.
	 */
	public AbstractFragmentPO(AbstractFragmentAO fragmentAO) {

		super();
		this.numFragments = (int) fragmentAO.getNumberOfFragments();
		this.heartbeatCounter = new int[this.numFragments];

	}

	/**
	 * Constructs a new {@link AbstractFragmentPO} as a copy of an existing one.
	 * 
	 * @param fragmentPO
	 *            The {@link AbstractFragmentPO} to be copied.
	 */
	public AbstractFragmentPO(AbstractFragmentPO<T> fragmentPO) {

		super(fragmentPO);
		this.numFragments = fragmentPO.numFragments;

	}

	@Override
	public abstract AbstractPipe<T, T> clone();

	@Override
	public OutputMode getOutputMode() {

		return OutputMode.INPUT;

	}

	@Override
	@SuppressWarnings("unchecked")
	protected synchronized void process_next(T object, int port) {
		// DO NOT SYNCHRONIZE ON THIS!
		int outPort = this.route(object);
		AbstractFragmentPO.log.debug("Routed " + object + " to output port " + outPort);
		this.transfer(object, outPort);

		sendHeartbeats(outPort, ((IStreamObject<? extends ITimeInterval>) object).getMetadata().getStart());
	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {

		int outPort = this.route(punctuation);
		AbstractFragmentPO.log.debug("Routed " + punctuation + " to output port " + outPort);
		this.sendPunctuation(punctuation, outPort);

		sendHeartbeats(outPort, punctuation.getTime());
		
	}

	/**
	 * Send heartbeats in order to communicate the temporal advance to all ports.
	 * @param exceptionPort The port, where the current element is routed. There will no heartbeat send.
	 * @param currentPoT The {@link PointInTime} of the current element.
	 */
	private void sendHeartbeats(int exceptionPort, PointInTime currentPoT) {
		
		final Heartbeat heartbeat = Heartbeat.createNewHeartbeat(currentPoT);
		
		for(int p = 0; p < this.numFragments; p++) {
			
			if(p != exceptionPort && ++this.heartbeatCounter[p] % this.heartbeatRate != 0)
				this.sendPunctuation(heartbeat, p);
			
		}
	}

	/**
	 * Routes an incoming object to the next output port.
	 * 
	 * @param object
	 *            The incoming {@link IStreamable} object.
	 * @return The output port to which <code>object</code> shall be transfered.
	 */
	protected abstract int route(IStreamable object);

}