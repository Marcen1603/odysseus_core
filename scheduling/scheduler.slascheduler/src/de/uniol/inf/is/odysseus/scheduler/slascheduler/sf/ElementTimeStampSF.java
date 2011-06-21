package de.uniol.inf.is.odysseus.scheduler.slascheduler.sf;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.IStarvationFreedom;

public class ElementTimeStampSF implements IStarvationFreedom {
	
	private List<IBuffer<?>> buffers;
	
	public ElementTimeStampSF(IPartialPlan plan) {
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
		double sf = decay * this.longestTupleWaitingTime();
		return sf *sf;
	}
	
	private long longestTupleWaitingTime() {
		long oldestTS = Long.MAX_VALUE;
		
		// find element with oldest ts
		for (IBuffer<?> buffer : this.buffers) {
			Object head = buffer.peek();
			if (head instanceof MetaAttributeContainer<?>) {
				MetaAttributeContainer<?> metaContainer = (MetaAttributeContainer<?>) head;
				IMetaAttribute metaData = metaContainer.getMetadata();
				if (metaData instanceof ILatency) {
					ILatency latency = (ILatency)metaData;
					long startTS = latency.getLatencyStart();
					if (startTS < oldestTS) {
						oldestTS = startTS;
					}
				} else {
					throw new RuntimeException("Latency missing");
				}
			}
		}
		
		// avoid returning negative delta
		long delta = System.currentTimeMillis() - oldestTS; 
		return (delta < 0) ? 0 : delta;
	}

}
