package de.uniol.inf.is.odysseus.objecttracking.metadata;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;


public interface IProbability extends IMetaAttribute, IClone{

	public void setCovariance(double[][] sigma);
	public double[][] getCovariance();
}
