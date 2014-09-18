package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;

import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformationStore;

/**
 * A helper class to backup information about distributed queries within the
 * local {@link IRecoveryBackupInformationStore}.
 * 
 * @author Michael Brand
 */
public class LocalBackupInformationAccess {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(LocalBackupInformationAccess.class);

	/**
	 * The bound backup information store, if there is one bound.
	 */
	private static Optional<IRecoveryBackupInformationStore> cInfoStore = Optional
			.absent();

	/**
	 * Binds a backup information store. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param store
	 *            The backup information store to bind. <br />
	 *            Must be not null.
	 */
	public static void bindStore(IRecoveryBackupInformationStore store) {

		Preconditions.checkNotNull(store,
				"The backup information store to bind must be not null!");
		cInfoStore = Optional.of(store);
		LOG.debug("Bound {} as a backup information store for recovery.", store
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds a backup information store. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param store
	 *            The backup information store to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindStore(IRecoveryBackupInformationStore store) {

		Preconditions.checkNotNull(store,
				"The backup information store to unbind must be not null!");
		if (cInfoStore.isPresent() && cInfoStore.get().equals(store)) {

			cInfoStore = Optional.absent();
			LOG.debug("Unbound {} as a backup information store for recovery.",
					store.getClass().getSimpleName());

		}

	}

	/**
	 * Calls
	 * {@link IRecoveryBackupInformationStore#addSharedQuery(ID, Collection)} of
	 * the local backup information store.
	 * 
	 * @param sharedQueryId
	 *            The shared query id of the distributed query.
	 * @param parts
	 *            The parts to store local.
	 */
	public static void storeLocal(ID sharedQueryId,
			Collection<ILogicalQueryPart> parts) {

		if (!cInfoStore.isPresent()) {

			LOG.error("No backup information store for recovery bound!");
			return;

		}

		boolean success = cInfoStore.get().addSharedQuery(sharedQueryId, parts);
		if (!success) {

			LOG.error(
					"Backup information for shared query id '{}' were already stored!",
					sharedQueryId);
			return;

		}

	}

}