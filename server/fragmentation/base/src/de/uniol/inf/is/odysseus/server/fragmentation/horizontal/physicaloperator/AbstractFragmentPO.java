package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

/**
 * A {@link AbstractFragmentPO} can be used to realize a
 * {@link AbstractFragmentAO}.
 * 
 * @author Michael Brand
 * @author Marco Grawunder
 */
public abstract class AbstractFragmentPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> implements IPhysicalOperatorKeyValueProvider{

	private static final Logger log = LoggerFactory
			.getLogger(AbstractFragmentPO.class);

	/**
	 * The rate heartbeats are send.
	 */
	private final int heartbeatRate;

	/**
	 * The current amount of heartbeats.
	 */
	private int[] heartbeatCounter;

	/**
	 * The number of fragments.
	 */
	protected final int numFragments;
	
	/**
	 * How many elements are send to which port
	 */
	protected final long[] elementsSend;

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
		this.heartbeatRate = fragmentAO.getHeartbeatrate();
		this.elementsSend = new long[this.numFragments];
	}

	@Override
	public OutputMode getOutputMode() {

		return OutputMode.INPUT;

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(T object, int port) {
		int outPort = this.route(object);
		elementsSend[outPort]++;
		AbstractFragmentPO.log.trace("Routed " + object + " to output port "
				+ outPort);
		this.transfer(object, outPort);

		sendHeartbeats(outPort,
				((IStreamObject<? extends ITimeInterval>) object).getMetadata()
						.getStart());
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		// Send punctuations to all fragments!
		for (int outPort = 0; outPort < numFragments; outPort++) {
			AbstractFragmentPO.log.trace("Routed " + punctuation
					+ " to output port " + outPort);
			this.sendPunctuation(punctuation, outPort);
		}
	}

	/**
	 * Send heartbeats in order to communicate the temporal advance to all
	 * ports.
	 * 
	 * @param exceptionPort
	 *            The port, where the current element is routed. There will no
	 *            heartbeat send.
	 * @param currentPoT
	 *            The {@link PointInTime} of the current element.
	 */
	private void sendHeartbeats(int exceptionPort, PointInTime currentPoT) {

		final Heartbeat heartbeat = Heartbeat.createNewHeartbeat(currentPoT);

		for (int p = 0; p < this.numFragments; p++) {

			if (p != exceptionPort) {

				if (heartbeatCounter[p] < heartbeatRate) {
					heartbeatCounter[p]++;
				} else {
					this.sendPunctuation(heartbeat, p);
					heartbeatCounter[p] = 0;
				}
			}

		}
	}

	/**
	 * Routes an incoming object to the next output port.
	 * 
	 * @param object
	 *            The incoming {@link IStreamable} object.
	 * @return The output port to which <code>object</code> shall be transfered.
	 */
	protected abstract int route(IStreamObject<IMetaAttribute> object);

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Distribution", dumpArray(elementsSend));
		return map;
	}
	
	private String dumpArray(long[] a){
		StringBuffer b = new StringBuffer("[");
		String SEP="";
		for (int i=0;i<a.length;i++){
			b.append(SEP).append(a[i]);
			SEP=",";
		}
		b.append("]");
		return b.toString();
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AbstractFragmentPO)){
			return false;
		}
		
		@SuppressWarnings("unchecked")
		AbstractFragmentPO<T> other = (AbstractFragmentPO<T>) ipo;
		
		if (this.numFragments != other.numFragments){
			return false;
		}
		
		return super.isSemanticallyEqual(ipo);
	}
	
}