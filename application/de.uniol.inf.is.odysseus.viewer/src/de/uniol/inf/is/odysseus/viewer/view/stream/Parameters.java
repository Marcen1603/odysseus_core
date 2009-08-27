package de.uniol.inf.is.odysseus.viewer.view.stream;

import java.util.HashMap;
import java.util.Map;


public final class Parameters {

	private Map<String, Object> params = new HashMap<String, Object>();
		
	public void put( String key, Object value ) {
		params.put( key, value );
	}
	
	public String getString( String key ) {
		return params.get( key ).toString();
	}
	
	public int getInteger( String key, int stdValue ) {
		try {
			int val = Integer.valueOf( params.get( key ).toString() );
			return val;
		} catch( Exception ex ) {
			return stdValue;
		}
	}

	public float getFloat( String key, float stdValue ) {
		try {
			float val = Float.valueOf( params.get( key ).toString() );
			return val;
		} catch( Exception ex ) {
			return stdValue;
		}
	}
	
	public Object getObject( String key ) {
		return params.get( key );
	}
}
