/*
 * Created on 16.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder  TODO To change the template for this generated type comment go to  Window - Preferences - Java - Code Style - Code Templates
 */
public class StringConcatWithBlank extends ConversionFunction {
    
    	/**
		 * @uml.property  name="instance"
		 */
    	static StringConcatWithBlank instance = null;

    	protected StringConcatWithBlank(boolean egal) {
    		super(egal);
    	}

    	/**
		 * @return  the instance
		 * @uml.property  name="instance"
		 */
    	public static StringConcatWithBlank getInstance() {
    		if (instance == null)
    			instance = new StringConcatWithBlank(true);
    		return instance;
    	}

    	/**
    	 * Konkateniert alle params-Strings zu einem neuen
    	 */
    	public List<Object> process(List<Object> params) {
    		List<Object> retValue = new ArrayList<Object>();
    		StringBuffer retString = new StringBuffer();
    		Iterator iter = params.iterator();
    		while (iter.hasNext()) {
    			retString.append((String) iter.next()+" ");
    		}
    		retValue.add(retString.toString().trim());
    		return retValue;
    	}


}
