package de.uniol.inf.is.odysseus.recovery.badast.util;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;
import de.uniol.inf.is.odysseus.recovery.badast.physicaloperator.BaDaStBackupPO;
import de.uniol.inf.is.odysseus.recovery.badast.physicaloperator.BaDaStRecoveryPO;

/**
 * Operator state for {@link BaDaStBackupPO}s and {@link BaDaStRecoveryPO}s. It
 * contains the last transfered element.
 * 
 * @author Michael Brand
 */
public class BaDaStSyncState<StreamObject extends IStreamObject<IMetaAttribute>> extends AbstractOperatorState {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 4915627773710986754L;

	/**
	 * The last transferred element.
	 */
	private StreamObject reference;

	/**
	 * Gets the last transferred element.
	 */
	public StreamObject getReference() {
		return this.reference;
	}

	/**
	 * Sets the last transferred element.
	 */
	public BaDaStSyncState(StreamObject element) {
		this.reference = element;
	}

	@Override
	public long estimateSizeInBytes() {
		return getSizeInBytesOfSerializable(this.reference);
	}

}