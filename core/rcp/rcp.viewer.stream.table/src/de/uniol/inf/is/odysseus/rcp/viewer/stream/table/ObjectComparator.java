package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import java.util.Comparator;

public class ObjectComparator implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		if( arg0 == null ) {
			if( arg1 == null ) {
				return 0;
			}
			return 1;
		}
		
		if( arg1 == null ) {
			return -1;
		}
		
		Class<?> class0 = arg0.getClass();
		Class<?> class1 = arg1.getClass();
		
		if( class0.equals(class1)) {
			
			if( class0.isAssignableFrom(Integer.class)) {
				return Integer.compare((Integer)arg0, (Integer)arg1);
			}
			
			if( class0.isAssignableFrom(Float.class)) {
				return Float.compare((Float)arg0, (Float)arg1);
			}
			
			if( class0.isAssignableFrom(Double.class)) {
				return Double.compare((Double)arg0, (Double)arg1);
			}
			
			if( class0.isAssignableFrom(Long.class)) {
				return Long.compare((Long)arg0, (Long)arg1);
			}

			if( class0.isAssignableFrom(String.class)) {
				return ((String)arg0).compareTo((String)arg1);
			}
			
			if( class0.isAssignableFrom(Byte.class)) {
				return Byte.compare((Byte)arg0, (Byte)arg1);
			}
			
			if( class0.isAssignableFrom(Boolean.class)) {
				return Boolean.compare((Boolean)arg0, (Boolean)arg1);
			}
		}
		
		return 0;
	}

}
