package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;

public class ConnectionComparator extends Connection{

	public ConnectionComparator(Connection copy) {
		super(copy);
	}
	
	public ConnectionComparator(int[] leftPath, int[] rightPath, double rating) {
		super(leftPath, rightPath, rating);
	}

	public boolean equals(MVRelationalTuple<?> tuple, MVRelationalTuple<?> compareTuple) {
		TupleHelper tupleHelper = new TupleHelper(tuple);
		if(tupleHelper.getObject(getRightPath()).equals(compareTuple)) {
			return true;
		}
		return false;
	}

}
