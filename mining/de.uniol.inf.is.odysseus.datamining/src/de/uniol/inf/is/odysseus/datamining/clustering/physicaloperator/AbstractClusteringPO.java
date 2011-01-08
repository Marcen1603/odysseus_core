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

/**
 * This class is a super class for physical clustering operators. It wraps
 * incoming RelationalTuple into RelationalClusteringObjects and passes them to
 * an abstract method, concrete physical clustering operators must implement,
 * for further processing. It also offers methods to transfer outgoing clusters
 * and data points.
 * 
 * @author Kolja Blohm
 * 
 */
public abstract class AbstractClusteringPO<T extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	protected int[] restrictList;

	protected final int ELEMENT_PORT = 0;
	protected final int CLUSTER_PORT = 1;
	protected IDissimilarity<T> dissimilarity;

	
	/**
	 * Returns the resrictList, a list of indices identifying the positions of
	 * the attributes in a RelationalTuple that should be used for clustering.
	 * 
	 * @return the list of indices.
	 */
	public int[] getRestrictList() {
		return restrictList;
	}

	/**
	 * Returns the dissimilarity function used for clustering.
	 * 
	 * @return the dissimilarity function.
	 */
	public IDissimilarity<T> getDissimilarity() {
		return dissimilarity;
	}

	/**
	 * Default constructor.
	 */
	protected AbstractClusteringPO() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.ISink#processPunctuation(de
	 * .uniol.inf.is.odysseus.metadata.PointInTime, int)
	 */
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the AbstractClusteringPO to copy.
	 */
	public AbstractClusteringPO(AbstractClusteringPO<T> copy) {
		super(copy);

		this.restrictList = Arrays.copyOf(copy.getRestrictList(),
				copy.getRestrictList().length);
		this.dissimilarity = copy.getDissimilarity();
	}

	/**
	 * Sets the restrictList containing the indices of attributes in a
	 * RelationalTuple, which should be used in the clustering process.
	 * 
	 * @param restrictList
	 *            the list of indices.
	 */
	public void setRestrictList(int[] restrictList) {

		this.restrictList = restrictList;

	}

	/**
	 * Sets the dissimilarity function used in the clustering process.
	 * 
	 * @param dissimilarity
	 *            the dissimilarity function.
	 */
	public void setDissimilarity(IDissimilarity<T> dissimilarity) {

		this.dissimilarity = dissimilarity;
	}

	/**
	 * Wraps an incoming RelationalTuple into an {@link IClusteringObject} for
	 * further processing through an concrete implementation.
	 * 
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#process_next(java.lang.Object,
	 *      int)
	 */
	protected void process_next(RelationalTuple<T> object, int port) {

		IClusteringObject<T> tuple = new RelationalClusteringObject<T>(object,
				restrictList);
		process_next(tuple, port);
	}

	/**
	 * Transfers the RelationalTuple presentation of an
	 * {@link IClusteringObject}.
	 * 
	 * 
	 * @param object
	 *            the object to transfer.
	 */
	protected void transferTuple(IClusteringObject<T> object) {
		
		transfer(object.getLabeledTuple(), ELEMENT_PORT);
	}

	/**
	 * Transfers a list {@link IClusteringObject}. For each object the
	 * RelationalTuple representation is transfered.
	 * 
	 * @param objects
	 *            list of objects to transfer.
	 */
	protected void transferTuples(List<? extends IClusteringObject<T>> objects) {
		Iterator<? extends IClusteringObject<T>> iter = objects.iterator();
		ArrayList<RelationalTuple<T>> transferList = new ArrayList<RelationalTuple<T>>();

		while (iter.hasNext()) {
			RelationalTuple<T> tuple = iter.next().getLabeledTuple();
			
			transferList.add(tuple);
		}
		transfer(transferList, ELEMENT_PORT);
	}

	/**
	 * Transfers the RelationalTuple representation of an
	 * {@link AbstractCluster}.
	 * 
	 * 
	 * @param cluster
	 *            the cluster to transfer.
	 */
	protected void transferCluster(AbstractCluster<T> cluster) {
		transfer(cluster.getRelationalCluster(), CLUSTER_PORT);
	}

	/**
	 * Transfers a list of {@link AbstractCluster}. For each cluster its
	 * RelationalTuple representation is transfered.
	 * 
	 * @param clusters
	 *            the list of clusters to transfer.
	 */
	protected void transferClusters(List<? extends AbstractCluster<T>> clusters) {
		Iterator<? extends AbstractCluster<T>> iter = clusters.iterator();
		ArrayList<RelationalTuple<T>> list = new ArrayList<RelationalTuple<T>>();
		while (iter.hasNext()) {
			list.add(iter.next().getRelationalCluster());
		}
		transfer(list, CLUSTER_PORT);
	}

	/**
	 * Concrete implementations must override this method to process and cluster
	 * incoming objects.
	 * 
	 * @param object
	 *            the object to process.
	 * @param port
	 *            the port on which the object arrived on.
	 */
	protected abstract void process_next(IClusteringObject<T> object, int port);

	/**
	 * Finds the closest cluster from a list of clusters for a specific data
	 * point using a dissimilarity function.
	 * 
	 * @param tuple
	 *            the data point.
	 * @param clusters
	 *            the list of clusters.
	 * @param dissimilarity
	 *            the dissimilarity function.
	 * @return the cluster closest to the specified data point.
	 */
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
