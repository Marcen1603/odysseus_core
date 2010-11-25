package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.AlternativeCarModel;

public interface IAlternativeCalcModel extends IGenericCalcModel {

	public void setModel(AlternativeCarModel model);

	public void calculateLaneid();

	public void calculatePosx();

	public void calculatePosy();

	public void calculateVelocity();

	public int initLaneid();

	public float initPosx();

	public float initPosy();

	public float initVelocity();

	public float initAcceleration();
	
}
