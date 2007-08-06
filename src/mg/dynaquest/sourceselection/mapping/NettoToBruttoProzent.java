package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class NettoToBruttoProzent extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	static NettoToBruttoProzent instance = null;

	public NettoToBruttoProzent(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static NettoToBruttoProzent getInstance() {
		if (instance == null)
			instance = new NettoToBruttoProzent(true);
		return instance;
	}

	/**
	 * Umwandeln eines Nettowertes (0) und eines Prozentwertes (1) in einen
	 * Bruttowert
	 */
	public List<Object> process(List<Object> params) {
		List<Object> retValue = new ArrayList<Object>();
		Iterator iter = params.iterator();
		Double netto = (Double) iter.next();
		Double prozent = (Double) iter.next();
		double brutto = netto.doubleValue() * (1 + prozent.doubleValue());
		retValue.add(new Double(brutto));
		return retValue;
	}

	public static void main(String[] args) {
		NettoToBruttoProzent conv = NettoToBruttoProzent.getInstance();
		ArrayList<Object> in = new ArrayList<Object>();
		in.add(new Double(100));
		in.add(new Double(0.16));
		System.out.println(conv.process(in));
	}

}

