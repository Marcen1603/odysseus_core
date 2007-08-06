package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class ToString extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	private static ToString instance = null;

	public ToString(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static ToString getInstance() {
		if (instance == null)
			instance = new ToString(true);
		return instance;
	}

	/**
	 * Alle elemente aus in in Strings wandeln
	 */
	public List<Object> process(List<Object> in) {
		ArrayList<Object> ret = new ArrayList<Object>();
		Iterator iter = in.iterator();
		while (iter.hasNext()) {
			String toAdd = String.valueOf(iter.next());
			ret.add(toAdd);
		}
		return ret;
	}

	public static void main(String[] args) {
		ConversionFunction conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#ToString");
		List<Object> in = new ArrayList<Object>();
		in.add(new Double(100.05));
		in.add(new Integer(42));
		in.add(new String("Hallo"));
		List<Object> out = conv.process(in);
		System.out.println(in + " --> " + out);
	}

}

