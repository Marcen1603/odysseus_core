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
package de.uniol.inf.is.odysseus.mining.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.util.CounterList;

/**
 * @author Dennis Geesen
 * 
 */
public class ClassificationLearnC45PO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private SDFAttribute classAttribute;
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();

	private SDFSchema inputSchema;
	private Map<SDFAttribute, List<Object>> partitions = new HashMap<SDFAttribute, List<Object>>();
	private int clazzPosition;

	/**
	 * @param classAttribute
	 */
	public ClassificationLearnC45PO(SDFAttribute classAttribute, SDFSchema inputschema) {
		this.classAttribute = classAttribute;
		this.inputSchema = inputschema;
	}

	public ClassificationLearnC45PO(ClassificationLearnC45PO<M> old) {
		this.classAttribute = old.classAttribute;
		this.inputSchema = old.inputSchema;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();

		for (SDFAttribute attribute : this.inputSchema) {
			this.partitions.put(attribute, new ArrayList<Object>());
		}
		this.clazzPosition = inputSchema.indexOf(classAttribute);
	}

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		PointInTime currentTime = object.getMetadata().getStart();
		Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(currentTime);
		List<Tuple<M>> pool = new ArrayList<>();
		while (qualifies.hasNext()) {
			Tuple<M> next = qualifies.next();
			for (int i = 0; i < object.size(); i++) {
				SDFAttribute attribute = this.inputSchema.get(i);
				Object value = next.getAttribute(i);
				if (!this.partitions.get(attribute).contains(value)) {
					this.partitions.get(attribute).add(value);
				}
			}
			pool.add(next);
		}
		if (pool.size() > 0) {
			List<SDFAttribute> allAttributes = new ArrayList<>();
			List<SDFAttribute> splitOrder = new ArrayList<>();
			for (SDFAttribute attribute : inputSchema) {
				if(!attribute.equals(classAttribute)){
					allAttributes.add(attribute);
				}
			}
			while(!allAttributes.isEmpty()){
				SDFAttribute bestSplitAt = getBestSplit(pool, allAttributes);
				System.out.println("BEST SPLIT: "+bestSplitAt);
				splitOrder.add(bestSplitAt);
				allAttributes.remove(bestSplitAt);
			}
			System.out.println("Best split order is: ");
			for(SDFAttribute a : splitOrder){
				System.out.println(a);
			}
			
		}
		sweepArea.insert(object);

	}

	
	private SDFAttribute getBestSplit(List<Tuple<M>> pool, List<SDFAttribute> attributes){
		double entropyT = entropy(pool);
		System.out.println("entropy(T)=" + entropyT);		
		double maxWGain = 0;
		SDFAttribute bestAttribute = null; 
		// for each attribute!
		for (SDFAttribute attribute : attributes) {				
			int position = this.inputSchema.indexOf(attribute);
			// for each possible value of attribute, calculate the entropy
			double sum = 0;
			for (Object value : this.partitions.get(attribute)) {
				List<Tuple<M>> subpool = new ArrayList<>();
				for (Tuple<M> t : pool) {
					if (t.getAttribute(position).equals(value)) {
						subpool.add(t);
					}
				}
				// calc entropy
				double ent = entropy(subpool);
				double relh = ((double)subpool.size())/pool.size();
				sum = sum + (relh*ent);
				System.out.println("Entropy(" + attribute + ", " + value + ") = " + ent);
			}
			double wgain = entropyT - sum;
			System.out.println("wgain = (" + attribute + ") = " + wgain);
			if(wgain>maxWGain){
				maxWGain = wgain;
				bestAttribute = attribute;
			}
		}
		return bestAttribute;
		
	}
	
	
	private double entropy(List<Tuple<M>> pool) {

		CounterList<Object> counts = new CounterList<>();

		for (Tuple<M> t : pool) {
			Object o = t.getAttribute(this.clazzPosition);
			counts.count(o);
		}

		double entropy = 0.0;
		for (Object clazz : this.partitions.get(classAttribute)) {
			double count = counts.getCount(clazz);
			if (count != 0) {
				double frac = count / counts.getTotalCount();
				double e = -1 * frac * (Math.log(frac) / Math.log(2.0));
				entropy += e;
			}
		}

		return entropy;
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new ClassificationLearnC45PO<>(this);
	}

}
