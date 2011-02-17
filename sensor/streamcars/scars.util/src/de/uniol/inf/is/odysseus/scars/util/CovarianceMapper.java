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
package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class CovarianceMapper {

	private List<String> indices = new ArrayList<String>();
	
	public CovarianceMapper(CovarianceMapper copy) {
		this.indices = new ArrayList<String>(copy.indices);
	}
	
	public CovarianceMapper( SDFAttributeList schema ) {
		find(schema, null);
	}
	
	private void find(SDFAttributeList list, String fullAttributeName ) {
		String lastName = fullAttributeName;

		for( SDFAttribute attribute : list ) {
			
			if( lastName == null ) 
				lastName = attribute.getSourceName() + "." + attribute.getAttributeName();
			else
				lastName = fullAttributeName + ":" + attribute.getAttributeName();
			
			if( SDFDatatypes.isMeasurementValue(attribute.getDatatype())) {
				indices.add(lastName);
			}
			find(attribute.getSubattributes(), lastName);
		}
	}
	
	public int getSize() {
		return indices.size();
	}
	
	public int getCovarianceIndex( String fullAttributeName ) {
		return indices.indexOf(fullAttributeName);
	}
	
	public String getAttributeName( int index ) {
		return indices.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		for( int i = 0; i < getSize(); i++ ) {
			sb.append(i).append(" -> ").append(getAttributeName(i));
			if(i < getSize() - 1)
				sb.append(", ");
		}
		sb.append("}");
		
		return sb.toString();
	}
	
	public List<String> getMapping() {
		return indices;
	}
}
