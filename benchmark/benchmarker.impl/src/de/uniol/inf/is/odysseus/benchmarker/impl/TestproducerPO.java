package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.interval_latency_priority.IntervalLatencyPriority;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttributeList;

public class TestproducerPO extends
		AbstractSource<RelationalTuple<IntervalLatencyPriority>>{

	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private int jedeswievielteelementprio;
	
	public TestproducerPO(int percentagePrios) {
		this.jedeswievielteelementprio = percentagePrios;

	}

	public void addTestPart(int elementCount, long elementsPerSecond) {
		this.elementCounts.add(elementCount);
		this.frequencies.add(elementsPerSecond);
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		Thread t = new Thread() {
			@Override
			public void run() {
				long lastTime = System.nanoTime();
				for (int j = 0; j < elementCounts.size(); ++j) {
					Integer count = elementCounts.get(j);
					Long frequency = frequencies.get(j);
					long offset = 1000000000 / frequency;
					for (int i = 0; i < count; ++i) {
						RelationalTuple<IntervalLatencyPriority> r = new RelationalTuple<IntervalLatencyPriority>(
								1);
						r.setAttribute(0, i);
						long expectedTime = lastTime + offset;
						r
								.setMetadata(new IntervalLatencyPriority(
										expectedTime));
						if (jedeswievielteelementprio > 0 && i%(jedeswievielteelementprio) == 0) {
							r.getMetadata().setPriority((byte) 1);
						}
						while (expectedTime > System.nanoTime()) {
						}
						lastTime = expectedTime;
						transfer(r);
					}
				}
				propagateDone();
			}
		};
		t.setPriority(7);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public TestproducerPO clone()  {
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
