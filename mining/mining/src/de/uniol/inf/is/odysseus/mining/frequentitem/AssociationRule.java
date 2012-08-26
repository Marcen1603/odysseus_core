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
package de.uniol.inf.is.odysseus.mining.frequentitem;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth.Pattern;

/**
 * @author Dennis Geesen
 *
 */
public class AssociationRule<M extends ITimeInterval> {

	private Pattern<M> premise;
	private Pattern<M> consequence;	
	private double confidence;

	/**
	 * @param premise
	 * @param consequence
	 * @param support
	 * @param confidence
	 */
	public AssociationRule(Pattern<M> premise, Pattern<M> consequence, double confidence) {
		this.setPremise(premise);
		this.setConsequence(consequence);		
		this.setConfidence(confidence);
	}

	/**
	 * @return the premise
	 */
	public Pattern<M> getPremise() {
		return premise;
	}

	/**
	 * @param premise the premise to set
	 */
	public void setPremise(Pattern<M> premise) {
		this.premise = premise;
	}

	/**
	 * @return the consequence
	 */
	public Pattern<M> getConsequence() {
		return consequence;
	}

	/**
	 * @param consequence the consequence to set
	 */
	public void setConsequence(Pattern<M> consequence) {
		this.consequence = consequence;
	}	

	/**
	 * @return the confidence
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.premise+" => "+this.consequence+" ("+this.confidence+")";		
	}
	

}
