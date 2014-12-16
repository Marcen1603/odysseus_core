package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Map;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.IAddQueryResponseHandler;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryResponseMessage;

/**
 * Entity to send query parts to add. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class AddQuerySender extends AbstractRepeatingMessageSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AddQuerySender.class);

	/**
	 * The single instance of this class.
	 */
	private static AddQuerySender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link AddQuerySender}.
	 */
	public static AddQuerySender getInstance() {
		return cInstance;
	}

	private static Optional<IAddQueryResponseHandler> cAddQueryResponseHandler = Optional
			.absent();

	public static void bindAddQueryResponseHandler(IAddQueryResponseHandler serv) {
		Preconditions.checkNotNull(serv);
		cAddQueryResponseHandler = Optional.of(serv);
		LOG.debug("Bound {} as an AddQueryResponseHandler.", serv.getClass()
				.getSimpleName());
	}

	public static void unbindAddQueryResponseHandler(
			IAddQueryResponseHandler serv) {
		Preconditions.checkNotNull(serv);
		if (cAddQueryResponseHandler.isPresent()
				&& cAddQueryResponseHandler.get() == serv) {
			cAddQueryResponseHandler = Optional.absent();
			LOG.debug("Unbound {} as an AddQueryResponseHandler.", serv
					.getClass().getSimpleName());
		}
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

	private Map<UUID, RecoveryAddQueryMessage> mSentMessages = Maps
			.newHashMap();

	/**
	 * Sends given query part to a given peer by using a repeating message send
	 * process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param pql
	 *            The PQL code of the given query part. <br />
	 *            Must be not null.
	 * @param localQuery
	 *            The id of the local query.
	 * @param queryState
	 *            The state of the query. <br />
	 *            Must be not null.
	 * @param processId
	 *            The id of the recovery process. <br />
	 *            Must be not null.
	 * @param subprocessId
	 *            The id of the recovery subprocess. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendAddQueryPart(PeerID destination, String pql,
			int localQuery, QueryState queryState, java.util.UUID processId,
			java.util.UUID subprocessId, IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(pql);
		Preconditions.checkNotNull(processId);
		Preconditions.checkNotNull(subprocessId);
		Preconditions.checkNotNull(communicator);

		RecoveryAddQueryMessage message = new RecoveryAddQueryMessage(pql,
				localQuery, queryState, processId, subprocessId);
		this.mSentMessages.put(message.getUUID(), message);
		return repeatingSend(destination, message, message.getUUID(),
				communicator);

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RecoveryAddQueryResponseMessage.class);
		serv.addListener(this, RecoveryAddQueryResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RecoveryAddQueryResponseMessage.class);
		serv.removeListener(this, RecoveryAddQueryResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof RecoveryAddQueryResponseMessage) {
			RecoveryAddQueryResponseMessage response = (RecoveryAddQueryResponseMessage) message;
			RecoveryAddQueryMessage sentMessage = this.mSentMessages
					.get(response.getUUID());
			cAddQueryResponseHandler.get().handleAddQueryResponse(senderPeer,
					cRecoveryCommunicator.get(), response,
					sentMessage.getRecoveryProcessId(),
					sentMessage.getmSubprocessId(), sentMessage.getPQLCode(),
					sentMessage.getLocalQueryId());
		}
	}

}