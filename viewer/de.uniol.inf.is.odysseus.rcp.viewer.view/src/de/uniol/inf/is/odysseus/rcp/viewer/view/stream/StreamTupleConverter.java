package de.uniol.inf.is.odysseus.rcp.viewer.view.stream;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@SuppressWarnings("unchecked")
public class StreamTupleConverter extends AbstractStreamConverter<RelationalTuple, String[] > {
	
	private String[] values = null;
		
	public StreamTupleConverter( Parameters params ) {
		super( params );
	}

	@Override
	public String[] convertStreamElement( RelationalTuple element ) {
		if( values == null )
			values = new String[element.getAttributeCount()];
			
		for( int i = 0; i < element.getAttributeCount(); i++ )
			values[i] = element.getAttribute( i ).toString();
		
		
		return values;
	}

}
