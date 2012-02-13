/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.List;

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
		List<SDFMetaAttribute> metalist = new ArrayList<SDFMetaAttribute>(super.getMetaAttributeSchema().getAttributes());
		SDFMetaAttribute mataAttribute = new SDFMetaAttribute(IntervalLatencyPriority.class);
		if(!metalist.contains(mataAttribute)){
			metalist.add(mataAttribute);
		}
		return new SDFMetaAttributeList(super.getMetaAttributeSchema().getURI(),metalist);
	}		
}
