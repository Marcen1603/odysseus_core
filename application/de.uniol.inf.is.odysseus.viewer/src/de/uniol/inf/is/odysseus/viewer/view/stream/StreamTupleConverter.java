package de.uniol.inf.is.odysseus.viewer.view.stream;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@SuppressWarnings("unchecked")
public class StreamTupleConverter extends AbstractStreamConverter<RelationalTuple, Map<String, String>> {
	
	public StreamTupleConverter( Parameters params ) {
		super( params );
	}

	@Override
	public Map< String, String > convertStreamElement( RelationalTuple element ) {
		Map<String, String> tempMap = new HashMap<String, String>();
		
		for( int i = 0; i < element.getAttributeCount(); i++ ){
			String name = null;
//			if( element.getSchema() != null ){
//				SDFAttributeList schema = element.getSchema();
//				SDFAttribute attr = schema.getAttribute( i ); 
//				name = attr.getQualName();
//			}else{ 
				name = String.valueOf( i );
//			}
			String value = element.getAttribute( i ).toString();
			tempMap.put( name, value );
		}
		
		return tempMap;
	}

}
