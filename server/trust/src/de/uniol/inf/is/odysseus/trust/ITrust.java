package de.uniol.inf.is.odysseus.trust;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface ITrust extends IMetaAttribute {
	
	void setTrust(double trustValue);
	double getTrust();

//	@Override
//	ITrust clone();
}
