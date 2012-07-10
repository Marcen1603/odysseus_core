/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * 
 * @author Dennis Geesen
 * Created at: 08.07.2011
 */
public abstract class AbstractAggregateDetection implements IBinaryDetection<Tuple<?>>{

	
	private List<AggregateFunction> aggregateFunctions = new ArrayList<AggregateFunction>();
	private String attributeName;
	private RelationalPredicate predicate;
	
	public AbstractAggregateDetection(String attributeName){
		this.attributeName = attributeName;		
	}
	
	public AbstractAggregateDetection(String attributeName, List<AggregateFunction> aggregateFunctions){
		this.attributeName = attributeName;
		this.aggregateFunctions = aggregateFunctions;
	}
	
	protected abstract String buildPredicateStirng();
	
	@Override
	public IPredicate<Tuple<?>> getPredicate() {
		return this.predicate;
	}
	
	protected void addAggregateFunction(AggregateFunction aggFunction){
		this.aggregateFunctions.add(aggFunction);
	}
	
	protected AggregateFunction getAggregateFunction(int i){
		return this.aggregateFunctions.get(i);
	}

	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		this.internalInit(leftSchema, rightSchema);		
		this.predicate.init(leftSchema, rightSchema);
	}

	private void internalInit(SDFSchema leftSchema, SDFSchema rightSchema) {
		try {	
			AttributeResolver attributeResolver = new AttributeResolver();			
			attributeResolver.addAttributes(leftSchema);
			attributeResolver.addAttributes(rightSchema);
			
		
			// build the predicate
			
			String predicateString = buildPredicateStirng();
		
			SDFExpression expression = new SDFExpression("", predicateString, attributeResolver, MEP.getInstance());
			this.predicate = new RelationalPredicate(expression);
		} catch (NoSuchAttributeException ex) {
			System.err.println("Could not found the attribute \"" + ex.getAttribute() + "\" in schema ");
			throw ex;
		}
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}


	public String getAttributeName() {
		return attributeName;
	}
	
	@Override
	public String getAttribute() {
		return attributeName;
	}
	
	public String getAggregationAttribute(int i){
		return getAggregateFunction(i).getName().toUpperCase()+"("+getAttributeName()+")";
	}
	
	public String getAggregationAttribute(){
		String del = "";
		String attributeNames = "";
		for(int i=0;i<this.aggregateFunctions.size(); i++){
			attributeNames = attributeNames+del+getAggregationAttribute(i);
		}
		return attributeNames;
	}
	
	public List<AggregateFunction> getAggregateFunctions(){
		return this.aggregateFunctions;
	}
	
	public List<String> getAggregateAttributes(){
		List<String> names = new ArrayList<String>();
		for(int i=0;i<this.aggregateFunctions.size();i++){
			names.add(getAggregationAttribute(i));
		}
		return names;
	}	

}
