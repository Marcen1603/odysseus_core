package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This class wraps a RelationalTuple into an IClusteringObject.
 * 
 * @author Kolja Blohm
 * 
 */
public class RelationalClusteringObject<T extends IMetaAttribute> implements
		IClusteringObject<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4096520200881866189L;
	T metadata;
	private RelationalTuple<T> tuple;
	private RelationalTuple<T> restrictedTuple;
	int clusterId;

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            RelationalClusteringObject to copy.
	 */
	@SuppressWarnings("unchecked")
	public RelationalClusteringObject(RelationalClusteringObject<T> copy) {
		if (copy.metadata != null) {
			this.metadata = (T) copy.metadata.clone();
		}
		this.tuple = copy.tuple.clone();
		this.restrictedTuple = copy.restrictedTuple.clone();
		this.clusterId = copy.clusterId;
	}

	/**
	 * Creates a new RelationalClusteringObject from an existing
	 * RelationalTuple.
	 * 
	 * @param tuple
	 *            the RelationalTuple to wrap.
	 * @param restrictList
	 *            this array determines which attributes are used for
	 *            clustering.
	 */
	public RelationalClusteringObject(RelationalTuple<T> tuple,
			int[] restrictList) {
		clusterId = -1;
		this.tuple = tuple;
		this.restrictedTuple = tuple.restrict(restrictList, true);
		this.metadata = tuple.getMetadata();
	}

	/**
	 * Creates a new RelationalClusteringObject from an existing RelationalTuple
	 * that is already restricted to contain only the attributes used for
	 * clustering and already belongs to a cluster.
	 * 
	 * @param restrictedTuple
	 *            the already restricted RelationalTuple.
	 * @param clusterid the id of the cluster this object belongs to.
	 */
	public RelationalClusteringObject(RelationalTuple<T> restrictedTuple,
			int clusterid) {
		this.clusterId = clusterid;
		this.tuple = restrictedTuple;
		this.restrictedTuple = restrictedTuple;
		this.metadata = tuple.getMetadata();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject#setClusterId
	 * (int)
	 */
	@Override
	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject#getClusterId
	 * ()
	 */
	@Override
	public int getClusterId() {

		return clusterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject#
	 * getAttributes()
	 */
	@Override
	public Object[] getAttributes() {
		return tuple.getAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject#
	 * getClusterAttributes()
	 */
	@Override
	public Object[] getClusterAttributes() {
		return restrictedTuple.getAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject#
	 * getLabeledTuple()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RelationalTuple<T> getLabeledTuple() {
		Object[] attributes = new Object[tuple.getAttributeCount() + 1];
		attributes[0] = getClusterId();

		System.arraycopy(tuple.getAttributes(), 0, attributes, 1,
				attributes.length - 1);
		RelationalTuple<T> labeledTuple = new RelationalTuple<T>(attributes);
		labeledTuple.setMetadata((T) getMetadata().clone());
		return labeledTuple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject#
	 * getClusterAttributeCount()
	 */
	@Override
	public int getClusterAttributeCount() {
		return restrictedTuple.getAttributeCount();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer#getMetadata()
	 */
	@Override
	public final T getMetadata() {
		return metadata;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public RelationalClusteringObject<T> clone() {
		return new RelationalClusteringObject<T>(this);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer#setMetadata(de.uniol.inf.is.odysseus.metadata.IMetaAttribute)
	 */
	@Override
	public void setMetadata(T metadata) {
		this.metadata = metadata;
	}

}
