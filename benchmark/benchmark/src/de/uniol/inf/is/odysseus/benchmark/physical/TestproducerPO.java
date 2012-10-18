/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.benchmark.physical;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.interval_latency.IntervalLatency;

public class TestproducerPO extends
		AbstractSource<Tuple<IntervalLatency>>{

	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private long delay;
	
	public TestproducerPO(long delay) {
		this.delay = delay;
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
				delayStart(delay);
				long lastTime = System.nanoTime();
				for (int j = 0; j < elementCounts.size(); ++j) {
					Integer count = elementCounts.get(j);
					Long frequency = frequencies.get(j);
					long offset = 1000000000 / frequency;
					for (int i = 0; i < count; ++i) {
						Tuple<IntervalLatency> r = new Tuple<IntervalLatency>(
								1, false);
						r.setAttribute(0, i);
						IntervalLatency meta = new IntervalLatency();
						long expectedTime = lastTime + offset;
						meta.setLatencyStart(expectedTime);
						r.setMetadata(meta);
						if( offset < 1000000) {
							while (expectedTime > System.nanoTime()) {}
						} else {
							waitSomeTime(offset);
						}
						lastTime = expectedTime;
						transfer(r);
					}
				}
				propagateDone();
			}


		};
		t.setPriority(7); // 7 is over normal
		t.setDaemon(true);
		t.start();
	}
	
	private static void waitSomeTime(long offset) {
		try {
			Thread.sleep(offset / 1000000);
		} catch (InterruptedException ex) {}
	}

	@Override
	public TestproducerPO clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		List<SDFMetaAttribute> metalist = new ArrayList<SDFMetaAttribute>(super.getMetaAttributeSchema().getAttributes());
		SDFMetaAttribute mataAttribute = new SDFMetaAttribute(IntervalLatency.class);
		if(!metalist.contains(mataAttribute)){
			metalist.add(mataAttribute);
		}
		return new SDFMetaAttributeList(super.getMetaAttributeSchema().getURI(), metalist);
	}

	private static void delayStart(long delay) {
		if( delay > 0 ) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException ex) {}
		}
	}	
}
