package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;



/**
 * Class to calculate the euclidian distance between an IClusteringObject and an
 * AbstractCluster represented by its center.
 * 
 * @author Kolja Blohm
 *
 */
public class EuclidianDistance<T extends IMetaAttribute> extends
		MinkowsiDistance<T> {

	/**
	 * Creates a new EuclidianDistance.
	 */
	public EuclidianDistance() {
		super(2);

	}

}
