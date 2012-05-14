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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;

/**
 * 
 * @author Dennis Geesen
 * Created at: 08.07.2011
 */
public class SimpleDeviationDetection extends AbstractSimpleAggregateDetection{

	private boolean inPercent;
	private double abweichung;

	public SimpleDeviationDetection(String attributeName, double abweichung, boolean inPercent, AggregateFunction aggFunction) {
		super(attributeName, aggFunction);
		this.inPercent = inPercent;
		this.abweichung = abweichung;
	}

	@Override
	protected String buildPredicateStirng(){		
		if(this.inPercent){
			double percent = this.abweichung/100.0;
			String p = getAttributeName()+" < ( (1.0-"+percent+") * "+getAggregationAttribute()+")";
			p = p+" || "+getAttributeName()+" > ( (1.0+"+percent+") * "+getAggregationAttribute()+")";		
			return p;
		}
        String p = getAttributeName()+"+"+this.abweichung+"<"+getAggregationAttribute();
        p = p+" || "+getAttributeName()+"-"+this.abweichung+">"+getAggregationAttribute();		
        return p;
	}
}
