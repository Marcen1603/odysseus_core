package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class LeaderCluster<T extends IMetaAttribute> extends ACluster<T> {

	
	RelationalTuple<T> centroid;
	
	public LeaderCluster(int attributeCount) {
		super(attributeCount);
		
	}

	@Override
	public RelationalTuple<T> getCentre() {
		return centroid;
	}
	
	public void setCentre(RelationalTuple<T> centroid) {
		this.centroid = centroid;
	}
	
}
