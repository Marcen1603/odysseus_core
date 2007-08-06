package mg.dynaquest.sourceselection.mapping;

import java.util.List;

/**
 * @author  Marco Grawunder
 */
public class Identity extends ConversionFunction {

	/**
	 * @uml.property  name="instance"
	 */
	private static Identity instance = null;

	private Identity(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static Identity getInstance() {
		if (instance == null)
			instance = new Identity(true);
		return instance;
	}

	/**
	 * Identität macht nichts anderes als den Wert wieder zurückzuliefern
	 */
	public List<Object> process(List<Object> in) {
		return in;
	}

}