package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;

public interface IGain extends IMetaAttribute, IClone{

	public void setGain(double[][] newGain);
	public double[][] getGain();
}
