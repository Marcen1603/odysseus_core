package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MinkowsiDistance<T extends IMetaAttribute> implements IDissimilarity<T>{

	int p;
	public MinkowsiDistance(int p) {
		this.p = p;
	}
	@Override
	public Double getDissimilarity(IClusteringObject<T> element,
			AbstractCluster<T> cluster) {
		Double distance = 0D;
		Object[] elementAttributes = element.getClusterAttributes();
		Object[] clusterAttributes = cluster.getCentre().getClusterAttributes();
		for(int i = 0; i < elementAttributes.length; i++){
			distance += Math.pow(Math.abs(Double.valueOf(elementAttributes[i].toString()) - Double.valueOf(clusterAttributes[i].toString())),p);
		}
		return Math.pow(distance,1.0/p);
	}

	
	
}
