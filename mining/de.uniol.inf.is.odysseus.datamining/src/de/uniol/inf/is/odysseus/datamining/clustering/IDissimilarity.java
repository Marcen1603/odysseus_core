package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This interface represents dissimilarity functions to calculate
 * the dissimilarity between an IClusteringObject and an AbstractCluster.
 * 
 * 
 * @author Kolja Blohm
 *
 */
public interface IDissimilarity<T extends IMetaAttribute> {


	

	/**
	 * Calculates the dissimilarity between an IClusteringObject and
	 * and an AbstractCluster.
	 * 
	 * @param element the IClusteringObject.
	 * @param cluster the AbstractCluster.
	 * @return the dissimilarity between the cluster and the element.
	 */
	public Double getDissimilarity(IClusteringObject<T> element, AbstractCluster<T> cluster);
}
