package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster;
import de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject;
import de.uniol.inf.is.odysseus.datamining.clustering.IDissimilarity;
import de.uniol.inf.is.odysseus.datamining.clustering.RelationalClusteringObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class AbstractClusteringPO<T extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	protected int[] restrictList;

	protected final int ELEMENT_PORT = 0;
	protected final int CLUSTER_PORT = 1;
	protected IDissimilarity<T> dissimilarity;

	public int[] getRestrictList() {
		return restrictList;
	}

	public IDissimilarity<T> getDissimilarity() {
		return dissimilarity;
	}

	protected AbstractClusteringPO() {

	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	public AbstractClusteringPO(AbstractClusteringPO<T> copy) {
		super(copy);
		this.restrictList = Arrays.copyOf(copy.getRestrictList(),
				copy.getRestrictList().length);
		this.dissimilarity = copy.getDissimilarity();
	}

	public void setRestrictList(int[] determineRestrictList) {

		this.restrictList = determineRestrictList;

	}

	public void setDissimilarity(IDissimilarity<T> dissimilarity) {

		this.dissimilarity = dissimilarity;
	}

	protected void process_next(RelationalTuple<T> object, int port) {
		IClusteringObject<T> tuple = new RelationalClusteringObject<T>(object,
				restrictList);
		process_next(tuple, port);
	}

	protected void transferTuple(IClusteringObject<T> object) {
		transfer(object.getLabeledTuple(), ELEMENT_PORT);
	}

	protected void transferTuples(List<? extends IClusteringObject<T>> objects) {
		Iterator<? extends IClusteringObject<T>> iter = objects.iterator();
		ArrayList<RelationalTuple<T>> list = new ArrayList<RelationalTuple<T>>();
		while (iter.hasNext()) {
			list.add(iter.next().getLabeledTuple());
		}
		transfer(list, ELEMENT_PORT);
	}

	protected void transferCluster(AbstractCluster<T> cluster){
		transfer(cluster.getRelationalCluster(), CLUSTER_PORT);
	}
	
	protected void transferClusters(List<? extends AbstractCluster<T>> clusters){
		Iterator<? extends AbstractCluster<T>> iter = clusters.iterator();
		ArrayList<RelationalTuple<T>> list = new ArrayList<RelationalTuple<T>>();
		while (iter.hasNext()) {
			list.add(iter.next().getRelationalCluster());
		}
		transfer(list, CLUSTER_PORT);
	}
	
	protected abstract void process_next(IClusteringObject<T> object, int port);

	public static <U extends IMetaAttribute, K extends AbstractCluster<U>> K getMinCluster(
			IClusteringObject<U> tuple, ArrayList<K> clusters,
			IDissimilarity<U> dissimilarity) {
		K minCluster = null;
		Double minDistance = 0D;
		Double distance;
		for (K cluster : clusters) {
			distance = dissimilarity.getDissimilarity(tuple, cluster);
			if (minCluster == null || distance < minDistance) {
				minCluster = cluster;

				minDistance = distance;
			}

		}

		return minCluster;

	}

}
