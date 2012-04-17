package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order;

public class BufferedFilterPO<K extends ITimeInterval, R extends IMetaAttributeContainer<K>>
		extends AbstractPipe<R, R> {

	private static final int BUFFERPORT = 0;
	
	private final IPredicate<? super R> predicate;
	private final List<R> buffer = new LinkedList<R>();
	private final long bufferTime;
	private final long deliverTime;
	private PointInTime deliverUntil = null;

	final IDataMergeFunction<R> dataMerge;
	final IMetadataMergeFunction<K> metadataMerge;
	
	private R trigger = null;

	public BufferedFilterPO(IPredicate<? super R> predicate, long bufferTime,
			long deliverTime, IDataMergeFunction<R> dataMerge, IMetadataMergeFunction<K> metadataMerge) {
		super();
		this.predicate = predicate.clone();
		this.bufferTime = bufferTime;
		this.deliverTime = deliverTime;
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
	}

	public BufferedFilterPO(BufferedFilterPO<K, R> pipe) {
		super(pipe);
		this.predicate = pipe.predicate.clone();
		this.bufferTime = pipe.bufferTime;
		this.deliverTime = pipe.deliverTime;
		this.dataMerge = pipe.dataMerge.clone();
		this.metadataMerge = pipe.metadataMerge.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		buffer.clear();
		deliverUntil = null;
	}

	@Override
	protected void process_done() {
		// TODO: What to do in this case? Empty buffer?
	}

	@Override
	protected void process_next(R object, int port) {
		synchronized (buffer) {
			if (port == BUFFERPORT) {
				buffer.add(object);
			} else {
				if (predicate.evaluate(object)) {
					// Set output time
					deliverUntil = PointInTime.plus(object.getMetadata()
							.getStart(), deliverTime);
					// Set trigger object, that will be appended
					trigger  = object;
				}
			}
			produceData(object.getMetadata().getStart());
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		produceData(timestamp);
		sendPunctuation(timestamp);
	}

	private void produceData(PointInTime timestamp) {
		synchronized (buffer) {
			// 1. Remove outdated values
			R elem;
			boolean bufferCleard = false;
			while (buffer.size() > 0 && !bufferCleard) {
				elem = buffer.get(0);
				if (elem.getMetadata().getStart().getMainPoint() + bufferTime < timestamp
						.getMainPoint()) {
					// Send filtered data to output port 1
					transfer(buffer.remove(0), 1);
					continue;
				} else {
					bufferCleard = true;
				}
			}

			// 2. Produce values, if timestamp before deliverUntil
			if (deliverUntil != null
					&& PointInTime.beforeOrEquals(timestamp, deliverUntil)) {
				// all elements remaining in the buffer are relevant
				while (buffer.size() > 0) {
					R toTransfer = merge(trigger,buffer.remove(0), Order.LeftRight);
					transfer(toTransfer);
				}
			}
		}
	}
	
	protected R merge(R left, R right, Order order) {
		// if (logger.isTraceEnabled()) {
		// logger.trace("JoinTIPO (" + hashCode() + ") start merging: " + left
		// + " AND " + right);
		// }
		R mergedData;
		K mergedMetadata;
		if (order == Order.LeftRight) {
			mergedData = dataMerge.merge(left, right);
			mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(),
					right.getMetadata());
		} else {
			mergedData = dataMerge.merge(right, left);
			mergedMetadata = metadataMerge.mergeMetadata(right.getMetadata(),
					left.getMetadata());
		}
		mergedData.setMetadata(mergedMetadata);
		return mergedData;
	}

	@Override
	public BufferedFilterPO<K, R> clone() {
		return new BufferedFilterPO<K, R>(this);
	}

}
