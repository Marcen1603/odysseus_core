package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class ADissimilarity<T extends IMetaAttribute> {

	private SDFAttributeList schema;

	public ADissimilarity(SDFAttributeList schema) {
		this.schema = schema;
	}

	protected SDFAttributeList getSchema() {
		return schema;
	}

	public Double getDissimilarity(RelationalTuple<T> one,
			RelationalTuple<T> two) {
		return getDissimilarity(one.getAttributes(), two.getAttributes());
	}

	public abstract Double getDissimilarity(Object[] one, Object[] two);
}
