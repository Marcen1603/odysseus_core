/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.cleaning.detection.stateful;

import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;

/**
 * 
 * @author Dennis Geesen
 * Created at: 23.06.2011
 */
public class SigmaRuleDetection extends AbstractAggregateDetection{

	private int sigma;
	
	public SigmaRuleDetection(String attributeName, int sigma) {
		super(attributeName);
		super.addAggregateFunction(new AggregateFunction("STDEV"));
		super.addAggregateFunction(new AggregateFunction("AVG"));
		this.sigma = sigma;
	}

	@Override
	protected String buildPredicateStirng() {		
		String p = "abs( ("+getAttributeName()+"- "+getAggregationAttribute(1)+")/"+getAggregationAttribute(0)+") > "+this.sigma;
		return p;
	}

	

}
