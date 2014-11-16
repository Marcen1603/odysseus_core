package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

import com.google.common.collect.ImmutableMap;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.recovery.IRecoveryBackupInformation;

/**
 * A helper-class with methods specifically for the buddy-concept
 * 
 * @author Tobias Brandt
 *
 */
public class BuddyHelper {

	/**
	 * Determines, if we need a buddy for the part with the given PQL TODO
	 * Redefine with respect to the issue Michael found
	 * 
	 * @param pql
	 *            The PQL of the part of the shared query on this peer
	 * @return true, if you need a buddy, false, if not
	 */
	public static boolean needBuddy(String pql) {
		boolean foundReceiver = false;
		List<ILogicalQuery> logicalQueries = RecoveryHelper.convertToLogicalQueries(pql);
		for (ILogicalQuery query : logicalQueries) {
			for (ILogicalOperator op : LogicalQueryHelper.getAllOperators(query)) {
				if (op instanceof JxtaReceiverAO) {
					foundReceiver = true;
					break;
				}
			}
			if (!foundReceiver) {
				// There is a part without a Receiver -> We need a buddy
				return true;
			}
			foundReceiver = false;
		}
		return false;
	}

	// TODO JavaDoc
	public static List<IRecoveryBackupInformation> findBackupInfoForBuddy(PeerID failedPeer) {
		List<IRecoveryBackupInformation> infoList = new ArrayList<IRecoveryBackupInformation>();
		ImmutableMap<ID, Collection<IRecoveryBackupInformation>> infoStore = LocalBackupInformationAccess.getStore()
				.getAll();
		for (ID key : infoStore.keySet()) {
			for (IRecoveryBackupInformation info : infoStore.get(key)) {
				if (info.getLocationPeer().equals(failedPeer)) {
					infoList.add(info);
				}
			}
		}
		return infoList;
	}

}
