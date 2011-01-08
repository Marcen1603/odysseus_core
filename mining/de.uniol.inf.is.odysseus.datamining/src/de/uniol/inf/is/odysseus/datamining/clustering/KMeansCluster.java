package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.SimpleSinglePassKMeansPO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This class represents a cluster for the simple
 * single pass k-means algorithm implemented in the {@link SimpleSinglePassKMeansPO}.
 * 
 * 
 * @author Kolja Blohm
 * 
 */
public class KMeansCluster<T extends IMetaAttribute> extends AbstractCluster<T> {

	/**
	 * Creates a new KMeansCluster.
	 * 
	 * @param numberOfAttributes
	 *            the number of attributes the inner ClusterinFeature can store.
	 */

	public KMeansCluster(int numberOfAttributes) {
		super(numberOfAttributes);

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster#getCenter()
	 */
	@Override
	public IClusteringObject<T> getCenter() {

		return new RelationalClusteringObject<T>(new RelationalTuple<T>(
				clusteringFeature.getMean()), getId());
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy the KMeansCluster to copy.
	 */
	private KMeansCluster(KMeansCluster<T> copy) {
		super(copy);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public KMeansCluster<T> clone() {
				return new KMeansCluster<T>(this);
	}
}
