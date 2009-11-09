package de.uniol.inf.is.odysseus.objecttracking.physicaloperator;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractIterableSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractSensorAccessPO<T extends IMetaAttributeContainer<M>, M extends IMetaAttribute> extends
		AbstractIterableSource<T> {
	
	public abstract SDFAttributeList getOutputSchema();
}
