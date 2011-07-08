package de.uniol.inf.is.odysseus.mining.metadata;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IMiningMetadata extends IMetaAttribute{

	public boolean isCorrected();
	public void setCorrected(boolean corrected);
	public boolean isDetected();
	public void setDetected(boolean detected);
		
}
