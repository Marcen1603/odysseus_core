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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.classification.TreeNode;
import de.uniol.inf.is.odysseus.mining.util.CounterList;

/**
 * @author Dennis Geesen
 * 
 */
public class ClassificationLearnC45PO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private static Logger logger = LoggerFactory.getLogger(ClassificationLearnC45PO.class);

	private SDFAttribute classAttribute;
	private IMetadataMergeFunction<M> metadatamergefunction;
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();

	private SDFSchema inputSchema;
	private Map<SDFAttribute, List<Object>> partitions = new HashMap<SDFAttribute, List<Object>>();
	private int clazzPosition;

	private PointInTime lastCut = PointInTime.getZeroTime();

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
		this.metadatamergefunction = old.metadatamergefunction.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();

		initPartitions();
		this.clazzPosition = inputSchema.indexOf(classAttribute);
	}

	/**
	 * 
	 */
	private void initPartitions() {
		for (SDFAttribute attribute : this.inputSchema) {
			this.partitions.put(attribute, new ArrayList<Object>());
		}		
	}

	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		PointInTime currentTime = object.getMetadata().getStart();
		process_data(currentTime);
		sweepArea.insert(object);
	}

	private synchronized void process_data(PointInTime currentTime) {
		if (currentTime.after(lastCut)) {
			initPartitions();
			Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(currentTime);
			List<Tuple<M>> pool = new ArrayList<>();
			while (qualifies.hasNext()) {
				Tuple<M> next = qualifies.next();
				for (int i = 0; i < next.size(); i++) {
					SDFAttribute attribute = this.inputSchema.get(i);
					Object value = next.getAttribute(i);
					if (!this.partitions.get(attribute).contains(value)) {
						this.partitions.get(attribute).add(value);
					}
				}
				pool.add(next);
			}
			if (pool.size() > 0) {
				PointInTime totalMin = lastCut;
				PointInTime totalMax = currentTime;
				// fill all attributes - without class-attribute
				List<SDFAttribute> allAttributes = new ArrayList<>();
				for (SDFAttribute attribute : inputSchema) {
					if (!attribute.equals(classAttribute)) {
						allAttributes.add(attribute);
					}
				}
				// get the best split for the root
				TreeNode root = new TreeNode();
				getNextSplit(pool, allAttributes, root);
				logger.trace("Best split order is: ");
				

				Tuple<M> newtuple = new Tuple<M>(1, false);
				@SuppressWarnings("unchecked")
				M meta = (M) pool.get(pool.size() - 1).getMetadata().clone();
				meta.setStartAndEnd(totalMin, totalMax);
				newtuple.setMetadata(meta);
				newtuple.setAttribute(0, root);
				root.printSubTree();
				transfer(newtuple);
				
				lastCut = currentTime;
				sweepArea.purgeElementsBefore(currentTime);
			}
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		process_data(timestamp);
		super.processPunctuation(timestamp, port);

	}

	private void getNextSplit(List<Tuple<M>> pool, List<SDFAttribute> attributesToCheck, TreeNode parent) {
		logger.trace("----------------------------------");
		logger.trace("Check for " + parent.getAttribute());

		ArrayList<SDFAttribute> attributes = new ArrayList<>(attributesToCheck);
		logger.trace("open: " + attributes);
		SDFAttribute bestSplitAt = getBestSplit(pool, attributes);
		logger.trace("best: " + bestSplitAt);
		if(bestSplitAt==null){
			System.out.println("SPLIT IS NULL?!");
		}
		attributes.remove(bestSplitAt);
		parent.setAttribute(bestSplitAt);
		// for each possible value of the split attribute...
		for (Object value : this.partitions.get(bestSplitAt)) {
			// ... get only the subset for this value
			List<Tuple<M>> subset = getSubset(pool, bestSplitAt, value);
			// and create a node for each possible value
			TreeNode node = new TreeNode();

			parent.addChild(value, node);
			// check, if all values of the class-attribute are equal...
			if (onlyOneClassLeft(subset)) {
				if (subset.size() > 0) {
					node.setClazz(subset.get(0).getAttribute(clazzPosition));
				}else{
					System.out.println("SHOULD NOT HAPPEN");
				}
			} else {
				// ... else find next split
				getNextSplit(subset, attributes, node);
			}

		}
	}

	/**
	 * @param subset
	 * @return
	 */
	private boolean onlyOneClassLeft(List<Tuple<M>> subset) {
		Object found = null;
		for (Tuple<M> t : subset) {
			Object clazz = t.getAttribute(clazzPosition);
			if (found == null) {
				found = clazz;
			} else if (!found.equals(clazz)) {
				return false;
			}

		}
		return true;
	}

	private List<Tuple<M>> getSubset(List<Tuple<M>> set, SDFAttribute attribute, Object value) {
		List<Tuple<M>> subset = new ArrayList<>();
		int index = this.inputSchema.indexOf(attribute);
		for (Tuple<M> t : set) {
			if (t.getAttribute(index).equals(value)) {
				subset.add(t);
			}
		}
		return subset;
	}

	private SDFAttribute getBestSplit(List<Tuple<M>> pool, List<SDFAttribute> attributes) {
		double entropyT = entropy(pool);
		logger.trace("entropy(T)=" + entropyT);
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
				double relh = ((double) subpool.size()) / pool.size();
				sum = sum + (relh * ent);
				logger.trace("Entropy(" + attribute + ", " + value + ") = " + ent);
			}
			double wgain = entropyT - sum;
			logger.trace("wgain = (" + attribute + ") = " + wgain);
			if (wgain >= maxWGain) {
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
