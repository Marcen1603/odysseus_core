package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class ManhattanDistance<T extends IMetaAttribute> extends MinkowsiDistance<T> {

	public ManhattanDistance() {
		super(1);
		
	}


}
