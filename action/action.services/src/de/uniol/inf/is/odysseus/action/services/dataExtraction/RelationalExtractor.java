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
package de.uniol.inf.is.odysseus.action.services.dataExtraction;

import de.uniol.inf.is.odysseus.action.services.exception.DataextractionException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Extractor for relational tuples.
 * Identifiers must be the index of the attribute. 
 * @author Simon Flandergan
 *
 */
public class RelationalExtractor implements IAttributeExtractor {

	@SuppressWarnings("unchecked")
	@Override
	public Object extractAttribute(Object identifier, Object datastreamElement) throws DataextractionException {
		Object value = null;
		try {
			//check if identifier is an index
			int index = ((Number)identifier).intValue();
			value = ((RelationalTuple)datastreamElement).getAttribute(index);
		}catch (ClassCastException e){
			throw new DataextractionException(e.getMessage());
		}
		return value;
	}

	@Override
	public String getName() {
		return "relational";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object extractAttribute(Object attributeIdentifier, Object element,
			SDFAttributeList schema) throws DataextractionException {
		for (int i=0; i<schema.getAttributeCount(); i++){
			SDFAttribute attribute = schema.get(i);
			//check for uri since it is unique
			try {
				if (attribute.getPointURI().equals(attributeIdentifier)){
					return ((RelationalTuple)element).getAttribute(i);
				}
			}catch (ClassCastException e){
				throw new DataextractionException(e.getMessage());
			}
		}
		throw new DataextractionException("Attribute not found");
	}
}
