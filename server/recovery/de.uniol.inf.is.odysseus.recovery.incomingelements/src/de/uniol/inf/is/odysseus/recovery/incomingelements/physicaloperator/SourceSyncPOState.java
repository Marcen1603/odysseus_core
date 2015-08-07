package de.uniol.inf.is.odysseus.recovery.incomingelements.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;

/**
 * State for {@link SourceSyncPO}s. Contains only the offset for the Kafka
 * Consumer.
 * 
 * @author Michael Brand
 *
 */
public class SourceSyncPOState extends AbstractOperatorState implements
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
	 * @return The offset or -1, if none is set.
	 */
	public long getOffset() {
		return this.mOffset;
	}

	/**
	 * Creates a new state.
	 * 
	 * @param offset
	 *            The offset or -1, if none is set.
	 */
	public SourceSyncPOState(long offset) {
		this.mOffset = offset;
	}

	@Override
	public long estimateSizeInBytes() {
		return 8l;
	}

}
