package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class KmMileTrafo extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	static KmMileTrafo instance = null;

	public KmMileTrafo(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static KmMileTrafo getInstance() {
		if (instance == null)
			instance = new KmMileTrafo(true);
		return instance;
	}

	/**
	 * Umwandeln einer Meilenangabe in eine Kilometerangabe
	 */
	public List<Object> process(List<Object> params) {
		List<Object> retValue = new ArrayList<Object>();
		Iterator iter = params.iterator();
		Double km = (Double) iter.next();
		double miles = km.doubleValue() * 1.609344;
		retValue.add(new Double(miles));
		return retValue;
	}

	public static void main(String[] args) {
		ConversionFunction conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Source_CarVendor_1.sdf#MileToKm");
		ConversionFunction reConv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Source_CarVendor_1.sdf#KmToMile");

		ArrayList<Object> in = new ArrayList<Object>();
		in.add(0, new Double(0));
		for (int i = 0; i < 1000; i++) {
			in.set(0, new Double(i));
			System.out.print(in + "-->");
			List<Object> ret = conv.process(in);
			System.out.print(ret);
			System.out.println("-->" + reConv.process(ret));
		}

	}

}

