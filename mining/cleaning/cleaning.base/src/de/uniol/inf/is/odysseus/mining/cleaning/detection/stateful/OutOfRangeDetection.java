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
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * 
 * @author Dennis Geesen
 * Created at: 23.06.2011
 */
public class OutOfRangeDetection implements IBinaryDetection<RelationalTuple<?>>{
	
	private double abweichung;
	private boolean inPercent;	
	private RelationalPredicate predicate;
	private String attributeName;
	private AggregateFunction aggFunction = new AggregateFunction("AVG");
	
	public OutOfRangeDetection(String attributeName, double abweichung, boolean inPercent) {		
		this.attributeName = attributeName;
		this.abweichung = abweichung;			
		this.inPercent = inPercent;		
	}

	@Override
	public IPredicate<RelationalTuple<?>> getPredicate() {
		return this.predicate;
	}

	@Override
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.internalInit(leftSchema, rightSchema);		
		this.predicate.init(leftSchema, rightSchema);
	}

	private void internalInit(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		try {	
			AttributeResolver attributeResolver = new AttributeResolver();			
			attributeResolver.addAttributes(leftSchema);
			attributeResolver.addAttributes(rightSchema);
			
		
			// build the predicate
			
			String predicateString = buildPredicateStirng();
		
			SDFExpression expression = new SDFExpression("", predicateString, attributeResolver);
			this.predicate = new RelationalPredicate(expression);
		} catch (NoSuchAttributeException ex) {
			System.err.println("Could not found the attribute \"" + ex.getAttribute() + "\" in schema ");
			throw ex;
		}
	}
	
	public String getAggregationAttribute(){
		return aggFunction.getName().toUpperCase()+"("+attributeName+")";
	}
	
	public AggregateFunction getAggregateFunction(){
		return aggFunction;
	}
	
	private String buildPredicateStirng(){
		String aggAttribute = getAggregationAttribute();
		if(this.inPercent){
			double percent = this.abweichung/100.0;
			String p = attributeName+" < ( (1.0-"+percent+") * "+aggAttribute+")";
			p = p+" || "+attributeName+" > ( (1.0+"+percent+") * "+aggAttribute+")";		
			return p;
		}else{
			String p = attributeName+"+"+this.abweichung+"<"+aggAttribute;
			p = p+" || "+attributeName+"-"+this.abweichung+">"+aggAttribute;		
			return p;
		}
	}	

}
