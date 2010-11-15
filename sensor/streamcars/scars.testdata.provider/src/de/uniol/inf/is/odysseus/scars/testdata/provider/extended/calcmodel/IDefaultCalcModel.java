package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.DefaultCarModel;

public interface IDefaultCalcModel extends IGenericCalcModel {

	public void init(Map<String, Object> params);

	public void setModel(DefaultCarModel model);

	public void calculateLaneid();

	public void calculatePosx();

	public void calculatePosy();

	public void calculatePosz();

	public void calculateVelocity();

	public int initLaneid();

	public float initPosx();

	public float initPosy();

	public float initPosz();

	public float initVelocity();

}
