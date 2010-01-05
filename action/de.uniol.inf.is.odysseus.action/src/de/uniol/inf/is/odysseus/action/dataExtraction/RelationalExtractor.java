package de.uniol.inf.is.odysseus.action.dataExtraction;

import de.uniol.inf.is.odysseus.action.exception.DataextractionException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

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
}
