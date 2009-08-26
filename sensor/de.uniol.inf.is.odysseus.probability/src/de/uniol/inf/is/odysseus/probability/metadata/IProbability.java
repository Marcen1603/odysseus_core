package de.uniol.inf.is.odysseus.probability.metadata;

import de.uniol.inf.is.odysseus.base.IClone;

public interface IProbability extends IClone{

	public void setCovariance(double[][] sigma);
	public double[][] getCovariance();
}
