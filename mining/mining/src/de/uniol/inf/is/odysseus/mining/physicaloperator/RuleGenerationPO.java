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
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.Pattern;

/**
 * @author Dennis Geesen
 * 
 */
public class RuleGenerationPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	private int itemposition = -1;
	private double minconfidence = 0.9d;

	public RuleGenerationPO(int itemposition, double confidence) {
		this.itemposition = itemposition;
		this.minconfidence = confidence;
	}

	/**
	 * @param ruleGenerationPO
	 */
	public RuleGenerationPO(RuleGenerationPO<M> ruleGenerationPO) {
		this.itemposition = ruleGenerationPO.itemposition;
		this.minconfidence = ruleGenerationPO.minconfidence;
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
		Pattern<M> o = element.getAttribute(itemposition);
		System.out.println("--------------------------------------------------------------");
		System.out.println(o);
		if (o.length() >= 2) {
			List<Pattern<M>> oneConsequences = o.splitIntoSinglePatterns();
			generateRules(o, oneConsequences);
		}
	}

	private void generateRules(Pattern<M> frequentitemset, List<Pattern<M>> consequences) {
		
		if(consequences.size() == 0){
			return;
		}
		
		if(frequentitemset.length()> consequences.get(0).length()){
			
			Iterator<Pattern<M>> iterHigherCons = consequences.iterator();
			while(iterHigherCons.hasNext()){
				Pattern<M> consequence = iterHigherCons.next();
				Pattern<M> premise = frequentitemset.substract(consequence);
				double conf = frequentitemset.getSupport() / premise.getSupport();
				if(conf>=minconfidence){
					System.out.println("FOUND: "+premise+" => "+consequence);
				}else{
					iterHigherCons.remove();
				}			
			}
			List<Pattern<M>> higherConsequences = generateSuperset(consequences);
			generateRules(frequentitemset, higherConsequences);			
		}					
	}
	
	
	
	private List<Pattern<M>> generateSuperset(List<Pattern<M>> itemsets) {
		if(itemsets.isEmpty()){
			return new ArrayList<Pattern<M>>();
		}
		List<Pattern<M>> result = new ArrayList<Pattern<M>>();		
		for (int i = 0; i < itemsets.size() - 1; i++) {
			for (int j = i + 1; j < itemsets.size(); j++) {				
				Pattern<M> combined = new Pattern<M>(itemsets.get(i));
				Pattern<M> other = itemsets.get(j).clone();
				if(!combined.overlapsExceptForOne(other)){
					break;
				}else{
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
		// TODO Auto-generated method stub

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

}
