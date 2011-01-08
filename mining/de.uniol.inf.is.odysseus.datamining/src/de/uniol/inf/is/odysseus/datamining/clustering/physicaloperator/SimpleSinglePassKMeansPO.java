package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents the physical operator for the simple single pass k-meas
 * algorithm.
 * 
 * @author Kolja Blohm
 * 
 */
public class SimpleSinglePassKMeansPO<T extends IMetaAttribute> extends
		AbstractClusteringPO<T> {

	private int clusterCount;
	private int bufferSize;
	private ArrayList<IClusteringObject<T>> buffer;
	private KMeansClustering<T> kMeans;

	/**
	 * Returns the buffer of IClusteringObjects.
	 * 
	 * @return the buffer
	 */
	public ArrayList<IClusteringObject<T>> getBuffer() {
		return buffer;
	}

	/**
	 * Returns how many clusters the algorithm should find.
	 * 
	 * @return the number of clusters.
	 */
	public int getClusterCount() {
		return clusterCount;
	}

	/**
	 * Creates a new SimpleSinglePassKMeansPO.
	 */
	public SimpleSinglePassKMeansPO() {
		buffer = new ArrayList<IClusteringObject<T>>();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the SimpleSinglePassKMeansPO to copy.
	 */
	protected SimpleSinglePassKMeansPO(SimpleSinglePassKMeansPO<T> copy) {
		super(copy);
		this.buffer = new ArrayList<IClusteringObject<T>>(copy.getBuffer());
		this.bufferSize = copy.bufferSize;
		this.clusterCount = copy.clusterCount;
	}

	/**
	 * Clusters an incoming {@link IClusteringObject} using the simple single
	 * pass k-means algorithm.
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.AbstractClusteringPO#process_next(de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject,
	 *      int)
	 */
	@Override
	protected void process_next(IClusteringObject<T> object, int port) {

		// adds the object into the buffer
		buffer.add(object);
		if (buffer.size() == bufferSize) {
			// if the buffer is full, use k-means to cluster it.
			if (kMeans == null) {
				// initialize the KMeansClustering object
				kMeans = new KMeansClustering<T>(buffer, clusterCount,
						restrictList, dissimilarity);

			}
			kMeans.cluster(buffer);
			// transfer the buffered objects and the clusters
			transferTuples(buffer);
			transferClusters(kMeans.getClusterList());
			buffer.clear();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public SimpleSinglePassKMeansPO<T> clone() {

		return new SimpleSinglePassKMeansPO<T>(this);
	}

	/**
	 * @param clusterCount
	 */
	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;

	}

	/**
	 * Sets the size of the buffer. The size is measured in the number of data
	 * points the buffer can contain.
	 * 
	 * @param bufferSize
	 *            the buffer size.
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;

	}

	/**
	 * Returns the number of data points the buffer can contain.
	 * 
	 * @return the buffer size.
	 */
	public int getBufferSize() {
		return bufferSize;
	}

}
