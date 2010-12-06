package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.datamining.clustering.RelationalTupleWrapper;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class SimpleSinglePassKMeansPO<T extends IMetaAttribute> extends AbstractClusteringPO<T> {

	private int clusterCount;
	private int bufferSize;
	private ArrayList<RelationalTupleWrapper<T>> buffer;
	private KMeansClustering<T> kMeans;

	public ArrayList<RelationalTupleWrapper<T>> getBuffer() {
		return buffer;
	}

	public void setBuffer(ArrayList<RelationalTupleWrapper<T>> buffer) {
		this.buffer = buffer;
	}

	public int getClusterCount() {
		return clusterCount;
	}

	public SimpleSinglePassKMeansPO(){
		buffer = new ArrayList<RelationalTupleWrapper<T>>();
	}
	
	protected SimpleSinglePassKMeansPO(SimpleSinglePassKMeansPO<T> copy){
		super(copy);
		this.buffer = new ArrayList<RelationalTupleWrapper<T>>(copy.getBuffer());
		this.bufferSize = copy.bufferSize;
		this.clusterCount = copy.clusterCount;
	}
	
	@Override
	protected void process_next(RelationalTuple<T> tuple, int port) {
		
		buffer.add(new RelationalTupleWrapper<T>(tuple,restrictList));
		if(buffer.size() == bufferSize){
			if(kMeans == null){
				kMeans = new KMeansClustering<T>(buffer,clusterCount,restrictList,dissimilarity);
				
			}
			kMeans.cluster(buffer);
			transferTuple();
			buffer.clear();
		}
		
		
	}
	
	private void transferTuple(){
		for(RelationalTupleWrapper<T> tupleWrapper: buffer){
			transfer(createLabeledTuple(tupleWrapper.getTuple(),tupleWrapper.getClusterId()), 0);
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
