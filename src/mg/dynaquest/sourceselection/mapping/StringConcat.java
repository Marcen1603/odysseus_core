package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class StringConcat extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	static StringConcat instance = null;

	protected StringConcat(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static StringConcat getInstance() {
		if (instance == null)
			instance = new StringConcat(true);
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
			retString.append((String) iter.next());
		}
		retValue.add(retString.toString());
		return retValue;
	}

}

