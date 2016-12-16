package de.uniol.inf.is.odysseus.planmigration.simpleplanmigrationstrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;

/**
 * Timeinterval-dependant buffer which can be used to insert
 * MigrationMarkerPunctuations into the buffer. This is used to determine the
 * beginning and the end of a planmigration.
 * 
 * @author Marco Grawunder, Merlin Wasmann
 *
 * @param <T>
 */
public class MigrationBuffer<T extends IStreamObject<? extends ITimeInterval>> extends BufferPO<T> {

	private static final Logger LOG = LoggerFactory.getLogger(MigrationBuffer.class);

	private boolean waitForFirst = false;
	private boolean migrationStarted = true;
	private boolean markerInserted = false;
	private PointInTime newestTimestamp = null;
	private PointInTime latestEndTimestamp = null;

	private ISource<?> source = null;

	public void markMigrationStart(PointInTime maxEndTs) {
		this.migrationStarted = true;
		this.latestEndTimestamp = maxEndTs;
		LOG.debug("Migrationstart marked for {}", this.latestEndTimestamp);
	}

	/**
	 * Inserts a MigrationMarkerPunctuation at the last position of the buffer.
	 */
	@SuppressWarnings("unchecked")
	private void insertMigrationMarkerPunctuation() {
		if (this.source == null) {
			throw new RuntimeException("No source for BufferPO " + toString() + " was set.");
		}

		synchronized (this.buffer) {
			if (this.buffer.isEmpty()) {
				// wait for the first element to be put into the buffer.
				this.waitForFirst = true;
				LOG.debug("Buffer was empty. Waiting for first element");
				return;
			}

			// get the timestamp of the last element in the buffer.
			PointInTime pit = ((IStreamObject<? extends ITimeInterval>) this.buffer.getLast()).getMetadata().getStart();

			if (this.newestTimestamp == null) {
				LOG.debug("Waiting for next element to check if it has the same timestamp as the current");
				this.newestTimestamp = pit;
				return;
			}

			if (pit.equals(this.newestTimestamp)) {
				LOG.debug("Same StartTimestamp as the element before");
				return;
			}

			// create new punctuation and insert it at the last position in the
			// buffer.
			MigrationMarkerPunctuation punctuation = new MigrationMarkerPunctuation(this.newestTimestamp, this.source);
			punctuation.setSourceName(getName());
			if (LOG.isDebugEnabled()) {
				LOG.debug("Insert MigrationMarkerPunctuation in Buffer for {} with timestamp {}", getName(),
						punctuation.getTime());
			}
			this.buffer.add(this.buffer.size() - 1, punctuation);
			this.markerInserted = true;
			this.newestTimestamp = null;
		}
	}

	@Override
	protected void process_next(T object, int port) {
		synchronized (buffer) {
			elementsRead++;
			this.buffer.add(object);
			if (this.migrationStarted) {
				if (this.latestEndTimestamp == null) {
					this.latestEndTimestamp = object.getMetadata().getEnd();
					if(this.latestEndTimestamp.isInfinite()){
						LOG.error("Maximum endtimestamp is infinite. No state migration possible.");
					}
					LOG.debug("Setting latestEndTimestamp to current element {}", this.latestEndTimestamp);
				}
				// if the received objects starttimestamp is after the
				// latestEndTimestamp the MigrationMarkerPunctuation can be
				// inserted.
				if (!this.markerInserted && ((this.latestEndTimestamp != null
						&& object.getMetadata().getStart().after(this.latestEndTimestamp)) || this.waitForFirst
						|| this.newestTimestamp != null)) {
					this.waitForFirst = false;
					insertMigrationMarkerPunctuation();
				}

			}
		}
	}

	public void setSource(ISource<?> source) {
		this.source = source;
	}
}
