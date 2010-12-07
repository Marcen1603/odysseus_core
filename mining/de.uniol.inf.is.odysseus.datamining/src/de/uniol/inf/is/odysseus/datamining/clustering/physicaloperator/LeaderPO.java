package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject;
import de.uniol.inf.is.odysseus.datamining.clustering.LeaderCluster;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class LeaderPO<T extends IMetaAttribute> extends AbstractClusteringPO<T> {

	private Double threshold;

	private ArrayList<LeaderCluster<T>> clusters;

	public LeaderPO() {
		clusters = new ArrayList<LeaderCluster<T>>();
	}

	public ArrayList<LeaderCluster<T>> getClusters() {
		return clusters;
	}

	public LeaderPO(LeaderPO<T> leaderPO) {
		super(leaderPO);
		this.clusters = new ArrayList<LeaderCluster<T>>(leaderPO.getClusters());
		this.threshold = leaderPO.getThreshold();

	}

	private Double getThreshold() {

		return threshold;
	}

	@Override
	protected void process_next(IClusteringObject<T> tuple, int port) {

		assignToCluster(tuple);

		// gibt das aktuelle Tupel mit Clusterzugehörigkeit auf Port 0 aus

		transferTuple(tuple);
		// gibt eine Liste der Cluster auf Port 1 aus
		transfer(createRelationalClusterList(), 1);
	}

	

	private ArrayList<RelationalTuple<T>> createRelationalClusterList() {
		ArrayList<RelationalTuple<T>> relationalClusterList = new ArrayList<RelationalTuple<T>>();
		for (LeaderCluster<T> cluster : clusters) {
			relationalClusterList.add(createRelationalCluster(cluster));
		}
		return relationalClusterList;
	}

	private RelationalTuple<T> createRelationalCluster(LeaderCluster<T> cluster) {

		Object[] attributes = new Object[cluster.getCentre()
				.getClusterAttributeCount() + 2];
		attributes[0] = cluster.getId();
		attributes[1] = cluster.getClusteringFeature().getCount();
		System.arraycopy(cluster.getCentre().getAttributes(), 0, attributes, 2,
				attributes.length - 2);

		return new RelationalTuple<T>(attributes);
	}

	private void assignToCluster(IClusteringObject<T> tuple) {

		LeaderCluster<T> minCluster = getMinCluster(tuple);
		if (minCluster == null
				|| dissimilarity.getDissimilarity(tuple,minCluster) > threshold) {
			LeaderCluster<T> cluster = new LeaderCluster<T>(
					tuple.getClusterAttributeCount());
			cluster.setId(clusters.size());
			cluster.setCentre(tuple);

			cluster.addTuple(tuple);
			clusters.add(cluster);
			tuple.setClusterId(cluster.getId());
		} else {
			minCluster.addTuple(tuple);
			tuple.setClusterId(minCluster.getId());
		}
	}

	@Override
	public LeaderPO<T> clone() {

		return new LeaderPO<T>(this);
	}

	private LeaderCluster<T> getMinCluster(IClusteringObject<T> tuple) {

		return getMinCluster(tuple, clusters, dissimilarity);
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;

	}

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
