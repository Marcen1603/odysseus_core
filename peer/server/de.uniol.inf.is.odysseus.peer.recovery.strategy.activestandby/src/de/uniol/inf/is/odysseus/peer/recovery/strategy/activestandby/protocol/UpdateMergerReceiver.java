package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.protocol;

import java.util.Collection;
import java.util.Set;

import net.jxta.impl.id.UUID.UUID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.peer.communication.IMessage;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicator;
import de.uniol.inf.is.odysseus.peer.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.peer.communication.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.physicaloperator.RecoveryMergePO;

/**
 * Entity to handle received merger update instructions. <br />
 * Handles incoming instructions and sends acknowledge/failure messages.
 * 
 * @author Michael Brand
 *
 */
public class UpdateMergerReceiver implements IPeerCommunicatorListener {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UpdateMergerReceiver.class);

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
			serv.registerMessageType(UpdateMergerMessage.class);
			serv.addListener(this, UpdateMergerMessage.class);
			LOG.debug("Bound {} as a peer communicator.", serv.getClass().getSimpleName());
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
			serv.unregisterMessageType(UpdateMergerMessage.class);
			serv.removeListener(this, UpdateMergerMessage.class);
			LOG.debug("Unbound {} as a peer communicator.", serv.getClass().getSimpleName());
		}
	}

	/**
	 * The Peer dictionary, if there is one bound.
	 */
	private static Optional<IPeerDictionary> cPeerDictionary = Optional.absent();

	/**
	 * Binds a Peer dictionary. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to bind. <br />
	 *            Must be not null.
	 */
	public static void bindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);
		cPeerDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a Peer dictionary.", serv.getClass().getSimpleName());

	}

	/**
	 * Unbinds a Peer dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);

		if (cPeerDictionary.isPresent() && cPeerDictionary.get() == serv) {

			cPeerDictionary = Optional.absent();
			LOG.debug("Unbound {} as a Peer dictionary.", serv.getClass().getSimpleName());

		}

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
			LOG.debug("Unbound {} as an executor.", serv.getClass().getSimpleName());

		}

	}

	/**
	 * All already received messages (their UUID).
	 */
	private final Set<UUID> mReceivedUUIDs = Sets.newHashSet();

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		Preconditions.checkNotNull(message);
		Preconditions.checkNotNull(senderPeer);
		Preconditions.checkNotNull(communicator);

		if (message instanceof UpdateMergerMessage) {
			UpdateMergerMessage upMessage = (UpdateMergerMessage) message;
			if (!mReceivedUUIDs.contains(upMessage.getUUID())) {
				mReceivedUUIDs.add(upMessage.getUUID());
			} else {
				return;
			}

			UpdateMergerResponseMessage response = null;
			updateMerger(upMessage.getPipeId());
			response = new UpdateMergerResponseMessage(upMessage.getUUID());

			try {
				communicator.send(senderPeer, response);
			} catch (PeerCommunicationException e) {
				LOG.error("Could not send UpdateMergerResponse message!", e);
			}

		}
	}

	private void updateMerger(PipeID pipeId) {

		if (!cExecutor.isPresent()) {
			LOG.error("No executor bound!");
			return;
		}

		@SuppressWarnings("rawtypes")
		Optional<JxtaReceiverPO> optReceiver = findReceiver(pipeId, cExecutor.get());
		if (optReceiver.isPresent()) {
			Collection<IPair<RecoveryMergePO<?>, Integer>> foundMergers = Lists.newArrayList();
			findMergers(optReceiver.get(), foundMergers);
			for (IPair<RecoveryMergePO<?>, Integer> mergerAndPort : foundMergers) {
				mergerAndPort.getE1().setInputAsRecovered(mergerAndPort.getE2());
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static Optional<JxtaReceiverPO> findReceiver(PipeID pipeId, IServerExecutor executor) {
		for (IPhysicalQuery query : executor.getExecutionPlan().getQueries()) {
			for (IPhysicalOperator op : query.getAllOperators()) {
				if (JxtaReceiverPO.class.isInstance(op) && ((JxtaReceiverPO<?>) op).getPipeIDString().equals(pipeId.toString())) {
					return Optional.of((JxtaReceiverPO) op);
				}
			}
		}
		return Optional.absent();
	}

	private static void findMergers(AbstractSource<?> startPO, Collection<IPair<RecoveryMergePO<?>, Integer>> foundMergers) {
		for (AbstractPhysicalSubscription<?> sub : startPO.getSubscriptions()) {
			if (RecoveryMergePO.class.isInstance(sub.getTarget())) {
				foundMergers.add(new Pair<RecoveryMergePO<?>, Integer>((RecoveryMergePO<?>) sub.getTarget(), sub.getSinkInPort()));
			} else {
				if (AbstractSource.class.isInstance(sub.getTarget())) {
					findMergers((AbstractSource<?>) sub.getTarget(), foundMergers);
				} else if (AbstractPipe.class.isInstance(sub.getTarget())) {
					findMergers((AbstractPipe<?, ?>) sub.getTarget(), foundMergers);
				}

			}
		}
	}

	private static void findMergers(AbstractPipe<?, ?> startPO, Collection<IPair<RecoveryMergePO<?>, Integer>> foundMergers) {
		for (AbstractPhysicalSubscription<?> sub : startPO.getSubscriptions()) {
			if (RecoveryMergePO.class.isInstance(sub.getTarget())) {
				foundMergers.add(new Pair<RecoveryMergePO<?>, Integer>((RecoveryMergePO<?>) sub.getTarget(), sub.getSinkInPort()));
			} else {
				if (AbstractSource.class.isInstance(sub.getTarget())) {
					findMergers((AbstractSource<?>) sub.getTarget(), foundMergers);
				} else if (AbstractPipe.class.isInstance(sub.getTarget())) {
					findMergers((AbstractPipe<?, ?>) sub.getTarget(), foundMergers);
				}

			}
		}
	}

}