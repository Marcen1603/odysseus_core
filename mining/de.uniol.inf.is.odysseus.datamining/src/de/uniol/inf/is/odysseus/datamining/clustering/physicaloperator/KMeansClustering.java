package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject;
import de.uniol.inf.is.odysseus.datamining.clustering.IDissimilarity;
import de.uniol.inf.is.odysseus.datamining.clustering.KMeansCluster;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class KMeansClustering<T extends IMetaAttribute> {

	private ArrayList<KMeansCluster<T>> clusterList;

	public ArrayList<KMeansCluster<T>> getClusterList() {
		return clusterList;
	}

	private IDissimilarity<T> dissimilarity;
	private boolean init;

	public KMeansClustering(ArrayList<IClusteringObject<T>> buffer,
			int clusterCount, int[] restrictList,
			IDissimilarity<T> dissimilarity) {
		this.clusterList = new ArrayList<KMeansCluster<T>>();
		this.dissimilarity = dissimilarity;
		initializeClusterList(buffer, clusterCount, restrictList);
	}

	private void initializeClusterList(
			ArrayList<IClusteringObject<T>> buffer, int clusterCount,
			int[] restrictList) {
		init = true;
		for (int i = 0; i < clusterCount; i++) {
			KMeansCluster<T> cluster = new KMeansCluster<T>(restrictList.length);
			cluster.setId(i);
			cluster.addTuple(buffer.get(i));
			buffer.get(i).setClusterId(i);
			clusterList.add(cluster);
		}
	}

	public void cluster(ArrayList<IClusteringObject<T>> buffer) {

		ArrayList<KMeansCluster<T>> clusterListCopy = copyClusterList();
		int oldSwaps = -1;
		int swaps = 0;

		while (swaps != oldSwaps) {
			oldSwaps = swaps;
			swaps = 0;
			ArrayList<KMeansCluster<T>> emptyClusterList = getEmtpyClusterList();

			for (IClusteringObject<T> tuple : buffer) {

				int nearestCluster = getNearestCluster(tuple, clusterListCopy);
				if (nearestCluster != tuple.getClusterId()) {
					swaps++;
				}
				emptyClusterList.get(nearestCluster).addTuple(
						tuple);
				tuple.setClusterId(nearestCluster);
			}
			if (!init) {
				for (KMeansCluster<T> cluster : clusterList) {

					int nearestCluster = getNearestCluster(cluster.getCentre(),
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

	private ArrayList<KMeansCluster<T>> getEmtpyClusterList() {
		ArrayList<KMeansCluster<T>> emptyList = new ArrayList<KMeansCluster<T>>();
		for (KMeansCluster<T> cluster : clusterList) {
			KMeansCluster<T> emptyCluster = new KMeansCluster<T>(
					cluster.getAttributeCount());
			emptyCluster.setId(cluster.getId());
			emptyList.add(emptyCluster);
		}
		return emptyList;
	}

	

	private int getNearestCluster(IClusteringObject<T> tuple,
			ArrayList<KMeansCluster<T>> clusters) {
		return AbstractClusteringPO.getMinCluster(tuple, clusters,
				dissimilarity).getId();
	}

	private ArrayList<KMeansCluster<T>> copyClusterList() {
		ArrayList<KMeansCluster<T>> clusterListCopy = new ArrayList<KMeansCluster<T>>();
		for (KMeansCluster<T> cluster : clusterList) {
			clusterListCopy.add(cluster.clone());
		}
		return clusterListCopy;
	}

}
