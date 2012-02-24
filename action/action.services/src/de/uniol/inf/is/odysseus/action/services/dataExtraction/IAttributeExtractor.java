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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * OSGI Service Interface for Extractors
 * reading values from a data stream element of a specific type
 * @author Simon Flandergan
 *
 */
public interface IAttributeExtractor {
	
	/**
	 * Extracts the value from a data stream element's attribute 
	 * @param identifier identifier defining the attribute
	 * @param datastreamElement
	 * @return
	 * @throws DataextractionException thrown if identifier or data stream element are incompatible
	 */
	public Object extractAttribute(Object identifier, Object datastreamElement) throws DataextractionException;
	
	/**
	 * Returns the name of the extractor. Should be unique among all {@link IAttributeExtractor}s
	 * @return
	 */
	public String getName();

	/**
	 * Extracts the value from a data stream element's attribute with the use of the schema
	 * @param attributeIdentifier
	 * @param element
	 * @param schema
	 * @return
	 */
	public Object extractAttribute(Object attributeIdentifier, Object element,
			SDFSchema schema) throws DataextractionException;
}
