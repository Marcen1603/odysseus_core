package de.uniol.inf.is.odysseus.action.services.dataExtraction;

import de.uniol.inf.is.odysseus.action.services.exception.DataextractionException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
			SDFAttributeList schema) throws DataextractionException;
}
