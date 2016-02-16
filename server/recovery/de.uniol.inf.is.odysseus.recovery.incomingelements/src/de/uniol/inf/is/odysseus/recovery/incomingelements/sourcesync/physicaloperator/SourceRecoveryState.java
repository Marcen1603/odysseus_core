package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;

/**
 * State for source recovery operators ({@link SourceBackupPO} and
 * {@link SourceRecoveryPO}). Contains one object: <br />
 * The last transfered data stream element at the last protection point.
 * 
 * @author Michael Brand
 *
 */
public class SourceRecoveryState extends AbstractOperatorState {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -4773386206264312937L;

	/**
	 * The last element received.
	 */
	private final IStreamObject<IMetaAttribute> lastElem;

	/**
	 * Gets the last seen element.
	 * 
	 * @return The element, which was last processed by the operator.
	 */
	public IStreamObject<IMetaAttribute> getLastSeenElement() {
		return this.lastElem;
	}

	/**
	 * Creates a new state.
	 * 
	 * @param lastSeenElement
	 *            The element, which was last processed by the operator.
	 */
	public SourceRecoveryState(IStreamObject<IMetaAttribute> lastSeenElement) {
		this.lastElem = lastSeenElement;
	}

	@Override
	public long estimateSizeInBytes() {
		return getSizeInBytesOfSerializable(this.lastElem);
	}

}