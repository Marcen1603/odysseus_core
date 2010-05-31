package de.uniol.inf.is.odysseus.rcp.viewer.view.stream.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.view.stream.Parameters;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;



@SuppressWarnings("unchecked")
public class StreamFloatConverter extends AbstractStreamConverter<RelationalTuple, Float>{

	private int attributeID;
//	private static final Logger logger = LoggerFactory.getLogger( SWTStreamPanel.class );
	
	public StreamFloatConverter( Parameters params ) {
		super( params );
		
//		((Log4JLogger)logger).getLogger().setLevel(Level.ALL);
		
		attributeID = params.getInteger( "attributeID", 0 );
	}

	@Override
	public Float convertStreamElement( RelationalTuple element ) {
		if( element.getAttributeCount() == 0 )
			return 0.0f;
		
		try {
			float val = Float.valueOf( element.getAttribute( attributeID ).toString() );
			return val;
		} catch( Exception ex ) {
			return 0.0f;
		}
	}

}
