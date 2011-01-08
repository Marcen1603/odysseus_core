package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject;
import de.uniol.inf.is.odysseus.datamining.clustering.LeaderCluster;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * This class represents the physical operator for the leader algorithm.
 * 
 * @author Kolja Blohm
 * 
 */
public class LeaderPO<T extends IMetaAttribute> extends AbstractClusteringPO<T> {

	private Double threshold;

	private ArrayList<LeaderCluster<T>> clusters;

	/**
	 * Creates a new LeaderPO.
	 */
	public LeaderPO() {
		clusters = new ArrayList<LeaderCluster<T>>();
	}

	/**
	 * Returns the list of clusters.
	 * 
	 * @return the list of clusters.
	 */
	public ArrayList<LeaderCluster<T>> getClusters() {
		return clusters;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param leaderPO
	 *            the LeaderPO to copy.
	 */
	public LeaderPO(LeaderPO<T> leaderPO) {
		super(leaderPO);
		this.clusters = new ArrayList<LeaderCluster<T>>(leaderPO.getClusters());
		this.threshold = leaderPO.getThreshold();

	}

	/**
	 * Returns the leader algorithms threshold
	 * 
	 * @return the threshold
	 */
	private Double getThreshold() {

		return threshold;
	}

	/**
	 * Clusters an incoming {@link IClusteringObject} using the leader
	 * algorithm.
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.AbstractClusteringPO#process_next(de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject,
	 *      int)
	 */
	@Override
	protected void process_next(IClusteringObject<T> tuple, int port) {

		assignToCluster(tuple);

		// gibt das aktuelle Tupel mit Clusterzugehörigkeit auf Port 0 aus

		transferTuple(tuple);
		// gibt eine Liste der Cluster auf Port 1 aus
		transferClusters(clusters);
	
	}

	/**
	 * Assigns an object to a cluster or creates a new cluster represented by
	 * the object using the leader algorithm.
	 * 
	 * @param object
	 *            the object to cluster.
	 */
	private void assignToCluster(IClusteringObject<T> object) {

		// get the closest cluster
		LeaderCluster<T> minCluster = getMinCluster(object);
		if (minCluster == null
				|| dissimilarity.getDissimilarity(object, minCluster) > threshold) {
			// create a new cluster, because the closest one is too far away
			LeaderCluster<T> cluster = new LeaderCluster<T>(
					object.getClusterAttributeCount());
			cluster.setId(clusters.size());
			cluster.addTuple(object);
			clusters.add(cluster);
			object.setClusterId(cluster.getId());
		} else {
			// the object fits into its closest cluster, add it.
			minCluster.addTuple(object);
			object.setClusterId(minCluster.getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public LeaderPO<T> clone() {

		return new LeaderPO<T>(this);
	}

	/**
	 * Returns the closest, of the existing clusters, for this object.
	 * 
	 * @param object
	 *            the object to find the closest cluster for.
	 * @return the closest cluster.
	 */
	private LeaderCluster<T> getMinCluster(IClusteringObject<T> object) {

		return getMinCluster(object, clusters, dissimilarity);
	}

	/**
	 * Sets the leader algorithms threshold.
	 * 
	 * @param threshold
	 *            the threshold.
	 */
	public void setThreshold(Double threshold) {
		this.threshold = threshold;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#
	 * process_isSemanticallyEqual
	 * (de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator)
	 */
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo instanceof LeaderPO) {
			LeaderPO<?> other = (LeaderPO<?>) ipo;
			if (other.threshold == this.threshold) {
				if (Arrays.equals(other.restrictList, this.restrictList)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

}
