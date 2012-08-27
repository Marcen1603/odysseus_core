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
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.frequentitem.AssociationRule;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.FPTree;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.Pattern;

/**
 * @author Dennis Geesen
 * 
 */
public class RuleGenerationPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private int itemposition = -1;
	private int supportposition = -1;
	private double minconfidence = 0.9d;
	private DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private int counter = 0;

	public RuleGenerationPO(int itemposition, int supportposition, double confidence) {
		this.itemposition = itemposition;
		this.minconfidence = confidence;
		this.supportposition = supportposition;
	}

	/**
	 * @param ruleGenerationPO
	 */
	public RuleGenerationPO(RuleGenerationPO<M> ruleGenerationPO) {
		this.itemposition = ruleGenerationPO.itemposition;
		this.minconfidence = ruleGenerationPO.minconfidence;
		this.supportposition = ruleGenerationPO.supportposition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(java.lang.Object, int)
	 */
	@Override
	protected void process_next(Tuple<M> element, int port) {
//		System.out.println("--------------------------------------------------------------");
//		System.out.println("New element: " + element);
		// System.out.println("SA:");
		// System.out.println(this.sweepArea.getSweepAreaAsString());
		// System.out.println("--------------------------------------------------------------");

		processData(element.getMetadata().getStart());

		// if we have already this set, we use the newer one
		Iterator<Tuple<M>> qualified = this.sweepArea.queryOverlaps(element.getMetadata());
		while (qualified.hasNext()) {
			Tuple<M> nextOne = qualified.next();
			List<Tuple<M>> tuples = nextOne.getAttribute(itemposition);
			List<Tuple<M>> newOneTuples = element.getAttribute(itemposition);
			if (tuples.containsAll(newOneTuples)) {
				this.sweepArea.remove(nextOne);
			}
		}

		this.sweepArea.insert(element);
	}

	/**
	 * @param start
	 */
	private synchronized void processData(PointInTime start) {
		counter = 0;
		if (this.sweepArea.getMaxTs() != null && start.after(this.sweepArea.getMaxTs())) {

			Iterator<Tuple<M>> qualified = this.sweepArea.queryElementsStartingBefore(start);

			FPTree<M> tree = new FPTree<M>();
			synchronized (sweepArea) {
				while (qualified.hasNext()) {
					Tuple<M> next = qualified.next();
					List<Tuple<M>> tuples = next.getAttribute(itemposition);
					int support = next.getAttribute(supportposition);
					Pattern<M> pattern = new Pattern<M>(tuples, support);
					tree.insertTree(pattern.clone());
				}
				qualified = this.sweepArea.extractElementsStartingBefore(start);
				while (qualified.hasNext()) {
					Tuple<M> next = qualified.next();
					List<Tuple<M>> tuples = next.getAttribute(itemposition);
					int support = next.getAttribute(supportposition);
					Pattern<M> o = new Pattern<M>(tuples, support);
					generateRuleForItemSet(o.clone(), tree, start);
				}
			}
		}

	}

	private void generateRuleForItemSet(Pattern<M> o, FPTree<M> tree, PointInTime start) {
		if (o.length() >= 2) {
			List<Pattern<M>> oneConsequences = o.splitIntoSinglePatterns();
			generateRules(o, oneConsequences, tree, start);
		}
	}

	private void generateRules(Pattern<M> frequentitemset, List<Pattern<M>> consequences, FPTree<M> tree, PointInTime start) {

		if (consequences.size() == 0) {
			return;
		}

		if (frequentitemset.length() > consequences.get(0).length()) {

			Iterator<Pattern<M>> iterHigherCons = consequences.iterator();
			while (iterHigherCons.hasNext()) {
				Pattern<M> consequence = iterHigherCons.next();
				Pattern<M> premise = frequentitemset.substract(consequence);
				premise.setSupport(tree.getSupport(premise));
				consequence.setSupport(tree.getSupport(consequence));
				frequentitemset.setSupport(tree.getSupport(frequentitemset));
				double conf = frequentitemset.getSupport() / (double) premise.getSupport();
				// System.out.println("FOUND: \t" + premise + " => " +
				// consequence + "(conf=" + conf + ")");
				if (conf >= minconfidence) {
					// System.out.println("OUT: \t" + premise + " => " +
					// consequence + "(conf=" + conf + ")");
					Tuple<M> newtuple = new Tuple<M>(2, false);
					@SuppressWarnings("unchecked")
					M metadata = (M) frequentitemset.getMetadata().clone();
					newtuple.setMetadata((M) metadata);
					newtuple.getMetadata().setStart(frequentitemset.getMinTime());
					newtuple.getMetadata().setEnd(start);
					newtuple.setAttribute(0, counter);
					AssociationRule<M> rule = new AssociationRule<M>(premise, consequence, conf);
					newtuple.setAttribute(1, rule);
					counter++;					
					transfer(newtuple);
				} else {
					iterHigherCons.remove();
				}
			}
			List<Pattern<M>> higherConsequences = generateSuperset(consequences);
			generateRules(frequentitemset, higherConsequences, tree, start);
		}
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
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * processPunctuation(de.uniol.inf.is.odysseus.core.metadata.PointInTime,
	 * int)
	 */

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		processData(timestamp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone
	 * ()
	 */
	@Override
	public RuleGenerationPO<M> clone() {
		return new RuleGenerationPO<M>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * isSemanticallyEqual
	 * (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RuleGenerationPO)) {
			return false;
		}
		if (this.hasSameSources(ipo)) {
			RuleGenerationPO<?> po = (RuleGenerationPO<?>) ipo;
			if (this.itemposition == po.itemposition && this.minconfidence == po.minconfidence) {
				return true;
			}
		}
		return false;
	}
}
