package de.uniol.inf.is.odysseus.scars.util;

public class TypeCaster {

	private TypeCaster() {
		
	}
	
	public static Object cast( Object obj, Object toClass) {
		if( obj instanceof Double ) {
			Double castedValue = (Double)obj;
			if( toClass instanceof Float ) {
				return new Float( castedValue );
			} else if( toClass instanceof Integer ) {
				return new Integer(castedValue.toString());
			} else if( toClass instanceof Long ) {
				return new Long(castedValue.toString());
			} else {
				// muss wohl ein double sein
				return castedValue;
			}
		} else if( obj instanceof Float ) {
			Float castedValue = (Float)obj;
			if( toClass instanceof Double ) {
				return new Double( castedValue );
			} else if( toClass instanceof Integer ) {
				return new Integer(castedValue.toString());
			} else if( toClass instanceof Long ) {
				return new Long(castedValue.toString());
			} else {
				// muss wohl ein float sein
				return castedValue;
			}
		} else if( obj instanceof Long ) {
			Long castedValue = (Long)obj;
			if( toClass instanceof Double ) {
				return new Double( castedValue );
			} else if( toClass instanceof Integer ) {
				return new Integer(castedValue.toString());
			} else if( toClass instanceof Float ) {
				return new Float(castedValue.toString());
			} else {
				// muss wohl ein long sein
				return castedValue;
			}
		}
		
		return obj;
	}
}
