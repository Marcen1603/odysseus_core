package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Map;

import net.jxta.id.ID;
import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RequestQueryStateMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RequestQueryStateResponseMessage;

/**
 * Entity to send query state requests. <br />
 * Uses repeating message send routines and informs by boolean return values
 * about success/fails.
 * 
 * @author Michael Brand
 *
 */
public class RequestQueryStateSender extends AbstractRepeatingMessageSender {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RequestQueryStateSender.class);

	/**
	 * The single instance of this class.
	 */
	private static RequestQueryStateSender cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link RequestQueryStateSender}.
	 */
	public static RequestQueryStateSender getInstance() {
		return cInstance;
	}

	private Map<UUID, RequestQueryStateMessage> mSentMessages = Maps
			.newHashMap();

	/**
	 * Sends given query state request to a given peer by using a repeating
	 * message send process.
	 * 
	 * @param destination
	 *            The ID of the given peer. <br />
	 *            Must be not null.
	 * @param sharedQuery
	 *            The shared query. <br />
	 *            Must be not null.
	 * @param communicator
	 *            An active peer communicator. <br />
	 *            Must be not null.
	 * @return True, if an acknowledge returned from the given peer; false,
	 *         else.
	 */
	public boolean sendRequest(PeerID destination, ID sharedQuery,
			IPeerCommunicator communicator) {
		Preconditions.checkNotNull(destination);
		Preconditions.checkNotNull(sharedQuery);
		Preconditions.checkNotNull(communicator);

		RequestQueryStateMessage message = new RequestQueryStateMessage(
				sharedQuery);
		this.mSentMessages.put(message.getUUID(), message);
		return repeatingSend(destination, message, message.getUUID(),
				communicator);

	}

	/**
	 * The executor, if there is one bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an executor. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to bind. <br />
	 *            Must be not null.
	 */
	public static void bindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);
		cExecutor = Optional.of((IServerExecutor) serv);
		LOG.debug("Bound {} as an executor.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds an executor, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The executor to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindExecutor(IExecutor serv) {

		Preconditions.checkNotNull(serv);
		Preconditions.checkArgument(serv instanceof IServerExecutor);

		if (cExecutor.isPresent() && cExecutor.get() == (IServerExecutor) serv) {

			cExecutor = Optional.absent();
			LOG.debug("Unbound {} as an executor.", serv.getClass()
					.getSimpleName());

		}

	}
	
	/**
	 * The query part controller, if there is one bound.
	 */
	private static Optional<IQueryPartController> cController = Optional
			.absent();

	/**
	 * Binds a query part controller. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The query part controller to bind. <br />
	 *            Must be not null.
	 */
	public static void bindQueryPartController(IQueryPartController serv) {

		Preconditions.checkNotNull(serv);
		cController = Optional.of((IQueryPartController) serv);
		LOG.debug("Bound {} as a query part controller.", serv.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a query part controller, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The query part controller to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindQueryPartController(IQueryPartController serv) {

		Preconditions.checkNotNull(serv);

		if (cController.isPresent()
				&& cController.get() == (IQueryPartController) serv) {

			cController = Optional.absent();
			LOG.debug("Unbound {} as a query part controller.", serv.getClass()
					.getSimpleName());

		}

	}

	@Override
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		super.bindPeerCommunicator(serv);
		cInstance = this;
		serv.registerMessageType(RequestQueryStateResponseMessage.class);
		serv.addListener(this, RequestQueryStateResponseMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RequestQueryStateResponseMessage.class);
		serv.removeListener(this, RequestQueryStateResponseMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(communicator);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(message);

		if (message instanceof RequestQueryStateResponseMessage) {
			RequestQueryStateResponseMessage response = (RequestQueryStateResponseMessage) message;
			handleResponseMessage(response.getUUID(),
					response.getErrorMessage());

			if (response.isPositive()) {
				RequestQueryStateMessage sentMessage = this.mSentMessages
						.get(response.getUUID());
				updateQueryState(sentMessage.getSharedQueryId(), response
						.getState().get());
			}
		}
	}

	private static void updateQueryState(ID sharedQueryId, QueryState queryState) {
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(queryState);
		if(!cExecutor.isPresent()) {
			LOG.error("No executor bound!");
			return;
		} else if(!cController.isPresent()) {
			LOG.error("No query part controller bound!");
			return;
		}
		
		// Note: we only consider running or not
		if(queryState == QueryState.RUNNING) {
			for(int query : cController.get().getLocalIds(sharedQueryId)) {
				if(cExecutor.get().getQueryState(query) != QueryState.RUNNING) {
					cExecutor.get().startQuery(query, RecoveryCommunicator.getActiveSession());
				}
			}
		}
	}
}