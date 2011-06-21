package de.uniol.inf.is.odysseus.scheduler.slascheduler.sf;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.IStarvationFreedom;

public class QueueSizeSF implements IStarvationFreedom {
	
	private List<IBuffer<?>> buffers;
	
	public QueueSizeSF(IPartialPlan plan) {
		super();
		this.buffers = new ArrayList<IBuffer<?>>();
		for (IIterableSource<?> src : plan.getIterableSources()) {
			if (src instanceof IBuffer<?>) {
				IBuffer<?> buffer = (IBuffer<?>)src;
				this.buffers.add(buffer);
			}
		}
	}

	@Override
	public double sf(double decay) {
		// use quadratic function
		double sf = this.queueSize() * decay;
		return sf * sf;
	}
	
	private int queueSize() {
		int size = 0;
		for (IBuffer<?> buffer : this.buffers) {
			size += buffer.size();
		}
		return size;
	}

}
