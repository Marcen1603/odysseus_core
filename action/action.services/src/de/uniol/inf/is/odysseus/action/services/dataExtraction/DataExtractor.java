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

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.action.services.exception.DataextractionException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;


/**
 * Class responsible for the access to data stream elements.
 * Uses {@link IAttributeExtractor}s as declarative Service for each datatype.
 * @author Simon Flandergan
 *
 */
public class DataExtractor implements IDataExtractor{
	private volatile Map<String, IAttributeExtractor> extractors;
	
	public DataExtractor () {
		this.extractors = new HashMap<String, IAttributeExtractor>();
	}
	
	/**
	 * OSGI method for binding {@link IAttributeExtractor}s
	 * @param extractor
	 */
	public void bindAttributeExtractor (IAttributeExtractor extractor){
		this.extractors.put(extractor.getName(), extractor);
	}
	
	@Override
	public Object extractAttribute(Object element, Object attributeIdentifier, String type, SDFSchema schema) throws DataextractionException{
		IAttributeExtractor extractor = this.extractors.get(type);
		if (extractor == null){
			throw new DataextractionException("No Service for extraction of datatype: "+ type+ " found.");
		}
		
		Object result = extractor.extractAttribute(attributeIdentifier, element, schema);
		return result;
	}
	
	@Override
	public Object extractAttribute(Object element, Object attributeIdentifier,
			String type) throws DataextractionException {
		IAttributeExtractor extractor = this.extractors.get(type);
		if (extractor == null){
			throw new DataextractionException("No Service for extraction of datatype: "+ type+ " found.");
		}
		
		Object result = extractor.extractAttribute(attributeIdentifier, element);
		return result;
	}
	
	
	/**
	 * OSGI method for unbinding {@link IAttributeExtractor}s
	 * @param extractor
	 */
	public void unbindAttributeExtractor (IAttributeExtractor extractor){
		this.extractors.remove(extractor.getName());
	}

	

}
