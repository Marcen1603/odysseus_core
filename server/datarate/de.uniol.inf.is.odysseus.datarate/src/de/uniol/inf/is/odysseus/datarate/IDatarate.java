package de.uniol.inf.is.odysseus.datarate;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IDatarate extends IMetaAttribute {

	public void setDatarate( double datarate );
	public double getDatarate();
	
	@Override
	public IDatarate clone();
}
