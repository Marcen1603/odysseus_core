package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class DollarEuroTrafo extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	static DollarEuroTrafo instance = null;

	public DollarEuroTrafo(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static DollarEuroTrafo getInstance() {
		if (instance == null)
			instance = new DollarEuroTrafo(true);
		return instance;
	}

	/**
	 * Umwandeln eines Dollar-Wertes in einen Euro-Wert Erst mal mit einem
	 * festen Umrechnungskurs, hier kann man später geschicktere Verfahren
	 * verwenden (z.B. den Zugriff auf einen WebService zur Umrechnung oder eine
	 * DB oder was auch immer in param muss ein Objekt vom Typ Double stecken
	 * und es wird auch ein Objekt vom Typ Double zurückgeliefert
	 */
	public List<Object> process(List<Object> params) {
		List<Object> retValue = new ArrayList<Object>();
		Iterator iter = params.iterator();
		Double dollar = (Double) iter.next();
		// Darf nicht schief gehen, deswegen kein Null-Test
		//if (euro != null){
		double euro = dollar.doubleValue() / 1.2085;
		//}
		retValue.add(new Double(euro));
		return retValue;
	}

}

