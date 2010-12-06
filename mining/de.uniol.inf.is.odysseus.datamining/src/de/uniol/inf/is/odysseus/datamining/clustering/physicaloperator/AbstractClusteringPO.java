package de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.datamining.clustering.ADissimilarity;
import de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster;
import de.uniol.inf.is.odysseus.datamining.clustering.LeaderCluster;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class AbstractClusteringPO<T extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	protected int[] restrictList;
	public int[] getRestrictList() {
		return restrictList;
	}

	public ADissimilarity<T> getDissimilarity() {
		return dissimilarity;
	}
	protected AbstractClusteringPO(){
		
	}
	protected ADissimilarity<T> dissimilarity;

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);

	}

	
	protected RelationalTuple<T> createLabeledTuple(RelationalTuple<T> tuple,
			int clusterID) {
		Object[] attributes = new Object[tuple.getAttributeCount() + 1];
		attributes[0] = clusterID;

		System.arraycopy(tuple.getAttributes(), 0, attributes, 1,
				attributes.length - 1);

		return new RelationalTuple<T>(attributes);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	public AbstractClusteringPO(AbstractClusteringPO<T> copy){
		super(copy);
		this.restrictList = Arrays.copyOf(copy.getRestrictList(),copy.getRestrictList().length);
		this.dissimilarity = copy.getDissimilarity();
	}

	public void setRestrictList(int[] determineRestrictList) {

		this.restrictList = determineRestrictList;

	}

	public void setDissimilarity(ADissimilarity<T> dissimilarity) {

		this.dissimilarity = dissimilarity;
	}

	public static <U extends IMetaAttribute,K extends AbstractCluster<U>> K getMinCluster(RelationalTuple<U> tuple, ArrayList<K> clusters,ADissimilarity<U> dissimilarity){
		K minCluster = null;
		Double minDistance = 0D;
		Double distance;
		for (K cluster : clusters) {
			distance = dissimilarity.getDissimilarity(cluster.getCentre().getAttributes(), tuple.getAttributes());
			if (minCluster == null || distance < minDistance) {
				minCluster = cluster;

				minDistance = distance;
			}

		}

		return minCluster;
		
	}
	
}
