package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;

/**
 * State for source recovery operators ({@link SourceBackupPO} and
 * {@link SourceRecoveryPO}). Contains one object: <br />
 * The offset of the data stream element stored on the publish subscribe system,
 * which is the first to recover.
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
	 * The offset of the data stream element stored on the publish subscribe
	 * system, which is the first to recover.
	 */
	private final Long mOffset;

	/**
	 * Gets the offset.
	 * 
	 * @return The offset of the data stream element stored on the publish
	 *         subscribe system, which is the first to recover.
	 */
	public Long getOffset() {
		return this.mOffset;
	}

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
	 * @param offset
	 *            The offset of the data stream element stored on the publish
	 *            subscribe system, which is the first to recover.
	 * @param lastSeenElement
	 *            The element, which was last processed by the operator.
	 */
	public SourceRecoveryState(Long offset, IStreamObject<IMetaAttribute> lastSeenElement) {
		this.mOffset = offset;
		this.lastElem = lastSeenElement;
	}

	@Override
	public long estimateSizeInBytes() {
		return 8l + getSizeInBytesOfSerializable(this.lastElem);
	}

}