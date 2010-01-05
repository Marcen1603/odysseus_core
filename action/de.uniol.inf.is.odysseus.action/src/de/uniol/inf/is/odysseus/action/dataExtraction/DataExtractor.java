package de.uniol.inf.is.odysseus.action.dataExtraction;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.action.exception.DataextractionException;


/**
 * Class responsible for the access to data stream elements.
 * Uses {@link IAttributeExtractor}s as declarative Service for each datatype.
 * @author Simon Flandergan
 *
 */
public class DataExtractor {
	private volatile HashMap<String, IAttributeExtractor> extractors;
	
	private static DataExtractor instance = new DataExtractor();
	
	private DataExtractor () {
		this.extractors = new HashMap<String, IAttributeExtractor>();
	}
	
	/**
	 * OSGI method for binding {@link IAttributeExtractor}s
	 * @param extractor
	 */
	public void bindAttributeExtractor (IAttributeExtractor extractor){
		this.extractors.put(extractor.getName(), extractor);
	}
	
	/**
	 * OSGI method for unbinding {@link IAttributeExtractor}s
	 * @param extractor
	 */
	public void unbindAttributeExtractor (IAttributeExtractor extractor){
		this.extractors.remove(extractor.getName());
	}
	
	
	/**
	 * Extracts the value of given attribute
	 * @param element data stream element
	 * @param attributeIdentifier identifier for attribute
	 * @param type name of the datatype/ name of {@link IAttributeExtractor}-Service
	 * @return
	 * @throws DataextractionException 
	 */
	public Object extractAttribute(Object element, Object attributeIdentifier, String type) throws DataextractionException{
		IAttributeExtractor extractor = this.extractors.get(type);
		if (extractor == null){
			throw new DataextractionException("No Service for extraction of datatype: "+ type+ " found.");
		}
		
		Object result = extractor.extractAttribute(attributeIdentifier, element);
		return result;
	}
	
	/**
	 * Returns instance (singleton pattern)
	 * @return
	 */
	public static DataExtractor getInstance() {
		return instance;
	}
	
}
