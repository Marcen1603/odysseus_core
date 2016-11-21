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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.frequentitem.AssociationRule;
import de.uniol.inf.is.odysseus.mining.frequentitem.Pattern;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * @author Dennis Geesen
 *
 */
public class RuleGenerationPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private int itemposition = -1;
	private int supportposition = -1;
	private double minconfidence = 0.9d;
	final private ITimeIntervalSweepArea<Tuple<M>> sweepArea;
	private FastArrayList<PointInTime> points = new FastArrayList<PointInTime>();

	public RuleGenerationPO(int itemposition, int supportposition, double confidence, ITimeIntervalSweepArea<Tuple<M>> sweepArea) {
		this.itemposition = itemposition;
		this.minconfidence = confidence;
		this.supportposition = supportposition;
		this.sweepArea = sweepArea;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# process_next(java.lang.Object, int)
	 */
	@Override
	protected void process_next(Tuple<M> element, int port) {
		if (!this.points.contains(element.getMetadata().getStart())) {
			this.points.add(element.getMetadata().getStart());
		}
		if (!this.points.contains(element.getMetadata().getEnd())) {
			this.points.add(element.getMetadata().getEnd());
		}
		Collections.sort(this.points);
		sweepArea.insert(element);
		int removeTill = 0;
		for (int i = 1; i < this.points.size(); i++) {
			PointInTime endP = this.points.get(i);
			PointInTime startP = this.points.get(i - 1);
			if (endP.beforeOrEquals(element.getMetadata().getStart())) {
				synchronized (this.sweepArea) {
					TimeInterval ti = new TimeInterval(startP, endP);
					List<Tuple<M>> qualifies = this.sweepArea.queryOverlapsAsList(ti);
					Collection<AssociationRule<M>> rules = processElements(qualifies);
					for (AssociationRule<M> rule : rules) {

						Tuple<M> newtuple = new Tuple<M>(1, false);
						newtuple.setAttribute(0, rule);
						@SuppressWarnings("unchecked")
						M metadata = (M) element.getMetadata().clone();
						newtuple.setMetadata(metadata);
						newtuple.getMetadata().setStartAndEnd(startP, endP);
						transfer(newtuple);

					}
					removeTill = i;
				}
			} else {
				break;
			}
		}
		if (removeTill != 0) {
			this.points.removeRange(0, removeTill);
			sweepArea.purgeElementsBefore(element.getMetadata().getStart());
		}
	}

	private Collection<AssociationRule<M>> processElements(List<Tuple<M>> qualifies) {
		Collection<AssociationRule<M>> rules = new HashSet<>();
		synchronized (sweepArea) {
			// set up the tree
			for (Tuple<M> next : qualifies) {
				List<Tuple<M>> tuples = next.getAttribute(itemposition);
				int support = next.getAttribute(supportposition);
				Pattern<M> p = new Pattern<M>(tuples, support);
				rules.addAll(generateRuleForItemSet(p.clone()));
			}
		}
		return rules;
	}

	private Collection<AssociationRule<M>> generateRuleForItemSet(Pattern<M> o) {
		Collection<AssociationRule<M>> rules = new HashSet<>();
		if (o.length() >= 2) {
			List<Pattern<M>> oneConsequences = o.splitIntoSinglePatterns();
			rules.addAll(generateRules(o, oneConsequences));
		}
		return rules;
	}


	private int getSupport(Pattern<M> pattern){
		Iterator<Tuple<M>> iter = this.sweepArea.iterator();
		List<Tuple<M>> patternTuples = pattern.getPattern();
		while(iter.hasNext()){
			Tuple<M> next = iter.next();
			List<Tuple<M>> tuples = next.getAttribute(itemposition);
			if(tuples.size()==patternTuples.size()){
				if(tuples.containsAll(patternTuples)){
					return next.getAttribute(supportposition);
				}
			}
		}
		return 0;

	}

	private Collection<? extends AssociationRule<M>> generateRules(Pattern<M> frequentitemset, List<Pattern<M>> consequences) {
		Collection<AssociationRule<M>> rules = new HashSet<>();
		if (consequences.size() == 0) {
			return new HashSet<>();
		}

		if (frequentitemset.length() > consequences.get(0).length()) {

			Iterator<Pattern<M>> iterHigherCons = consequences.iterator();
			while (iterHigherCons.hasNext()) {
				Pattern<M> consequence = iterHigherCons.next();
				Pattern<M> premise = frequentitemset.substract(consequence);
				premise.setSupport(getSupport(premise));
				consequence.setSupport(getSupport(consequence));
				frequentitemset.setSupport(getSupport(frequentitemset));
				double conf = frequentitemset.getSupport() / (double) premise.getSupport();
				if (conf >= minconfidence) {
					AssociationRule<M> rule = new AssociationRule<M>(premise, consequence, conf);
					rules.add(rule);
				} else {
					iterHigherCons.remove();
				}
			}
			List<Pattern<M>> higherConsequences = generateSuperset(consequences);
			rules.addAll(generateRules(frequentitemset, higherConsequences));
		}
		return rules;
	}

	private List<Pattern<M>> generateSuperset(List<Pattern<M>> itemsets) {
		if (itemsets.isEmpty()) {
			return new ArrayList<Pattern<M>>();
		}
		List<Pattern<M>> result = new ArrayList<Pattern<M>>();
		for (int i = 0; i < itemsets.size() - 1; i++) {
			for (int j = i + 1; j < itemsets.size(); j++) {
				Pattern<M> combined = new Pattern<M>(itemsets.get(i));
				Pattern<M> other = itemsets.get(j).clone();
				if (!combined.overlapsExceptForOne(other)) {
					break;
				} else {
					combined.combine(other);
					result.add(combined);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# processPunctuation(de.uniol.inf.is.odysseus.core.metadata.PointInTime, int)
	 */

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// processData(punctuation.getTime());
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# isSemanticallyEqual (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RuleGenerationPO)) {
			return false;
		}
		RuleGenerationPO<?> po = (RuleGenerationPO<?>) ipo;
		if (this.itemposition == po.itemposition && this.minconfidence == po.minconfidence) {
			return true;
		} else {
			return false;
		}
	}
}
