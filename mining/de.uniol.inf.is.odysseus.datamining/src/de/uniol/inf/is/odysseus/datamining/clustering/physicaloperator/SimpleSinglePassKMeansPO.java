package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class SimpleSinglePassKMeansPO<T extends IMetaAttribute> extends AbstractClusteringPO<T> {

	private int clusterCount;
	private int bufferSize;
	private ArrayList<IClusteringObject<T>> buffer;
	private KMeansClustering<T> kMeans;

	public ArrayList<IClusteringObject<T>> getBuffer() {
		return buffer;
	}



	public int getClusterCount() {
		return clusterCount;
	}

	public SimpleSinglePassKMeansPO(){
		buffer = new ArrayList<IClusteringObject<T>>();
	}
	
	protected SimpleSinglePassKMeansPO(SimpleSinglePassKMeansPO<T> copy){
		super(copy);
		this.buffer = new ArrayList<IClusteringObject<T>>(copy.getBuffer());
		this.bufferSize = copy.bufferSize;
		this.clusterCount = copy.clusterCount;
	}
	
	@Override
	protected void process_next(IClusteringObject<T> tuple, int port) {
		
		buffer.add(tuple);
		if(buffer.size() == bufferSize){
			if(kMeans == null){
				kMeans = new KMeansClustering<T>(buffer,clusterCount,restrictList,dissimilarity);
				
			}
			kMeans.cluster(buffer);
			transferTuples(buffer);
			transferClusters(kMeans.getClusterList());
			buffer.clear();
		}
		
		
	}
	
	

	@Override
	public SimpleSinglePassKMeansPO<T> clone() {
		
		return new SimpleSinglePassKMeansPO<T>(this);
	}

	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;
		
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
		
	}

	public int getBufferSize() {
		return bufferSize;
	}

	

}
