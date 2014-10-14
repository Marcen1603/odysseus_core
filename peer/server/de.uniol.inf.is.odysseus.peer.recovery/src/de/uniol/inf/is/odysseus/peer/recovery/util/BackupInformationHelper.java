package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryCommunicator;

/**
 * A helper class for backup information (e.g., update of multiple stores).
 * 
 * @author Michael Brand
 *
 */
public class BackupInformationHelper {

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

}