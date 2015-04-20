package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class ControllablePhysicalSubscription<K> extends
		AbstractPhysicalSubscription<K> {

	private static final long serialVersionUID = -9102495312187048754L;

	@SuppressWarnings("rawtypes")
	final private List<IStreamObject> suspendBuffer = new LinkedList<>();
	private int sheddingFactor = 0;
	private int currentSheddingValue = 0;
	private int suspendCalls = 0;

	public ControllablePhysicalSubscription(K target, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		super(target, sinkInPort, sourceOutPort, schema);
	}

	public void setTarget(K target) {
		super.setTarget(target);
	}

	public boolean isSuspended() {
		return getOpenCalls() == suspendCalls;
	}

	public int getBufferSize() {
		return suspendBuffer.size();
	}

	/**
	 * This is the value the resulting stream should be smaller (e.g. 25 mean,
	 * the result stream should have 75 percent of the objects)
	 * 
	 * @param sheddingFactor
	 */
	public void setSheddingFactor(int sheddingFactor) {
		assert sheddingFactor >= 0 && sheddingFactor <= 100 : "sheddingFactor must be betweeen 0 and 100";
		this.sheddingFactor = sheddingFactor;
	}

	public int getSheddingFactor() {
		return sheddingFactor;
	}

	public boolean isShedding() {
		return sheddingFactor > 0;
	}

	@SuppressWarnings({ "rawtypes" })
	protected void do_process(IStreamObject o) {
		// is load shedding active?

		if (sheddingFactor > 0) {
			// add sheddingFactor and if value higher 100 is read,
			// remove object
			// e.g. 25: 25, 50, 75, 100 --> write 3 Object (at 25,50 and 75),
			// and remove 1
			currentSheddingValue += sheddingFactor;
			if (currentSheddingValue < 100) {
				process_internal(o);
			} else {
				currentSheddingValue -= 100;
			}
		} else {
			process_internal(o);
		}
	}

	@SuppressWarnings({"rawtypes" })
	public void process_internal(IStreamObject o) {
		if (getOpenCalls() > 0 && getOpenCalls() == suspendCalls) {
			synchronized (suspendBuffer) {
				suspendBuffer.add(o);
			}
		} else {
			clearSuspendBuffer();
			sendObject(o);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private void clearSuspendBuffer() {
		// Fast check, if no suspension at all
		if (suspendBuffer.isEmpty())
			return;

		synchronized (suspendBuffer) {

			if (suspendBuffer.isEmpty())
				return;

			for (IStreamObject o : suspendBuffer) {
				sendObject(o);
				// TODO: Thread.yield();
			}
			suspendBuffer.clear();
		}

	}

	public void suspend() {
		suspendCalls++;
	}

	public void resume() {
		suspendCalls--;
	}

	@Override
	public String toString() {
		return super.toString() + " suspendCalls " + suspendCalls + " "
				+ suspendBuffer;
	}
}
