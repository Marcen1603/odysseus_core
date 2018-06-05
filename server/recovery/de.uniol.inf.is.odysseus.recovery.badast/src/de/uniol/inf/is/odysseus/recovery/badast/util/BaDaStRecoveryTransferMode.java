package de.uniol.inf.is.odysseus.recovery.badast.util;

import de.uniol.inf.is.odysseus.recovery.badast.physicaloperator.BaDaStRecoveryPO;

/**
 * A {@link BaDaStRecoveryPO} can have different transfer modes: It can transfer
 * elements from the original data source (live) or from BaDaSt (recovery).
 * 
 * @author Michael
 *
 */
public enum BaDaStRecoveryTransferMode {

	/**
	 * If checkpoints are used, elements before that checkpoint shall not be
	 * transferred, neither from BaDaSt nor from the original source.
	 */
	NONE,

	/**
	 * Only elements from BaDaSt shall be transferred.
	 */
	RECOVERY,

	/**
	 * Only elements from the original data source shall be transferred.
	 */
	LIVE,

	/**
	 * No elements shall be transferred. Mode to get from recovery to live
	 * without data loss or duplicated transfers.
	 */
	SWITCH;
}