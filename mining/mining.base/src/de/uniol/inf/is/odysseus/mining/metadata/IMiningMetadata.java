package de.uniol.inf.is.odysseus.mining.metadata;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IMiningMetadata extends IMetaAttribute{

	public boolean isCorrected();
	public boolean isCorrectedAttribute(String attribute);
	public void setCorrectedAttribute(String attribute, boolean corrected);
	public boolean isDetected();	
	public boolean isDetectedAttribute(String attribute);
	public void setDetectedAttribute(String attribute, boolean detected);
		
}
