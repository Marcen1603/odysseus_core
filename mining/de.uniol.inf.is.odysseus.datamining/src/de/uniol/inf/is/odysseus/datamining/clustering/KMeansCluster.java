package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class KMeansCluster<T extends IMetaAttribute> extends AbstractCluster<T> {

	public KMeansCluster(int attributeCount) {
		super(attributeCount);

	}

	@Override
	public RelationalTuple<T> getCentre() {

		return new RelationalTuple<T>(clusteringFeature.getMean());
	}
	
	private KMeansCluster(KMeansCluster<T> copy){
		super(copy);
	}
	
	@Override
	public KMeansCluster<T> clone(){
		// TODO Auto-generated method stub
		return new KMeansCluster<T>(this);
	}
}
