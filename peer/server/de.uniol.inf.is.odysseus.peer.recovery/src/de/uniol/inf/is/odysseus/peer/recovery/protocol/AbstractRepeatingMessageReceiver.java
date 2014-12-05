package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Set;

import net.jxta.impl.id.UUID.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;

/**
 * Entity to handle received messages. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractRepeatingMessageReceiver implements
		IPeerCommunicatorListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractRepeatingMessageReceiver.class);

	/**
	 * The result code for successes.
	 */
	public static final String OK_RESULT = "OK";

	/**
	 * The peer communicator, if there is one bound.
	 */
	protected Optional<IPeerCommunicator> mPeerCommunicator = Optional.absent();

	/**
	 * Binds a peer communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to bind. <br />
	 *            Must be not null.
	 */
	public void bindPeerCommunicator(IPeerCommunicator serv) {

		Preconditions.checkNotNull(serv);
		if (!mPeerCommunicator.isPresent()) {
			mPeerCommunicator = Optional.of(serv);
			LOG.debug("Bound {} as a peer communicator.", serv.getClass()
					.getSimpleName());
		}
	}

	/**
	 * Unbinds a peer communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The peer communicator to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindPeerCommunicator(IPeerCommunicator serv) {

		Preconditions.checkNotNull(serv);

		if (mPeerCommunicator.isPresent() && mPeerCommunicator.get() == serv) {
			mPeerCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a peer communicator.", serv.getClass()
					.getSimpleName());
		}
	}
	
	/**
	 * All already received messages (their UUID).
	 */
	protected final Set<UUID> mReceivedUUIDs = Sets.newHashSet();

}