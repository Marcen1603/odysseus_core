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

package de.uniol.inf.is.odysseus.mining.cleaning.correction.stateful;

import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public abstract class AbstractAggregateCorrection implements IBinaryCorrection<RelationalTuple<?>>{

	
	private AggregateFunction aggregateFunction;
	private String attributeName;
	private boolean dataComesFromLeft = false;
	private int attributePosition;
	
	public AbstractAggregateCorrection(String aggregate, String attributeName){
		this.attributeName = attributeName;
		this.aggregateFunction = new AggregateFunction(aggregateFunction);
	}
	

	@Override
	public String getAttribute() {
		return this.attributeName;
	}
	
	public String getAggregationAttribute(){
		return aggregateFunction.getName().toUpperCase()+"("+getAttribute()+")";
	}
	
	protected RelationalTuple<?> replaceAttribute(RelationalTuple<?> tuple, Object value){		
		tuple.setAttribute(attributePosition, value);
		return tuple;		
	}

	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		for(int i=0;i<leftSchema.size();i++){
			if(leftSchema.get(i).getAttributeName().equals(attributeName)){
				this.attributePosition = i;
				this.dataComesFromLeft = true;
				break;
			}
		}			
		for(int i=0;i<rightSchema.size();i++){
			if(rightSchema.get(i).getAttributeName().equals(attributeName)){
				this.attributePosition = i;
				this.dataComesFromLeft = false;
				break;
			}
		}	
		
	}

}
