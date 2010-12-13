package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IGain extends IMetaAttribute, IClone{

	public void setGain(double[][] newGain);
	public double[][] getGain();
	
	public void setRestrictedGain(double[][] newGain, String[] restrictedList);
	
	public String[] getRestrictedList();

	public double getRestrictedGain(int index);
	
	public double getRestrictedGain(String attribute);
}

