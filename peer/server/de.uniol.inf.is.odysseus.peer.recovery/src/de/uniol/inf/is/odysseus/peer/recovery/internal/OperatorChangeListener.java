package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.PhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaReceiverPO;
import de.uniol.inf.is.odysseus.p2p_new.physicaloperator.JxtaSenderPO;
import de.uniol.inf.is.odysseus.p2p_new.util.IObservableOperator;
import de.uniol.inf.is.odysseus.p2p_new.util.IOperatorObserver;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;
import de.uniol.inf.is.odysseus.peer.recovery.util.RecoveryHelper;

public class OperatorChangeListener implements IOperatorObserver,
		IPlanModificationListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(OperatorChangeListener.class);

	private static IPQLGenerator pqlGenerator;
	private static IRecoveryCommunicator recoveryCommunicator;
	private static IServerExecutor executor;
	private static IBackupInformationAccess backupInformationAccess;

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator generator) {
		pqlGenerator = generator;
		LOG.debug("Bound PQL generator.");
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator generator) {
		if (pqlGenerator == generator) {
			pqlGenerator = null;
			LOG.debug("Unbound PQL generator.");
		}
	}

	// called by OSGi-DS
	public static void bindRecoveryCommunicator(
			IRecoveryCommunicator communicator) {
		recoveryCommunicator = communicator;
		LOG.debug("Bound recoveryCommunicator.");
	}

	// called by OSGi-DS
	public static void unbindRecoveryCommunicator(
			IRecoveryCommunicator communicator) {
		if (recoveryCommunicator == communicator) {
			recoveryCommunicator = null;
			LOG.debug("Unbound recoveryCommunicator.");
		}
	}

	public static void bindBackupInformationAccess(
			IBackupInformationAccess infoAccess) {
		backupInformationAccess = infoAccess;
	}

	public static void unbindBackupInformationAccess(
			IBackupInformationAccess infoAccess) {
		if (backupInformationAccess == infoAccess) {
			backupInformationAccess = null;
		}
	}

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
		executor = (IServerExecutor) serv;
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

		if (executor != null && executor == (IServerExecutor) serv) {
			executor = null;
			LOG.debug("Unbound {} as an executor.", serv.getClass()
					.getSimpleName());
		}

	}

	/**
	 * Gets the executor.
	 * 
	 * @return The bound executor or {@link Optional#absent()}, if there is none
	 *         bound.
	 */
	public static IServerExecutor getExecutor() {
		return executor;
	}

	public void activate() {
		if (executor != null) {
			executor.addPlanModificationListener(this);
			LOG.debug("Activated OperatorChangeLister.");
		}
	}

	public void deactivate() {
		if (executor != null) {
			executor.removePlanModificationListener(this);
			LOG.debug("Deactivated OperatorChangeLister.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(IObservableOperator observable, Object arg) {

		Preconditions.checkNotNull(pqlGenerator);

		boolean updateReceiver = false;
		if (observable instanceof JxtaReceiverPO)
			updateReceiver = true;
		else if (observable instanceof JxtaSenderPO)
			updateReceiver = false;

		if (observable instanceof JxtaReceiverPO
				|| observable instanceof JxtaSenderPO) {
			if (arg instanceof List) {
				List<String> infoList = (List<String>) arg;
				if (infoList.size() >= 3) {
					String oldPeerId = infoList.get(0);
					String newPeerId = infoList.get(1);
					String pipeId = infoList.get(2);

					// Find the query and get the local id
					IPhysicalQuery query = RecoveryHelper
							.findInstalledQueryWithJxtaOperator(
									RecoveryHelper.convertToPipeId(pipeId),
									updateReceiver);
					int queryId = query.getID();

					// Old peer-id is replaced by the new one ...
					String pql = backupInformationAccess.getBackupPQL(queryId);
					String sharedQuery = backupInformationAccess
							.getBackupSharedQuery(queryId);
					boolean master = backupInformationAccess
							.isBackupMaster(queryId);
					String masterId = backupInformationAccess.getBackupMasterId(queryId);
					String newPQL = pql.replace(oldPeerId, newPeerId);

					// Save this new information
					backupInformationAccess.saveBackupInformation(queryId,
							newPQL, query.getState().toString(), sharedQuery,
							master, masterId);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		// The plan was modified
		if (eventArgs.getEventType().equals(
				PlanModificationEventType.QUERY_ADDED)) {
			LOG.debug("A query was added - search for JxtaReceiverPO to observe.");
			if (eventArgs.getValue() instanceof PhysicalQuery) {
				PhysicalQuery query = (PhysicalQuery) eventArgs.getValue();
				// Search for the JXTA-Sender
				JxtaReceiverPO receiver = RecoveryHelper
						.findJxtaReceiverPO(query);
				if (receiver != null)
					receiver.addObserver(this);

				JxtaSenderPO sender = RecoveryHelper.findJxtaSenderPO(query);
				if (sender != null)
					sender.addObserver(this);

			}
		}
	}

}
