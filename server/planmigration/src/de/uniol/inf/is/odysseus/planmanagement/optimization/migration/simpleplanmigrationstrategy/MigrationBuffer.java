package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.MigrationMarkerPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorPhysicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;

/**
 * Timeinterval-dependant buffer which can be used to insert MigrationMarkerPunctuations into the buffer.
 * This is used to determine the beginning and the end of a planmigration.
 * 
 * @author Marco Grawunder, Merlin Wasmann
 *
 * @param <T>
 */
public class MigrationBuffer<T extends IStreamObject<? extends ITimeInterval>> extends BufferPO<T> {

	private static final Logger LOG = LoggerFactory.getLogger(MigrationBuffer.class);
	
	private boolean waitForFirst = false;
	private boolean markerInserted = false;
	private PointInTime newestTimestamp = null;
	private PointInTime latestEndTimestamp = null;
	
	private ISource<?> source = null;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void markMigrationStart(IPhysicalOperator root) {
		GenericGraphWalker walker = new GenericGraphWalker();
		CollectOperatorPhysicalGraphVisitor<IPhysicalOperator> visitor = new CollectOperatorPhysicalGraphVisitor(JoinTIPO.class);
		walker.prefixWalkPhysical(root, visitor);
		Set<IPhysicalOperator> joins = visitor.getResult();
		PointInTime latest = null;
		for(IPhysicalOperator join : joins) {
			PointInTime end = ((JoinTIPO) join).getLatestEndTimestamp();
			if(latest == null || (end != null && latest.before(end))) {
				latest = end;
			}
		}
		this.latestEndTimestamp = latest;
		LOG.debug("Migrationstart marked for {}", this.latestEndTimestamp);
	}
	
	/**
	 * Inserts a MigrationMarkerPunctuation at the last position of the buffer.
	 */
	@SuppressWarnings("unchecked")
	private void insertMigrationMarkerPunctuation() {
		if(this.source == null) {
			throw new RuntimeException("No source for BufferPO " + toString() + " was set.");
		}

		synchronized (this.buffer) {
			if (this.buffer.isEmpty()) {
				// wait for the first element to be put into the buffer.
				this.waitForFirst = true;
				LOG
						.debug("Buffer was empty. Waiting for first element");
				return;
			}

			// get the timestamp of the last element in the buffer.
			PointInTime pit = ((IStreamObject<? extends ITimeInterval>) this.buffer
					.getLast()).getMetadata().getStart();

			if (this.newestTimestamp == null) {
				LOG
						.debug("Waiting for next element to check if it has the same timestamp as the current");
				this.newestTimestamp = pit;
				return;
			}

			if (pit.equals(this.newestTimestamp)) {
				LOG.debug("Same StartTimestamp as the element before");
				return;
			}

			// create new punctuation and insert it at the last position in the
			// buffer.
			IPunctuation punctuation = new MigrationMarkerPunctuation(this.newestTimestamp,
					this.source);
			((MigrationMarkerPunctuation) punctuation).setSourceName(getName());
			if (LOG.isDebugEnabled()) {
				LOG.debug(
						"Insert MigrationMarkerPunctuation in Buffer for {} with timestamp {}", getName(), punctuation.getTime());
			}
			this.buffer.add(this.buffer.size() - 1, punctuation);
			this.markerInserted = true;
			this.newestTimestamp = null;
		}
	}
	
	@Override
	protected void process_next(T object, int port) {
		synchronized (buffer) {
			this.buffer.add(object);
			if(this.latestEndTimestamp == null) {
				this.latestEndTimestamp = object.getMetadata().getStart();
				LOG.debug("Setting latestEndTimestamp to current element {}", this.latestEndTimestamp);
			}
			// if the received objects starttimestamp is after the latestEndTimestamp the MigrationMarkerPunctuation can be inserted.
			if (!this.markerInserted && ((this.latestEndTimestamp != null && object.getMetadata()
					.getStart().after(this.latestEndTimestamp))
					|| this.waitForFirst || this.newestTimestamp != null)) {
				this.waitForFirst = false;
				insertMigrationMarkerPunctuation();
			}
		}
	}
	
	public void setSource(ISource<?> source) {
		this.source = source;
	}
}
