package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class EuroDollarTrafo extends ConversionFunction {

	/**
	 * @uml.property  name="instance"
	 */
	static EuroDollarTrafo instance = null;

	/**
	 * Umwandeln eines Euro-Wertes in einen Dollar-Wert Erst mal mit einem
	 * festen Umrechnungskurs, hier kann man später geschicktere Verfahren
	 * verwenden (z.B. den Zugriff auf einen WebService zur Umrechnung oder eine
	 * DB oder was auch immer in param muss ein Objekt vom Typ Double stecken
	 * und es wird auch ein Objekt vom Typ Double zurückgeliefert
	 */
	public List<Object> process(List<Object> params) {
		List<Object> retValue = new ArrayList<Object>();
		Iterator iter = params.iterator();
		Double euro = (Double) iter.next();
		// Darf nicht schief gehen, deswegen kein Null-Test
		//if (euro != null){
		double dollar = euro.doubleValue() * 1.2085;
		//}
		retValue.add(new Double(dollar));
		return retValue;
	}

	private EuroDollarTrafo(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static EuroDollarTrafo getInstance() {
		if (instance == null)
			instance = new EuroDollarTrafo(true);
		return instance;
	}

	public static void main(String[] args) {
		ConversionFunction conv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_unit_mappings.sdf#EuroDollarTrafo");
		ConversionFunction reConv = ConversionFunctionFactory
				.getFunction("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_unit_mappings.sdf#DollarEuroTrafo");

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