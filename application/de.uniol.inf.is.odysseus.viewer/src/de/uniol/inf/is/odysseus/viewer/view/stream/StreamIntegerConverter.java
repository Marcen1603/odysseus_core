package de.uniol.inf.is.odysseus.viewer.view.stream;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


@SuppressWarnings("unchecked")
public class StreamIntegerConverter extends AbstractStreamConverter<RelationalTuple, Integer>{

	private int attributeID;
//	private static final Logger logger = LoggerFactory.getLogger( SWTStreamPanel.class );
	
	public StreamIntegerConverter( Parameters params ) {
		super( params );
		
//		((Log4JLogger)logger).getLogger().setLevel(Level.ALL);
		
		attributeID = params.getInteger( "attributeID", 0 );
	}

	@Override
	public Integer convertStreamElement( RelationalTuple element ) {
		if( element.getAttributeCount() == 0 )
			return 0;
		
		try {
			int val = Integer.valueOf( element.getAttribute( attributeID ).toString() );
			return val;
		} catch( Exception ex ) {
			return 0;
		}
	}

}
