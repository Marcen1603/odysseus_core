package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalTupleWrapper<T extends IMetaAttribute>{

	private RelationalTuple<T> tuple;
	private RelationalTuple<T> restrictedTuple;
	
	int clusterId;
	
	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}


	public RelationalTuple<T> getTuple() {
		return tuple;
	}




	public RelationalTupleWrapper(RelationalTuple<T> tuple, int[] restrictList) {
		clusterId = -1;
		this.tuple = tuple;
		this.restrictedTuple = tuple.restrict(restrictList, true);
	}
	
	
	
	public RelationalTuple<T> getRestrictedTuple() {
		return restrictedTuple;
	}


	public int getClusterId() {
		
		return clusterId;
	}


}
