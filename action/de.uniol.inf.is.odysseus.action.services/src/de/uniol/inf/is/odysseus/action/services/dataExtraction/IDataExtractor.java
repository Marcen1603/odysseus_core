package de.uniol.inf.is.odysseus.action.services.dataExtraction;

import de.uniol.inf.is.odysseus.action.services.exception.DataextractionException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
	public Object extractAttribute(Object element, Object attributeIdentifier, String type, SDFAttributeList schema) 
		throws DataextractionException;
}
