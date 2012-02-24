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

package de.uniol.inf.is.odysseus.mining.cleaning.correction.stateless;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public abstract class AbstractSimpleRelationalCorrection implements IUnaryCorrection<RelationalTuple<?>> {

	
	private String attributeName;	
	private int attributePosition = -1;

	public AbstractSimpleRelationalCorrection(String attributeName){
		this.attributeName = attributeName;
	}
	
	@Override
	public void init(SDFSchema schema) {			
		for(int i=0;i<schema.size();i++){
			if(schema.get(i).getAttributeName().equals(attributeName)){
				this.attributePosition = i;
				break;
			}
		}		
	}
		
	protected RelationalTuple<?> replaceAttribute(RelationalTuple<?> tuple, Object value){		
		tuple.setAttribute(attributePosition, value);
		return tuple;		
	}
	
	@Override
	public String getAttribute() {		
		return attributeName;
	}
}
