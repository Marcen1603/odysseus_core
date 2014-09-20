package de.uniol.inf.is.odysseus.mining.kohonen;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import kohonen.LearningDataModel;

public class DataInput<M extends ITimeInterval> implements LearningDataModel {
	
	ArrayList<double[]> list = new ArrayList<double[]>();
	private double[] maxWeights;
	
	public DataInput(List<Tuple<M>> tuples) {
		maxWeights = new double[tuples.get(0).size()];
		for (Tuple<M> t: tuples) {
			double[] vector = new double[t.size()];
			for (int i=0; i < vector.length; i++) {
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
	
	public double[] getMaxWeights() {
		return this.maxWeights;
	}

}
