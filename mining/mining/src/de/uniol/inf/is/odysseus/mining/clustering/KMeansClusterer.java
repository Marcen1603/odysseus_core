package de.uniol.inf.is.odysseus.mining.clustering;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class KMeansClusterer<M extends ITimeInterval> implements IClusterer<M> {

	@Override
	public Map<Integer, List<Tuple<M>>> processClustering(Iterator<Tuple<M>> tuples, int[] attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOptions(Map<String, List<String>> options) {
		// TODO Auto-generated method stub
		
	}

}
