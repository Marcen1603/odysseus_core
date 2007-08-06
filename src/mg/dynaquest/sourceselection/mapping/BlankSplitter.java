package mg.dynaquest.sourceselection.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author  Marco Grawunder
 */
public class BlankSplitter extends ConversionFunction {
	/**
	 * @uml.property  name="instance"
	 */
	static BlankSplitter instance = null;

	protected BlankSplitter(boolean egal) {
		super(egal);
	}

	/**
	 * @return  the instance
	 * @uml.property  name="instance"
	 */
	public static BlankSplitter getInstance() {
		if (instance == null)
			instance = new BlankSplitter(true);
		return instance;
	}

	/**
	 * Splittet des String in 0 in n unterschiedliche Strings auf
	 */
	public List<Object> process(List<Object> params) {
		List<Object> retValue = new ArrayList<Object>();
		Iterator iter = params.iterator();
		String toSplit = (String) iter.next();
		StringTokenizer tokenizer = new StringTokenizer(toSplit, " ");
		while (tokenizer.hasMoreTokens()) {
			retValue.add(tokenizer.nextToken());
		}
		return retValue;
	}

}

