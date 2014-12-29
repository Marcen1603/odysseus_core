package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryStrategyMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryStrategyResponseMessage;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

/**
 * Entity to send recovery strategy information. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryStrategySender extends AbstractRepeatingMessageSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryStrategySender.class);

	/**
	 * The single instance of this class.
	 */
	private static RecoveryStrategySender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link RecoveryStrategySender}.
	 */
	public static RecoveryStrategySender getInstance() {
		return cInstance;
	}

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cRecoveryCommunicator = Optional
			.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindRecoveryCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IRecoveryCommunicator);
		cRecoveryCommunicator = Optional.of((IRecoveryCommunicator) serv);
		LOG.debug("Bound {} as a recovery communicator.", serv.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindRecoveryCommunicator(IRecoveryCommunicator serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IRecoveryCommunicator);

		if (cRecoveryCommunicator.isPresent()
				&& cRecoveryCommunicator.get() == (IRecoveryCommunicator) serv) {

			cRecoveryCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", serv.getClass()
					.getSimpleName());

		}

	}

	/**
	 * Sends given recovery strategy for a given query part to a given peer by
	 * using a repeating message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param part
	 *            The given query part. <br />
	 *            Must be not null.
	 * @param strategy
	 *            The given strategy. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendRecoveryStrategy(PeerID destination,
			ILogicalQueryPart part, String strategy,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(part);
		Preconditions.checkNotNull(strategy);
		Preconditions.checkNotNull(communicator);

		String pql = RecoveryHelper.convertToPQL(part);
		RecoveryStrategyMessage message = new RecoveryStrategyMessage(pql,
				strategy);
		return repeatingSend(destination, message, message.getUUID(),
				communicator);

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryStrategyResponseMessage.class);
		serv.addListener(this, RecoveryStrategyResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryStrategyResponseMessage.class);
		serv.removeListener(this, RecoveryStrategyResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof RecoveryStrategyResponseMessage) {
			RecoveryStrategyResponseMessage response = (RecoveryStrategyResponseMessage) message;
			handleResponseMessage(response.getUUID(),
					response.getErrorMessage());
		}
	}

}