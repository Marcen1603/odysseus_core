package de.uniol.inf.is.odysseus.viewer.view.stream;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@SuppressWarnings("unchecked")
public class StreamTupleConverter extends AbstractStreamConverter<RelationalTuple, Map<String, String>> {
	
	private Map<String, String> valueMap = new HashMap<String, String>();
		
	public StreamTupleConverter( Parameters params ) {
		super( params );
	}

	@Override
	public Map< String, String > convertStreamElement( RelationalTuple element ) {
		for( int i = 0; i < element.getAttributeCount(); i++ ){
//			if( element.getSchema() != null ){
//				SDFAttributeList schema = element.getSchema();
//				SDFAttribute attr = schema.getAttribute( i ); 
//				name = attr.getQualName();
//			}else{ 
//				name = String.valueOf( i );
//			}
			valueMap.put( String.valueOf( i ), element.getAttribute( i ).toString() );
		}
		
		return valueMap;
	}

}
