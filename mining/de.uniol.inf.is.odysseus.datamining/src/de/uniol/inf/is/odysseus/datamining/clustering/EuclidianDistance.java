package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class EuclidianDistance<T extends IMetaAttribute> extends MinkowsiDistance<T> {

	public EuclidianDistance() {
		super( 2);
		
	}


}
