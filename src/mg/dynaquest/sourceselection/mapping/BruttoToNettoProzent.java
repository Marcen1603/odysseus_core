package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class BruttoToNettoProzent extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	static BruttoToNettoProzent instance = null;

	public BruttoToNettoProzent(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static BruttoToNettoProzent getInstance() {
		if (instance == null)
			instance = new BruttoToNettoProzent(true);
		return instance;
	}

	/**
	 * Umwandeln eines Bruttowertes (0) und eines Prozentwertes (1) in einen
	 * Nettowert
	 */
	public List<Object> process(List<Object> params) {
		List<Object> retValue = new ArrayList<Object>();
		Iterator iter = params.iterator();
		Double brutto = (Double) iter.next();
		Double prozent = (Double) iter.next();
		double netto = brutto.doubleValue() / (1 + prozent.doubleValue());
		retValue.add(new Double(netto));
		return retValue;
	}

	public static void main(String[] args) {
		BruttoToNettoProzent conv = BruttoToNettoProzent.getInstance();
		ArrayList<Object> in = new ArrayList<Object>();
		in.add(new Double(100));
		in.add(new Double(0.16));
		System.out.println(conv.process(in));
	}

}

