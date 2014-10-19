package de.uniol.inf.is.odysseus.mining.kohonen;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import kohonen.LearningDataModel;

/**
 * Takes tuples from Odysseus and creates weight vectors that can be used by
 * JKNNL. The Tuples must contain numeric attributes.
 * 
 * @author Dennis Nowak
 * 
 * @param <M>
 */
public class DataInput<M extends ITimeInterval> implements LearningDataModel {

	ArrayList<double[]> list = new ArrayList<double[]>();
	private double[] maxWeights;

	/**
	 * Creates an instance of DataInput
	 * 
	 * @param tuples
	 *            the tuples to give to JKNNL
	 */
	public DataInput(List<Tuple<M>> tuples) {
		maxWeights = new double[tuples.get(0).size()];
		for (Tuple<M> t : tuples) {
			double[] vector = new double[t.size()];
			for (int i = 0; i < vector.length; i++) {
				double value = t.getAttribute(i);
				vector[i] = value;
				if (value > maxWeights[i]) {
					maxWeights[i] = value;
				}

			}
			list.add(vector);
		}
	}

	@Override
	public double[] getData(int index) {
		return list.get(index);
	}

	@Override
	public int getDataSize() {
		return list.size();
	}

	/**
	 * Returns an array of maximal weights per dimension
	 * 
	 * @return array of maximal weights per dimension
	 */
	public double[] getMaxWeights() {
		return this.maxWeights;
	}

}
