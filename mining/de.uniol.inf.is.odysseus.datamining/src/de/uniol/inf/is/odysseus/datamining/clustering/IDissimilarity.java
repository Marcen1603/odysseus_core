package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IDissimilarity<T extends IMetaAttribute> {


	

	public Double getDissimilarity(IClusteringObject<T> element, AbstractCluster<T> cluster);
}
