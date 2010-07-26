package de.uniol.inf.is.odysseus.scars.util;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class CovarianceMapper {

	private Map<String, Integer> indices = new HashMap<String, Integer>();
	
	public CovarianceMapper( SDFAttributeList schema ) {
		find(schema, null, 0);
	}
	
	private void find(SDFAttributeList list, String fullAttributeName, int counter ) {
		String lastName = fullAttributeName;
		for( SDFAttribute attribute : list ) {
			
			if( lastName == null ) 
				lastName = attribute.getSourceName() + "." + attribute.getAttributeName();
			else
				lastName = lastName + ":" + attribute.getAttributeName();
			
			if( SDFDatatypes.isMeasurementValue(attribute.getDatatype())) {
				indices.put(lastName, counter);
				counter++;
			}
		}
	}
	
	public int getSize() {
		return indices.size();
	}
	
	public int getCovarianceIndex( String fullAttributeName ) {
		return indices.get(fullAttributeName);
	}
	
}
