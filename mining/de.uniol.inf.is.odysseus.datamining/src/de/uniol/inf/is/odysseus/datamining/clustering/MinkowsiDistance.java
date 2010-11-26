package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class MinkowsiDistance<T extends IMetaAttribute> extends ADissimilarity<T>{

	int p;
	public MinkowsiDistance(int p) {
		this.p = p;
	}

	@Override
	public Double getDissimilarity(Object[] one, Object[] two) {
		Double distance = 0D;
		for(int i = 0; i < one.length; i++){
			distance += Math.pow(Math.abs(Double.valueOf(one[i].toString()) - Double.valueOf(two[i].toString())),p);
		}
		return Math.pow(distance,1.0/p);
	}
	
}
