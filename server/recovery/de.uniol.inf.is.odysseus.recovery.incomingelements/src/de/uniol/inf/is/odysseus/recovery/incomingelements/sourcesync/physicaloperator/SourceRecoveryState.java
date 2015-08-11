package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;

/**
 * State for source recovery operators ({@link SourceBackupPO} and
 * {@link SourceRecoveryPO}). Contains one object: <br />
 * The offset of the data stream element stored on the Kafka server, which is
 * the first to recover.
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
	 * The offset for the Kafka consumer.
	 */
	private final long mOffset;

	/**
	 * Gets the offset for the Kafka consumer.
	 * 
	 * @return The offset of the data stream element stored on the Kafka server,
	 *         which is the first to recover.
	 */
	public long getOffset() {
		return this.mOffset;
	}

	/**
	 * Creates a new state.
	 * 
	 * @param offset
	 *            The offset of the data stream element stored on the Kafka
	 *            server, which is the first to recover.
	 */
	public SourceRecoveryState(long offset) {
		this.mOffset = offset;
	}

	@Override
	public long estimateSizeInBytes() {
		return 8l;
	}

}