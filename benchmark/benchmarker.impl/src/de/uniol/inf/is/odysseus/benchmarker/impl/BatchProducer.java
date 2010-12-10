package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.interval_latency_priority.IntervalLatencyPriority;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttributeList;

public class BatchProducer extends
		AbstractSource<RelationalTuple<IntervalLatencyPriority>> {

	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private int jedeswievielteelementprio;
	private RelationalTuple<IntervalLatencyPriority> nonprio = new RelationalTuple<IntervalLatencyPriority>(
			0);
	private RelationalTuple<IntervalLatencyPriority> prio = new RelationalTuple<IntervalLatencyPriority>(
			0);

	public BatchProducer(int percentagePrios) {
		this.jedeswievielteelementprio = percentagePrios;
	}

	public void addBatch(int size, long wait) {
		this.elementCounts.add(size);
		this.frequencies.add(wait);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		Thread t = new Thread() {
			@Override
			public void run() {
				for (int j = 0; j < elementCounts.size(); ++j) {
					Integer count = elementCounts.get(j);
					Long wait = frequencies.get(j);
					try {
						Thread.sleep(wait);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					IntervalLatencyPriority ilp = new IntervalLatencyPriority(System.nanoTime());
					nonprio.setMetadata(ilp);
					
					IntervalLatencyPriority ilp2 = null;
					ilp2 = ilp.clone();
					ilp2.setPriority((byte) 1);
					prio.setMetadata(ilp2);
					for (int i = 0; i < count; ++i) {
						transfer((jedeswievielteelementprio > 0 && i % jedeswievielteelementprio == 0 ? prio
								: nonprio).clone());
					}
				}
				propagateDone();
			}
		};
		t.setPriority(Thread.NORM_PRIORITY);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public BatchProducer clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	
	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		SDFMetaAttributeList metalist = super.getMetaAttributeSchema();
		SDFMetaAttribute mataAttribute = new SDFMetaAttribute(IntervalLatencyPriority.class);
		if(!metalist.contains(mataAttribute)){
			metalist.add(mataAttribute);
		}
		return metalist;
	}		
}
