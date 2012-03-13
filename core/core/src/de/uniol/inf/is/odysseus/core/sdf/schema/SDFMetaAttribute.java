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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class SDFMetaAttribute extends SDFAttribute {
	
	private static final long serialVersionUID = 8656969628309815890L;
	private Class<? extends IMetaAttribute> theclass; 
	
	
	public SDFMetaAttribute(Class<? extends IMetaAttribute> metaAttributeClass) {
		super(null,metaAttributeClass.getName(), SDFDatatype.OBJECT);	
		this.theclass = metaAttributeClass;
	}


	public Class<? extends IMetaAttribute> getMetaAttributeClass() {
		return theclass;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SDFMetaAttribute){
			SDFMetaAttribute other = (SDFMetaAttribute)obj;
			if(other.getMetaAttributeClass().equals(this.getMetaAttributeClass())){
				return true;
			}
			return false;
		}
        return false;		
	}
	
	@Override
	public int hashCode() {
		return theclass.hashCode();
	}
			

}
