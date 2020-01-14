package de.uniol.inf.is.odysseus.server.intervalapproach.window;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.state.SlidingElementWindowTIPOState;

abstract public class AbstractPartitionedWindowTIPO<T extends IStreamObject<ITimeInterval>>
		extends AbstractWindowTIPO<T> implements IStatefulPO, IPhysicalOperatorKeyValueProvider {

	static final Logger LOG = LoggerFactory
			.getLogger(AbstractPartitionedWindowTIPO.class);

	private IGroupProcessor<T, T> groupProcessor = new NoGroupProcessor<T, T>();

	protected ITransferArea<T, T> transferArea = new TITransferArea<>();

	private Map<Object, List<T>> buffers = new HashMap<>();

	private PointInTime lastTs = null;

	public AbstractPartitionedWindowTIPO(AbstractWindowAO ao) {
		super(ao);
	}

	protected AbstractPartitionedWindowTIPO(WindowType windowType, TimeUnit baseTimeUnit,
			TimeValueItem windowSize, TimeValueItem windowAdvance,
			TimeValueItem windowSlide,
			List<SDFAttribute> partitionedBy, SDFSchema inputSchema){
		super(windowType, baseTimeUnit, windowSize, windowAdvance, windowSlide, true, partitionedBy, inputSchema);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	public void setGroupProcessor(IGroupProcessor<T, T> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		buffers.clear();
		groupProcessor.init();
		transferArea.init(this, 1);
	}

	@Override
	protected void process_next(T object, int port) {
		transferArea.newElement(object, port);
		synchronized (buffers) {
			lastTs = object.getMetadata().getStart();
			Object bufferId = groupProcessor.getGroupID(object);
			List<T> buffer = buffers.get(bufferId);
			if (buffer == null) {
				buffer = new LinkedList<T>();
				buffers.put(bufferId, buffer);
			}
			//buffer.add(object);
			process(object, buffer, bufferId, lastTs);

			if (buffer.size() == 0) {
				buffers.remove(bufferId);
			}

		}
		// Do not call:
		// transferArea.newElement(object, port);
		// Determine min element in transferBuffer and send hearbeat
	}

	abstract protected void process(T object, List<T> buffer, Object bufferId,
			PointInTime ts);

	protected PointInTime getMinTs() {
		// MinTs is the oldest element of all buffers
		// because the buffers are sorted by time, only the first element has to
		// be treated
		if (buffers.size() == 0) {
			return null;
		}
		PointInTime min = null;
		for (List<T> buffer : buffers.values()) {
			if (!buffer.isEmpty()) {
				T e = buffer.get(0);
				PointInTime test = e.getMetadata().getStart();
				if (min == null || min.after(test)) {
					min = test;
				}
			}
		}
		return min;
	}

	@Override
	protected void process_done() {
		synchronized (buffers) {
			for (List<T> b : buffers.values()) {
				if (b.size() > 0) {
					// Transfer elements in buffers
					// use lastTs+1 as timestamp for the last element (else the
					// last element
					// will be removed)
					transferBuffer(b, b.size(), lastTs.plus(1));
					for (T t : b) {
						transferArea.transfer(t);
					}
				}
			}
		}
		transferArea.newHeartbeat(lastTs, 0);
	}

	protected void transferBuffer(List<T> buffer, long numberofelements,
			PointInTime ts) {
		synchronized (buffer) {
			Iterator<T> bufferIter = buffer.iterator();

			PointInTime start = null;
			if (usesSlideParam) {
				// keep start time of first element
				T elem = buffer.get(0);
				start = elem.getMetadata().getStart();
			}
			for (int i = 0; i < numberofelements; i++) {
				T toReturn = bufferIter.next();
				bufferIter.remove();
				// If slide param is used give all elements of the window
				// the same start timestamp
				// Remark: Copy to keep order with tibreak!
				if (usesSlideParam) {
					toReturn.getMetadata().setStart(new PointInTime(start));
				}
				// We can produce tuple with no validity --> Do not send them
				if (toReturn.getMetadata().getStart().before(ts)) {
					toReturn.getMetadata().setEnd(ts);
					transferArea.transfer(toReturn);
				}
			}
		}
	}

	@Override
	public void process_close() {
		process_done();
		synchronized (buffers) {
			for (List<T> b : buffers.values()) {
				b.clear();
			}
		}
		super.process_close();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transferArea.sendPunctuation(punctuation, port);
		transferArea.newElement(punctuation, port);
	}

	public void ping() {
		PointInTime minTs = getMinTs();
		if (minTs != null) {
			transferArea.newHeartbeat(minTs, 0);
		} else {
			// No elements in any buffer
			transferArea.newHeartbeat(lastTs, 0);
		}
	}

	@Override
	public long getElementsStored1() {
		long size = 0;
		Collection<List<T>> bufs = buffers.values();
		for (List<T> b : bufs) {
			size += b.size();
		}
		return size;
	}

	@Override
	public IOperatorState getState() {
		SlidingElementWindowTIPOState<T> state = new SlidingElementWindowTIPOState<T>();
		synchronized (buffers) {
			state.setBuffers(buffers);
			state.setGroupProcessor(groupProcessor);
			state.setLastTs(lastTs);
			state.setTransferArea(transferArea);
		}
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setStateInternal(Serializable serializable) {
		try {
			SlidingElementWindowTIPOState<T> state = (SlidingElementWindowTIPOState<T>) serializable;
			synchronized (buffers) {
				buffers = state.getBuffers();
				groupProcessor = state.getGroupProcessor();
				lastTs = state.getLastTs();
				transferArea = state.getTransferArea();
				transferArea.setTransfer(this);
			}
		} catch (Throwable T) {
			LOG.error("The serializable state to set for the SlidingElementWindowTIPO is not a valid SlidingElementWindowTIPOState!");
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider#getKeyValues()
	 */
	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> result = new HashMap<>();
		result.put("Buffers Size:", "" + buffers.size());
		result.put("Transferarea Size:", "" + transferArea.size());
		result.put("Last TS:", "" + lastTs);
		result.put("Implementation:", this.getClass().getSimpleName());
		return result;
	}
	
	protected Map<Object, List<T>> getBuffers() {
		return buffers;
	}
}
