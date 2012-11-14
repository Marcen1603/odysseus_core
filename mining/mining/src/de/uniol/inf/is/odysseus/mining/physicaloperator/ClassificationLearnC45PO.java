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
import java.util.Collections;
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
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.classification.TreeNode;
import de.uniol.inf.is.odysseus.mining.util.CounterList;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

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
	private Map<SDFAttribute, List<Object>> seenvalues = new HashMap<SDFAttribute, List<Object>>();
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

		init();
		this.clazzPosition = inputSchema.indexOf(classAttribute);
	}

	/**
	 * 
	 */
	private void init() {
		for (SDFAttribute attribute : this.inputSchema) {
			this.seenvalues.put(attribute, new ArrayList<Object>());
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
			init();
			// get all elements from the sweep area that can be processed
			Iterator<Tuple<M>> qualifies = sweepArea.queryElementsStartingBefore(currentTime);
			List<Tuple<M>> pool = new ArrayList<>();
			// look at each tuple and ...
			while (qualifies.hasNext()) {
				Tuple<M> next = qualifies.next();
				// ... read all values for each attribute
				for (int i = 0; i < next.size(); i++) {
					SDFAttribute attribute = this.inputSchema.get(i);
					Object value = next.getAttribute(i);
					// ... and save them
					if (!this.seenvalues.get(attribute).contains(value)) {
						this.seenvalues.get(attribute).add(value);
					}
				}
				// finally, the pool is our current list of tuples that have to
				// be used
				pool.add(next);
			}
			// we need at least one tuple for building a tree
			if (pool.size() > 0) {
				// first, we need to calculate possible split points for
				// continuous valued attributes
				PointInTime totalMin = lastCut;				
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
				meta.setStartAndEnd(totalMin, PointInTime.getInfinityTime());
				newtuple.setMetadata(meta);
				newtuple.setAttribute(0, root);
				// root.printSubTree();
				transfer(newtuple);

				lastCut = currentTime;
				sweepArea.purgeElementsBefore(currentTime);
			}
		}
	}

	private List<Double> calcSplitPoints(SDFAttribute attribute) {
		// we assume that numeric is a continuous valued
		if (attribute.getDatatype().isNumeric()) {
			// take the list of values
			List<Double> values = new ArrayList<Double>();
			for (Object v : this.seenvalues.get(attribute)) {
				Number n = (Number) v;
				values.add(n.doubleValue());
			}
			if (values.size() == 1) {
				return values;
			}
			// sort them
			Collections.sort(values);
			// calculate midpoints
			List<Double> midpoints = new ArrayList<Double>();
			for (int i = 0; i < values.size() - 1; i++) {
				double midpoint = (values.get(i) + values.get(i + 1)) / 2;
				midpoints.add(midpoint);
			}
			return midpoints;
		}
		return new ArrayList<>();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// process_data(timestamp);
		// super.processPunctuation(timestamp, port);

	}

	private void getNextSplit(List<Tuple<M>> pool, List<SDFAttribute> attributesToCheck, TreeNode parent) {
		logger.trace("----------------------------------");
		logger.trace("Check for " + parent.getAttribute());
		ArrayList<SDFAttribute> attributes = new ArrayList<>(attributesToCheck);
		logger.trace("open: " + attributes);
		// first, calculate splitting points for pool and attributes!
		Map<SDFAttribute, List<RelationalPredicate>> splittingPoints = calculateSplittingPoints(pool, attributes);
		// the, find the best split
		SDFAttribute bestSplitAt = getBestSplit(pool, attributes, splittingPoints);
		logger.trace("best: " + bestSplitAt);
		if (bestSplitAt == null) {
			logger.error("something went wrong: attribute for best split cannot be null!");
		}
		attributes.remove(bestSplitAt);
		parent.setAttribute(bestSplitAt);
		// for each possible value of the split attribute...
		for (RelationalPredicate splitPredicate : splittingPoints.get(bestSplitAt)) {
			// ... get only the subset for this value
			List<Tuple<M>> subset = getSubset(pool, bestSplitAt, splitPredicate);
			// if subset size is zero, we do not need this node...
			if (subset.size() > 0) {
				// however, if its not, then create a node for each possible
				// value
				TreeNode node = new TreeNode();
				// and connect it to the parent node
				parent.addChild(splitPredicate, node);
				// check, if all values of the class-attribute are equal...
				if (onlyOneClassLeft(subset)) {
					// ... if so, we can set the class (create a leaf)
					node.setClazz(subset.get(0).getAttribute(clazzPosition));
				} else {
					// ... else find next split, if there are attributes left...
					if (attributes.size() > 0) {
						getNextSplit(subset, attributes, node);
					} else {
						// if there are no attributes left, force a leaf with most frequent class
						node.setClazz(getMostFrequentClass(subset));
					}
				}
			}

		}

	}

	/**
	 * @param subset
	 * @return
	 */
	private Object getMostFrequentClass(List<Tuple<M>> subset) {
		CounterList<Object> counter = new CounterList<>();
		for (Tuple<M> t : subset) {
			counter.count(t.getAttribute(this.clazzPosition));
		}
		return counter.getMostFrequent();
	}

	/**
	 * @param pool
	 * @param attributes
	 * @return
	 */
	private Map<SDFAttribute, List<RelationalPredicate>> calculateSplittingPoints(List<Tuple<M>> pool, ArrayList<SDFAttribute> attributes) {
		Map<SDFAttribute, List<RelationalPredicate>> splittingPoints = new HashMap<>();
		for (SDFAttribute attribute : attributes) {
			List<Object> values = new ArrayList<>();
			int index = this.inputSchema.indexOf(attribute);
			for (Tuple<M> t : pool) {
				Object o = t.getAttribute(index);
				if (!values.contains(o)) {
					values.add(o);
				}
			}
			splittingPoints.put(attribute, new ArrayList<RelationalPredicate>());
			if (attribute.getDatatype().isNumeric()) {
				double bestmidpoint = getBestSplitPointForContinuous(attribute, pool);
				String smallerExprString = attribute.getAttributeName() + " <= " + bestmidpoint;
				SDFExpression smallerExpression = new SDFExpression(smallerExprString, MEP.getInstance());
				RelationalPredicate smallerRelationalPredicate = new RelationalPredicate(smallerExpression);
				smallerRelationalPredicate.init(inputSchema, null);
				splittingPoints.get(attribute).add(smallerRelationalPredicate);

				String greaterExprString = attribute.getAttributeName() + " > " + bestmidpoint;
				SDFExpression greaterExpression = new SDFExpression(greaterExprString, MEP.getInstance());
				RelationalPredicate greaterRelationalPredicate = new RelationalPredicate(greaterExpression);
				greaterRelationalPredicate.init(inputSchema, null);
				splittingPoints.get(attribute).add(greaterRelationalPredicate);
			} else {
				for (Object value : values) {
					String exprString = attribute.getAttributeName() + " == '" + value + "'";
					SDFExpression expression = new SDFExpression(exprString, MEP.getInstance());
					RelationalPredicate relationalPredicate = new RelationalPredicate(expression);
					relationalPredicate.init(inputSchema, null);
					splittingPoints.get(attribute).add(relationalPredicate);
				}
			}

		}
		return splittingPoints;
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

	private List<Tuple<M>> getSubset(List<Tuple<M>> set, SDFAttribute attribute, RelationalPredicate splitPredicate) {
		List<Tuple<M>> subset = new ArrayList<>();
		for (Tuple<M> t : set) {
			if (splitPredicate.evaluate(t)) {
				subset.add(t);
			}
		}
		return subset;
	}

	private SDFAttribute getBestSplit(List<Tuple<M>> pool, List<SDFAttribute> attributes, Map<SDFAttribute, List<RelationalPredicate>> splittingPredicates) {
		double entropyT = entropy(pool);
		logger.trace("entropy(T)=" + entropyT);
		double maxWGain = 0;
		SDFAttribute bestAttribute = null;
		// for each attribute!
		for (SDFAttribute attribute : attributes) {
			logger.trace("checking attribute " + attribute + "...");
			// for each possible value of attribute, calculate the entropy
			double sum = 0;
			for (RelationalPredicate splitPredicate : splittingPredicates.get(attribute)) {
				List<Tuple<M>> subpool = new ArrayList<>();
				for (Tuple<M> t : pool) {
					if (splitPredicate.evaluate(t)) {
						subpool.add(t);
					}
				}
				// calculate entropy
				double ent = entropy(subpool);
				double relh = ((double) subpool.size()) / pool.size();
				sum = sum + (relh * ent);
				logger.trace("Entropy(" + attribute + ", " + splitPredicate + ") = " + ent);
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

	private double getBestSplitPointForContinuous(SDFAttribute attribute, List<Tuple<M>> pool) {
		double bestSplit = 0.0;
		double bestMidpoint = 0.0;
		int attributePosition = this.inputSchema.indexOf(attribute);
		List<Double> midpoints = calcSplitPoints(attribute);
		for (double midpoint : midpoints) {
			List<Tuple<M>> d1 = new ArrayList<>();
			List<Tuple<M>> d2 = new ArrayList<>();
			for (Tuple<M> t : pool) {
				double tupleValue = ((Number) t.getAttribute(attributePosition)).doubleValue();
				if (tupleValue <= midpoint) {
					d1.add(t);
				} else {
					d2.add(t);
				}
			}
			double infoA = ((d1.size() / (double) pool.size()) * entropy(d1)) + ((d2.size() / (double) pool.size()) * entropy(d2));
			if (infoA >= bestSplit) {
				bestSplit = infoA;
				bestMidpoint = midpoint;
			}
		}
		return bestMidpoint;
	}

	private double entropy(List<Tuple<M>> pool) {

		CounterList<Object> counts = new CounterList<>();

		for (Tuple<M> t : pool) {
			Object o = t.getAttribute(this.clazzPosition);
			counts.count(o);
		}
		logger.trace("   entryopy-calculation: total size: " + pool.size());
		double entropy = 0.0;
		for (Object clazz : this.seenvalues.get(classAttribute)) {
			double count = counts.getCount(clazz);
			if (count != 0) {
				logger.trace("   entryopy-calculation: count for " + clazz + ": " + count);
				logger.trace("   entryopy-calculation: count for all: " + counts.getTotalCount());
				double frac = count / counts.getTotalCount();
				logger.trace("   entryopy-calculation: probability: " + frac);
				double e = -1 * frac * (Math.log(frac) / Math.log(2.0));
				logger.trace("   entryopy-calculation: part: " + e);
				entropy += e;
			}
		}
		logger.trace("   entryopy-calculation: result: " + entropy);

		return entropy;
	}

	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new ClassificationLearnC45PO<>(this);
	}

}
