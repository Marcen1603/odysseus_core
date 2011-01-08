package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
/**
 * Class to calculate the minkowski distance, also referred to as Lp norm, between an IClusteringObject and an
 * AbstractCluster represented by its center.
 * 
 * @author Kolja Blohm
 *
 */
public class MinkowsiDistance<T extends IMetaAttribute> implements IDissimilarity<T>{

	int p;
	
	
	/**
	 * Creates a new MinkowsiDistance.
	 * @param p the p-value for this Lp norm
	 */
	public MinkowsiDistance(int p) {
		this.p = p;
	}
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.IDissimilarity#getDissimilarity(de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject, de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster)
	 */
	@Override
	public Double getDissimilarity(IClusteringObject<T> element,
			AbstractCluster<T> cluster) {
		Double distance = 0D;
		Object[] elementAttributes = element.getClusterAttributes();
		Object[] clusterAttributes = cluster.getCenter().getClusterAttributes();
		
		for(int i = 0; i < elementAttributes.length; i++){
			
			distance += Math.pow(Math.abs(Double.valueOf(elementAttributes[i].toString()) - Double.valueOf(clusterAttributes[i].toString())),p);
		}
		return Math.pow(distance,1.0/p);
	}

	
	
}
