/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mining.clustering.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.clustering.model.AbstractCluster;
import de.uniol.inf.is.odysseus.mining.clustering.model.IClusteringObject;
import de.uniol.inf.is.odysseus.mining.clustering.model.RelationalClusteringObject;
import de.uniol.inf.is.odysseus.mining.distance.IDissimilarity;
import de.uniol.inf.is.odysseus.mining.distance.IMetricFunctionValues;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * This class is a super class for physical clustering operators. It wraps
 * incoming Tuple into RelationalClusteringObjects and passes them to
 * an abstract method, concrete physical clustering operators must implement,
 * for further processing. It also offers methods to transfer outgoing clusters
 * and data points.
 * 
 * @author Kolja Blohm
 * 
 */
public abstract class AbstractClusteringPO<T extends IMetaAttribute, O> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	protected int[] restrictList;

	protected final int ELEMENT_PORT = 0;
	protected final int CLUSTER_PORT = 1;
	protected IDissimilarity<O> dissimilarity;

	/**
	 * Returns the resrictList, a list of indices identifying the positions of
	 * the attributes in a Tuple that should be used for clustering.
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
	public IDissimilarity<O> getDissimilarity() {
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
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.ISink#processPunctuation(de
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
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#getOutputMode()
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
	public AbstractClusteringPO(AbstractClusteringPO<T, O> copy) {
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
	public void setDissimilarity(IDissimilarity<O> dissimilarity) {

		this.dissimilarity = dissimilarity;
	}

	/**
	 * Wraps an incoming Tuple into an {@link IClusteringObject} for
	 * further processing through an concrete implementation.
	 * 
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#process_next(java.lang.Object,
	 *      int)
	 */
	@Override
    protected void process_next(Tuple<T> object, int port) {
		IClusteringObject<T, Object> tuple = new RelationalClusteringObject<T>(
				object, restrictList);
		process_next(tuple, port);
	}

	/**
	 * Transfers the Tuple presentation of an
	 * {@link IClusteringObject}.
	 * 
	 * 
	 * @param object
	 *            the object to transfer.
	 */
	protected void transferTuple(IClusteringObject<T, Object> object) {

		transfer(object.getLabeledTuple(), ELEMENT_PORT);
	}

	/**
	 * Transfers a list {@link IClusteringObject}. For each object the
	 * Tuple representation is transfered.
	 * 
	 * @param objects
	 *            list of objects to transfer.
	 */
	protected void transferTuples(
			List<? extends IClusteringObject<T, O>> objects) {
		Iterator<? extends IClusteringObject<T, O>> iter = objects.iterator();
		ArrayList<Tuple<T>> transferList = new ArrayList<Tuple<T>>();

		while (iter.hasNext()) {
			Tuple<T> tuple = iter.next().getLabeledTuple();

			transferList.add(tuple);
		}
		transfer(transferList, ELEMENT_PORT);
	}

	/**
	 * Transfers the Tuple representation of an
	 * {@link AbstractCluster}.
	 * 
	 * 
	 * @param cluster
	 *            the cluster to transfer.
	 */
	protected void transferCluster(AbstractCluster<T, O> cluster) {
		transfer(cluster.getRelationalCluster(), CLUSTER_PORT);
	}

	/**
	 * Transfers a list of {@link AbstractCluster}. For each cluster its
	 * Tuple representation is transfered.
	 * 
	 * @param clusters
	 *            the list of clusters to transfer.
	 */
	protected void transferClusters(
			List<? extends AbstractCluster<T, O>> clusters) {
		Iterator<? extends AbstractCluster<T, O>> iter = clusters.iterator();
		ArrayList<Tuple<T>> list = new ArrayList<Tuple<T>>();
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
	protected abstract void process_next(IClusteringObject<T, Object> object,
			int port);

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
	public static <O, U extends IMetaAttribute, K extends IMetricFunctionValues<O>> K getMinCluster(
			IMetricFunctionValues<O> tuple, ArrayList<K> clusters,
			IDissimilarity<O> dissimilarity) {
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
