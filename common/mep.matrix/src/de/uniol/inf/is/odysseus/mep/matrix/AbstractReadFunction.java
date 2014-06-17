/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
abstract public class AbstractReadFunction<T> extends AbstractFunction<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4388346024131508003L;

	/**
	 * @param symbol
	 * @param arity
	 * @param acceptedTypes
	 * @param returnType
	 */
	public AbstractReadFunction(String symbol, int arity,
			SDFDatatype[][] acceptedTypes, SDFDatatype returnType) {
		super(symbol, arity, acceptedTypes, returnType);
	}

	protected double[][] getValueInternal(String path, String delimiter,
			int[] elements) {
		File file = new File(path);
		List<double[]> resultList = new LinkedList<>();
		if (file.canRead()) {
			BufferedReader reader = null;
			try {
				FileInputStream stream = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(stream));
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] stringValues = line.split(Pattern.quote(""
							+ delimiter));
					double[] values = new double[elements.length];
					for (int i = 0; i < elements.length; i++) {
						values[i] = Double
								.parseDouble(stringValues[elements[i]].trim());
					}
					resultList.add(values);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		double[][] result = new double[resultList.size()][elements.length];
		Iterator<double[]> iter = resultList.iterator();
		int i = 0;
		while (iter.hasNext()) {
			result[i] = iter.next();
			i++;
		}

		return result;
	}

}
