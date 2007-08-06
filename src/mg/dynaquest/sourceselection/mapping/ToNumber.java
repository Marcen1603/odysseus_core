package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class ToNumber extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	private static ToNumber instance = null;

	public ToNumber(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static ToNumber getInstance() {
		if (instance == null)
			instance = new ToNumber(true);
		return instance;
	}

	/**
	 * Alle elemente aus in in Double wandeln (soweit es geht) alle anderen
	 * elemente ignorieren, d.h. wieder zurückliefern (sinnvoll?)
	 */
	public List<Object> process(List in) {
		List<Object> ret = new ArrayList<Object>();
		Iterator iter = in.iterator();
		while (iter.hasNext()) {
			Object nextObj = iter.next();
			Double toAdd = null;
			try {
				toAdd = Double.valueOf((String) nextObj);
				ret.add(toAdd);
			} catch (Exception e) {
				e.printStackTrace();
				ret.add(nextObj);
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		ConversionFunction conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#ToNumber");
		ArrayList<Object> in = new ArrayList<Object>();
		in.add(new Double(100.05));
		in.add(new Integer(42));
		in.add(new String("100.05"));
		List<Object> out = conv.process(in);
		System.out.println(in + " --> " + out);
	}

}

