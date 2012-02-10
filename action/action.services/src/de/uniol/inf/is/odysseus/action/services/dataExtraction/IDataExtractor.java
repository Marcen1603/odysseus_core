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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * Interface describing the OSGI Service for the access to data stream elements
 * @author Simon Flandergan
 *
 */
public interface IDataExtractor {
	
	/**
	 * Extracts the value of given attribute
	 * @param element data stream element
	 * @param attributeIdentifier identifier for attribute
	 * @param type name of the datatype/ name of {@link IAttributeExtractor}-Service
	 * @return
	 * @throws DataextractionException 
	 */
	public Object extractAttribute(Object element, Object attributeIdentifier, String type) 
		throws DataextractionException;
	
	/**
	 * Extracts the value of given attribute by using the attribute plus schema information
	 * @param element data stream element
	 * @param attributeIdentifier identifier for attribute
	 * @param type name of the datatype/ name of {@link IAttributeExtractor}-Service
	 * @param schema 
	 * @return
	 * @throws DataextractionException 
	 */
	public Object extractAttribute(Object element, Object attributeIdentifier, String type, SDFSchema schema) 
		throws DataextractionException;
}
