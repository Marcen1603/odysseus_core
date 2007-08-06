package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class MileKmTrafo extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	static MileKmTrafo instance = null;

	public MileKmTrafo(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static MileKmTrafo getInstance() {
		if (instance == null)
			instance = new MileKmTrafo(true);
		return instance;
	}

	/**
	 * Umwandeln einer Meilenangabe in eine Kilometerangabe
	 */
	public List<Object> process(List<Object> params) {
		List<Object> retValue = new ArrayList<Object>();
		Iterator iter = params.iterator();
		Double miles = (Double) iter.next();
		double km = miles.doubleValue() * 0.621371192;
		retValue.add(new Double(km));
		return retValue;
	}

}

