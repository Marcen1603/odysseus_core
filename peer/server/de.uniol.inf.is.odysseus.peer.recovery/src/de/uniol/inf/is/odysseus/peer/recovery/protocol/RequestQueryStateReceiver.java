package de.uniol.inf.is.odysseus.peer.recovery.protocol;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RequestQueryStateMessage;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RequestQueryStateResponseMessage;

/**
 * Entity to handle received query state requests. <br />
 * Handles incoming requests and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class RequestQueryStateReceiver extends AbstractRepeatingMessageReceiver {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RequestQueryStateReceiver.class);

	/**
	 * The single instance of this class.
	 */
	private static RequestQueryStateReceiver cInstance;

	/**
	 * The single instance of this class.
	 * 
	 * @return The active {@link RequestQueryStateReceiver}.
	 */
	public static RequestQueryStateReceiver getInstance() {
		return cInstance;
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
		serv.registerMessageType(RequestQueryStateMessage.class);
		serv.addListener(this, RequestQueryStateMessage.class);
	}

	@Override
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		super.unbindPeerCommunicator(serv);
		cInstance = null;
		serv.unregisterMessageType(RequestQueryStateMessage.class);
		serv.removeListener(this, RequestQueryStateMessage.class);
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof RequestQueryStateMessage) {
			RequestQueryStateMessage request = (RequestQueryStateMessage) message;
			if (!mReceivedUUIDs.contains(request.getUUID())) {
				mReceivedUUIDs.add(request.getUUID());
			} else {
				return;
			}

			RequestQueryStateResponseMessage response = null;
			try {
				QueryState state = determineQueryState(request
						.getSharedQueryId());
				response = new RequestQueryStateResponseMessage(
						request.getUUID(), state);
			} catch (Exception e) {
				response = new RequestQueryStateResponseMessage(
						request.getUUID(), e.getMessage());
			}

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error(
						"Could not send request query state response message!",
						e);
			}

		}
	}

	private static QueryState determineQueryState(ID sharedQueryId)
			throws Exception {
		Preconditions.checkNotNull(sharedQueryId);
		if (!cExecutor.isPresent()) {
			throw new NullPointerException("No executor bound!");
		} else if (!cController.isPresent()) {
			throw new NullPointerException("No query part controller bound!");
		}

		Collection<Integer> queryIds = cController.get().getLocalIds(
				sharedQueryId);
		if(queryIds.isEmpty()) {
			throw new IllegalArgumentException("No local queries for shared query id + " + sharedQueryId + "!");
		}
		
		QueryState state = null;
		for(int queryId : queryIds) {
			QueryState curState = cExecutor.get().getQueryState(queryId);
			if(curState == null) {
				throw new NullPointerException("Query state for shared query id + " + sharedQueryId + " is null!");
			} else if(state != null && state != curState) {
				throw new IllegalArgumentException("Different query states for shared query id + " + sharedQueryId + "!");
			} else if(state == null) {
				state = curState;
			}
		}
		return state;
	}

}