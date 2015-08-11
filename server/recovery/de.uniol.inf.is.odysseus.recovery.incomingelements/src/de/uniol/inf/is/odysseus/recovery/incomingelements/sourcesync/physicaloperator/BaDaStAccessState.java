package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;

/**
 * State for {@link BaDaStBackupPO}s. Contains one object: <br />
 * The index of the data stream element stored on the Kafka server, which is the first to process, or {@code -1l}, if no offset is set.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStAccessState extends AbstractOperatorState implements
		IOperatorState {

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
	 * @return The index of the data stream element stored on the Kafka server, which is the first to process, or {@code -1l}, if no offset is set.
	 */
	public long getOffset() {
		return this.mOffset;
	}

	/**
	 * Creates a new state.
	 * 
	 * @param offset
	 *           The index of the data stream element stored on the Kafka server, which is the first to process, or {@code -1l}, if no offset is set.
	 */
	public BaDaStAccessState(long offset) {
		this.mOffset = offset;
	}

	@Override
	public long estimateSizeInBytes() {
		return 8l;
	}

}
