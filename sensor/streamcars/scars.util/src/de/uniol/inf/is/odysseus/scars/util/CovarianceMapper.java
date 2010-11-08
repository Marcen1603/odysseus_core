package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class CovarianceMapper {

	private List<String> indices = new ArrayList<String>();
	
	public CovarianceMapper( SDFAttributeList schema ) {
		find(schema, null);
	}
	
	private void find(SDFAttributeList list, String fullAttributeName ) {
		String lastName = fullAttributeName;

		for( SDFAttribute attribute : list ) {
			
			if( lastName == null ) 
				lastName = attribute.getSourceName() + "." + attribute.getAttributeName();
			else
				lastName = fullAttributeName + ":" + attribute.getAttributeName();
			
			if( SDFDatatypes.isMeasurementValue(attribute.getDatatype())) {
				indices.add(lastName);
			}
			find(attribute.getSubattributes(), lastName);
		}
	}
	
	public int getSize() {
		return indices.size();
	}
	
	public int getCovarianceIndex( String fullAttributeName ) {
		return indices.indexOf(fullAttributeName);
	}
	
	public String getAttributeName( int index ) {
		return indices.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		for( int i = 0; i < getSize(); i++ ) {
			sb.append(i).append(" -> ").append(getAttributeName(i));
			if(i < getSize() - 1)
				sb.append(", ");
		}
		sb.append("}");
		
		return sb.toString();
	}
}
