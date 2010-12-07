package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class LeaderCluster<T extends IMetaAttribute> extends AbstractCluster<T> {

	
	IClusteringObject<T> centroid;
	
	public LeaderCluster(int attributeCount) {
		super(attributeCount);
		
	}

	@Override
	public IClusteringObject<T> getCentre() {
		return centroid;
	}
	
	public void setCentre(IClusteringObject<T> centroid) {
		this.centroid = centroid;
	}
	
}
