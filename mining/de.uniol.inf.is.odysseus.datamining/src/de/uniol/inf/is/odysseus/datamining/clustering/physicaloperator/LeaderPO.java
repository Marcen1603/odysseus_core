package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.datamining.clustering.ADissimilarity;
import de.uniol.inf.is.odysseus.datamining.clustering.LeaderCluster;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
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
	protected void process_next(RelationalTuple<T> tuple, int port) {

		int clusterID = assignToCluster(tuple.restrict(restrictList, true));

		// gibt das aktuelle Tupel mit Clusterzugehörigkeit auf Port 0 aus

		transfer(createLabeledTuple(tuple, clusterID), 0);
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
				.getAttributeCount() + 2];
		attributes[0] = cluster.getId();
		attributes[1] = cluster.getClusteringFeature().getCount();
		System.arraycopy(cluster.getCentre().getAttributes(), 0, attributes, 2,
				attributes.length - 2);

		return new RelationalTuple<T>(attributes);
	}

	private int assignToCluster(RelationalTuple<T> tuple) {

		LeaderCluster<T> minCluster = getMinCluster(tuple);
		if (minCluster == null
				|| dissimilarity.getDissimilarity(tuple.getAttributes(),
						minCluster.getCentre().getAttributes()) > threshold) {
			LeaderCluster<T> cluster = new LeaderCluster<T>(
					tuple.getAttributeCount());
			cluster.setId(clusters.size());
			cluster.setCentre(tuple);

			cluster.addTuple(tuple);
			clusters.add(cluster);
			return cluster.getId();
		} else {
			minCluster.addTuple(tuple);
			return minCluster.getId();
		}
	}

	@Override
	public LeaderPO<T> clone() {

		return new LeaderPO<T>(this);
	}

	private LeaderCluster<T> getMinCluster(RelationalTuple<T> tuple) {

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
