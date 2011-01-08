package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;
import java.util.Random;

import de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject;
import de.uniol.inf.is.odysseus.datamining.clustering.IDissimilarity;
import de.uniol.inf.is.odysseus.datamining.clustering.KMeansCluster;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents an modified implementation of the k-means algorithm
 * that can incorporate clustering features as data points by using their center
 * as a representative point weighted by the number of points in that clustering
 * feature.
 * 
 * @author Kolja Blohm
 * 
 */
public class KMeansClustering<T extends IMetaAttribute> {

	private ArrayList<KMeansCluster<T>> clusterList;

	/**
	 * Returns the list of clusters.
	 * 
	 * @return the list of clusters.
	 */
	public ArrayList<KMeansCluster<T>> getClusterList() {
		return clusterList;
	}

	private IDissimilarity<T> dissimilarity;
	private boolean init;

	/**
	 * Creates a new instance of KMeansClustering and initializes the cluster
	 * list.
	 * 
	 * @param buffer
	 *            the buffer containing objects to cluster.
	 * @param clusterCount
	 *            the number of clusters k-means has to find.
	 * @param restrictList
	 *            the indices of the attributes used for clustering.
	 * @param dissimilarity
	 *            the dissimilarity function.
	 */
	public KMeansClustering(ArrayList<IClusteringObject<T>> buffer,
			int clusterCount, int[] restrictList,
			IDissimilarity<T> dissimilarity) {
		this.clusterList = new ArrayList<KMeansCluster<T>>();
		this.dissimilarity = dissimilarity;
		initializeClusterList(buffer, clusterCount, restrictList);
	}

	/**
	 * Initializes clusterCount clusters with random data points from the buffer
	 * as their initial means.
	 * 
	 * @param buffer
	 *            the buffer of data points.
	 * @param clusterCount
	 *            the number of initial clusters.
	 * @param restrictList
	 *            the indices of the attributes used for clustering.
	 */
	private void initializeClusterList(ArrayList<IClusteringObject<T>> buffer,
			int clusterCount, int[] restrictList) {
		init = true;
		ArrayList<Integer> randomIds = new ArrayList<Integer>();
		
		Random rand = new Random();

		// create clusterCount random indices within the buffers size
		while (randomIds.size() != clusterCount) {
			int random = (int) (rand.nextDouble() * buffer.size());
			if (!randomIds.contains(random))
				randomIds.add(random);
		}
		// pick clusterCount means unsing the random indives.
		for (int i = 0; i < clusterCount; i++) {
			KMeansCluster<T> cluster = new KMeansCluster<T>(restrictList.length);
			cluster.setId(i);
			cluster.addTuple(buffer.get(randomIds.get(i)));
			clusterList.add(cluster);
			
		}

	}

	/**
	 * Clusters the objects in the buffer. Also incorporates the clustering
	 * features of old clusters as data points. The method stops clustering, as
	 * soon as the number of reassignments of data points from one cluster to
	 * another cluster doesn't change in consecutive runs.
	 * 
	 * @param buffer
	 *            the objects to cluster.
	 */
	public void cluster(ArrayList<IClusteringObject<T>> buffer) {

		ArrayList<KMeansCluster<T>> clusterListCopy = copyClusterList();
		int oldSwaps = -1;
		int swaps = 0;
		int run = 0;
		// did the number of reassignments change?
		while (swaps != oldSwaps) {
			run++;
			oldSwaps = swaps;
			swaps = 0;
			ArrayList<KMeansCluster<T>> emptyClusterList = getEmtpyClusterList();
			// cluster new data points
			for (IClusteringObject<T> tuple : buffer) {

				int nearestCluster = getNearestCluster(tuple, clusterListCopy);
				if (nearestCluster != tuple.getClusterId()) {
					swaps++;
				}
				emptyClusterList.get(nearestCluster).addTuple(tuple);
				tuple.setClusterId(nearestCluster);
			}
			/*
			 * do not cluster old clusters in the first run to avoid adding the
			 * mean value twice
			 */
			if (!init) {
				// cluster old clusters
				for (KMeansCluster<T> cluster : clusterList) {

					int nearestCluster = getNearestCluster(cluster.getCenter(),
							clusterListCopy);

					if (nearestCluster != cluster.getId()) {
						
						swaps++;
					}
					emptyClusterList.get(nearestCluster).addCluster(cluster);
				}
			}

			clusterListCopy = emptyClusterList;

		}
		init = false;
		clusterList = clusterListCopy;
		
	}

	/**
	 * Creates a list of empty clusters from the current list of clusters. The
	 * empty clusters just contain the old clusters ids.
	 * 
	 * @return list of empty clusters.
	 */
	private ArrayList<KMeansCluster<T>> getEmtpyClusterList() {
		ArrayList<KMeansCluster<T>> emptyList = new ArrayList<KMeansCluster<T>>();
		for (KMeansCluster<T> cluster : clusterList) {
			KMeansCluster<T> emptyCluster = new KMeansCluster<T>(
					cluster.getNumberOfAttributes());
			emptyCluster.setId(cluster.getId());
			emptyList.add(emptyCluster);
		}
		return emptyList;
	}

	/**
	 * Returns the nearest cluster for an object from a list of clusters.
	 * 
	 * @param object
	 *            the object.
	 * @param clusters
	 *            list of clusters
	 * @return id of the nearest cluster
	 */
	private int getNearestCluster(IClusteringObject<T> object,
			ArrayList<KMeansCluster<T>> clusters) {
		return AbstractClusteringPO.getMinCluster(object, clusters,
				dissimilarity).getId();
	}

	/**
	 * Copies the current list of clusters.
	 * 
	 * @return the copied list of clusters.
	 */
	private ArrayList<KMeansCluster<T>> copyClusterList() {
		ArrayList<KMeansCluster<T>> clusterListCopy = new ArrayList<KMeansCluster<T>>();
		for (KMeansCluster<T> cluster : clusterList) {
			clusterListCopy.add(cluster.clone());
		}
		return clusterListCopy;
	}

}
