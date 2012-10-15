/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.clustering.model;

import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This class wraps a Tuple into an IClusteringObject.
 * 
 * @author Kolja Blohm
 * 
 */
public class RelationalClusteringObject<T extends IMetaAttribute> extends AbstractMetaAttributeContainer<T> implements IClusteringObject<T, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4096520200881866189L;
	T metadata;
	private Tuple<T> tuple;
	private Tuple<T> restrictedTuple;
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
	 *            the Tuple to wrap.
	 * @param restrictList
	 *            this array determines which attributes are used for
	 *            clustering.
	 */
	public RelationalClusteringObject(Tuple<T> tuple,
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
	 * @param clusterid
	 *            the id of the cluster this object belongs to.
	 */
	public RelationalClusteringObject(Tuple<T> restrictedTuple,
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
	 * de.uniol.inf.is.odysseus.core.server.datamining.clustering.IClusteringObject#setClusterId
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
	 * de.uniol.inf.is.odysseus.core.server.datamining.clustering.IClusteringObject#getClusterId
	 * ()
	 */
	@Override
	public int getClusterId() {

		return clusterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.datamining.clustering.IClusteringObject#
	 * getAttributes()
	 */
	@Override
	public Object[] getAttributes() {
		return tuple.getAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.datamining.clustering.IClusteringObject#
	 * getClusterAttributes()
	 */
	@Override
	public Object[] getClusterAttributes() {
		return restrictedTuple.getAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.datamining.clustering.IClusteringObject#
	 * getLabeledTuple()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Tuple<T> getLabeledTuple() {
		Object[] attributes = new Object[tuple.size() + 1];
		attributes[0] = getClusterId();

		System.arraycopy(tuple.getAttributes(), 0, attributes, 1,
				attributes.length - 1);
		Tuple<T> labeledTuple = new Tuple<T>(attributes, false);
		labeledTuple.setMetadata((T) getMetadata().clone());
		return labeledTuple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.datamining.clustering.IClusteringObject#
	 * getClusterAttributeCount()
	 */
	@Override
	public int getClusterAttributeCount() {
		return restrictedTuple.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer#getMetadata()
	 */
	@Override
	public final T getMetadata() {
		return metadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public RelationalClusteringObject<T> clone() {
		return new RelationalClusteringObject<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer#setMetadata
	 * (de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttribute)
	 */
	@Override
	public void setMetadata(T metadata) {
		this.metadata = metadata;
	}

	@Override
	public Object[] getValues() {
		return getClusterAttributes();
	}

}
