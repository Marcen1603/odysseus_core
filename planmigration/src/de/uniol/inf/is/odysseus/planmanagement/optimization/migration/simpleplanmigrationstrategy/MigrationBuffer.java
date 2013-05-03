package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.MigrationMarkerPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;

public class MigrationBuffer<T extends IStreamObject<?>> extends BufferPO<T> {

	private boolean waitForFirst = false;
	private PointInTime newestTimestamp = null;
	
	private ISource<?> source = null;
	
	/**
	 * Inserts a MigrationMarkerPunctuation at the last position of the buffer.
	 */
	@SuppressWarnings("unchecked")
	public void insertMigrationMarkerPunctuation() {
		if(this.source == null) {
			throw new RuntimeException("No source for BufferPO " + toString() + " was set.");
		}

		synchronized (this.buffer) {
			if (this.buffer.isEmpty()) {
				// wait for the first element to be put into the buffer.
				this.waitForFirst = true;
				getLogger()
						.debug("Buffer was empty. Waiting for first element");
				return;
			}

			// get the timestamp of the last element in the buffer.
			PointInTime pit = ((IStreamObject<? extends ITimeInterval>) this.buffer
					.getLast()).getMetadata().getStart();

			if (this.newestTimestamp == null) {
				getLogger()
						.debug("Waiting for next element to check if it has the same timestamp as the current");
				this.newestTimestamp = pit;
				return;
			}

			if (pit.equals(this.newestTimestamp)) {
				getLogger().debug("Same StartTimestamp as the element before");
				return;
			}

			// create new punctuation and insert it at the last position in the
			// buffer.
			IPunctuation punctuation = new MigrationMarkerPunctuation(this.newestTimestamp,
					this.source);
			if (getLogger().isDebugEnabled()) {
				getLogger().debug(
						"Insert MigrationMarkerPunctuation in Buffer for {} with timestamp {}", getName(), punctuation.getTime());
			}
			this.buffer.add(this.buffer.size() - 1, punctuation);
			this.newestTimestamp = null;
		}
	}
	
	@Override
	protected void process_next(T object, int port) {
		synchronized (buffer) {
			this.buffer.add(object);
			if(this.waitForFirst || this.newestTimestamp != null) {
				this.waitForFirst = false;
				insertMigrationMarkerPunctuation();
			}
		}
	}
	
	public void setSource(ISource<?> source) {
		this.source = source;
	}
}
