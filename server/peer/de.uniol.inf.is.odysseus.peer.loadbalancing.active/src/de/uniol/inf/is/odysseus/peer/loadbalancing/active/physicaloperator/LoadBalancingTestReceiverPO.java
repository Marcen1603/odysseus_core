package de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingPunctuation;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.LoadBalancingTestReceiverAO;

/**
 * A {@link LoadBalancingTestReceiverPO} listens for
 * {@link LoadBalancingPunctuation}s and can log them and filter them, so they
 * won't be sent to the next operator.
 * 
 * @author Tobias Brandt
 * 
 * @param <T>
 */
@Deprecated
public class LoadBalancingTestReceiverPO<T extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<T, T> {

	private static final Logger log = LoggerFactory
			.getLogger(LoadBalancingTestSenderPO.class);
	private String logMessage = "Received%s LoadBalancingPunctuation: ";

	private boolean filterLoadBalancingPuctuations;
	private boolean writeToLog;

	/**
	 * Standard-constructor which creates a {@link LoadBalancingTestReceiverPO}
	 * that filters the {@link LoadBalancingPunctuation}s and writes them to log
	 */
	public LoadBalancingTestReceiverPO() {
		super();
		filterLoadBalancingPuctuations = true;
		writeToLog = true;
	}

	/**
	 * Creates a {@link LoadBalancingTestReceiverPO} with the given
	 * configuration
	 * 
	 * @param filterLoadBalancingPunctuations
	 *            Set to true if incoming {@link LoadBalancingPunctuation}s
	 *            should be filtered so that they won't be send to the next
	 *            operator false, if you want to send the
	 *            {@link LoadBalancingPunctuation}s further
	 * @param writeToLog
	 *            Set to true if incoming {@link LoadBalancingPunctuation}s
	 *            should be logged false if not
	 */
	public LoadBalancingTestReceiverPO(boolean filterLoadBalancingPunctuations,
			boolean writeToLog) {
		super();
		this.filterLoadBalancingPuctuations = filterLoadBalancingPunctuations;
		this.writeToLog = writeToLog;
	}

	/**
	 * Creates a {@link LoadBalancingTestReceiverPO} from the given
	 * {@link LoadBalancingTestReceiverAO} and uses its configuration
	 * 
	 * @param other
	 *            {@link LoadBalancingTestReceiverAO} with the configuration
	 */
	public LoadBalancingTestReceiverPO(LoadBalancingTestReceiverAO other) {
		super();
		this.filterLoadBalancingPuctuations = other
				.getFilterLoadBalancingPuctuations();
		this.writeToLog = other.getWriteToLog();
	}

	/**
	 * Copies the given {@link LoadBalancingTestReceiverPO} and returns the
	 * clone with the same configuration
	 * 
	 * @param other
	 *            {@link LoadBalancingTestReceiverPO} to be copied
	 */
	public LoadBalancingTestReceiverPO(LoadBalancingTestReceiverPO<T> other) {
		super();
		this.filterLoadBalancingPuctuations = other.filterLoadBalancingPuctuations;
		this.writeToLog = other.writeToLog;
	}

	/**
	 * Clones this object and return it
	 */
	@Override
	public LoadBalancingTestReceiverPO<T> clone() {
		return new LoadBalancingTestReceiverPO<T>(this);
	}

	/**
	 * Just transfer any tuple to the next operator
	 */
	@Override
	protected void process_next(T object, int port) {
		this.transfer(object, port);
	}

	/**
	 * Filter and / or write the {@link LoadBalancingPunctuation}s to the log,
	 * depends on configuration
	 */
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation,
			int port) {
		if (punctuation instanceof LoadBalancingPunctuation) {
			if (writeToLog) {
				// Write to log
				String message = String.format(logMessage
						+ ((LoadBalancingPunctuation) punctuation).toString(),
						filterLoadBalancingPuctuations ? " and filtered" : "");
				log.debug(message);
			}

			if (!filterLoadBalancingPuctuations) {
				// Transfer to next operator
				this.sendPunctuation(punctuation);
			}
		} else {
			// Transfer all other punctuations to next operator
			this.sendPunctuation(punctuation);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
