package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractSensorAccessPO<T extends IMetaAttributeContainer<M>, M extends IMetaAttribute> extends
		AbstractIterableSource<T> {
	
	private String objectListPath;
	
	public void setObjectListPath(String objectListPath) {
		this.objectListPath = objectListPath;
	}
	
	public String getObjectListPath() {
		return objectListPath;
	}
	
	@Override
	public abstract SDFAttributeList getOutputSchema();
}
