/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.broker.metric;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;


public class MetricMeasureAO extends AbstractLogicalOperator{

	private static final long serialVersionUID = 4253188596531819983L;
	/** The queue schema. */
	private SDFSchema schema = null;
	private String onAttribute;	

	public MetricMeasureAO(String onAttribute){
		this.onAttribute = onAttribute;
	}
	
	public MetricMeasureAO(MetricMeasureAO original){
		this.onAttribute = original.onAttribute;
		this.schema = original.schema;
	}
	
	@Override
	public SDFSchema getOutputSchema() {
		return this.schema;
	}
	
	public void setOutputSchema(SDFSchema schema){
		this.schema = schema;
	}		

	public int getOnAttribute() {
		if(this.schema==null){
			return -1;
		}else{
			for(int i=0;i<this.schema.size();i++){			
				if(this.schema.getAttribute(i).getAttributeName().equals(this.onAttribute)){
					return i;
				}
			}
		}
		return -1;		
	}			
		
	@Override
	public MetricMeasureAO clone() {	
		return new MetricMeasureAO(this);
	}
}
