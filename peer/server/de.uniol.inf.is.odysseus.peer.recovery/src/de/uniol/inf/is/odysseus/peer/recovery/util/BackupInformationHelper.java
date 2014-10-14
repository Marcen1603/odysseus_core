package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartController;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;

/**
 * A helper class for backup information (e.g., update of multiple stores).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationHelper implements IPlanExecutionListener {

	/**
	 * The logger instance for this class.
	 */
	private static Logger LOG = LoggerFactory
			.getLogger(BackupInformationHelper.class);

	/**
	 * The recovery communicator, if there is one bound.
	 */
	private static Optional<IRecoveryCommunicator> cCommunicator = Optional
			.absent();

	/**
	 * Binds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to bind. <br />
	 *            Must be not null.
	 */
	public static void bindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to bind must be not null!");
		cCommunicator = Optional.of(communicator);
		LOG.debug("Bound {} as a recovery communicator.", communicator
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds a recovery communicator. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param communicator
	 *            The recovery communicator to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindCommunicator(IRecoveryCommunicator communicator) {

		Preconditions.checkNotNull(communicator,
				"The recovery communicator to unbind must be not null!");
		if (cCommunicator.isPresent()
				&& cCommunicator.get().equals(communicator)) {

			cCommunicator = Optional.absent();
			LOG.debug("Unbound {} as a recovery communicator.", communicator
					.getClass().getSimpleName());

		}

	}

	/**
	 * The query part controller, if there is one bound.
	 */
	private static Optional<IQueryPartController> cController = Optional
			.absent();

	/**
	 * Binds a query part controller. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param controller
	 *            The query part controller to bind. <br />
	 *            Must be not null.
	 */
	public static void bindController(IQueryPartController controller) {

		Preconditions.checkNotNull(controller,
				"The query part controller to bind must be not null!");
		cController = Optional.of(controller);
		LOG.debug("Bound {} as a query part controller.", controller.getClass()
				.getSimpleName());

	}

	/**
	 * Unbinds a query part controller. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param controller
	 *            The query part controller to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindController(IQueryPartController controller) {

		Preconditions.checkNotNull(controller,
				"The query part controller to unbind must be not null!");
		if (cController.isPresent() && cCommunicator.get().equals(controller)) {

			cController = Optional.absent();
			LOG.debug("Unbound {} as a query part controller.", controller
					.getClass().getSimpleName());

		}

	}

	/**
	 * Updates the backup information store (of this and other affected peers)
	 * after a take over.
	 * 
	 * @param oldPeer
	 *            The former peer, which has executed <code>pqlCode</code>. <br />
	 *            Must be not null.
	 * @param newPeer
	 *            The new peer, which executes <code>pqlCode</code> from now on. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The id of the distributed query. <br />
	 *            Must be not null.
	 * @param pqlCode
	 *            The PQL code, which has been taken over. <br />
	 *            Must be null.
	 */
	public static void updateInfoStores(PeerID oldPeer, PeerID newPeer,
			ID sharedQueryId, String pqlCode) {

		Preconditions.checkNotNull(oldPeer);
		Preconditions.checkNotNull(newPeer);
		Preconditions.checkNotNull(sharedQueryId);
		Preconditions.checkNotNull(pqlCode);

		if (!cCommunicator.isPresent()) {

			LOG.error("No communicator for recovery bound!");
			return;

		}

		// The affected backup information
		Optional<IRecoveryBackupInformation> affectedInfo = LocalBackupInformationAccess
				.getStore().get(pqlCode);

		Collection<IRecoveryBackupInformation> subsequentInfos = Sets
				.newHashSet();
		if (affectedInfo.isPresent()) {

			// Determine, which backup information have to be sent to the new
			// peer
			subsequentInfos = affectedInfo.get()
					.getSubsequentPartsInformation();

		}

		// Send these information
		for (IRecoveryBackupInformation info : subsequentInfos) {

			cCommunicator.get().sendBackupInformation(newPeer, info);

		}

		if (affectedInfo.isPresent()) {

			// Update the local backup information due to the take over
			LocalBackupInformationAccess.getStore().remove(affectedInfo.get());

		}

	}

	@Override
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs) {

		if (!cController.isPresent()) {

			LOG.error("No query part controller bound!");
			return;

		}

		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs
				.getEventType())) {

			int queryID = ((IPhysicalQuery) eventArgs.getValue()).getID();
			ID sharedQueryID = cController.get().getSharedQueryID(queryID);
			if (sharedQueryID == null) {
				return; // query was not shared
			}

			// Remove backup information
			LocalBackupInformationAccess.getStore().remove(sharedQueryID);

		}

	}
}