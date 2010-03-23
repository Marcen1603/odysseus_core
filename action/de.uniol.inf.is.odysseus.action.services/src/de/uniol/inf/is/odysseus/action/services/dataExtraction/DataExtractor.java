package de.uniol.inf.is.odysseus.action.services.dataExtraction;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.action.services.exception.DataextractionException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


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
	
	public Object extractAttribute(Object element, Object attributeIdentifier, String type, SDFAttributeList schema) throws DataextractionException{
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
