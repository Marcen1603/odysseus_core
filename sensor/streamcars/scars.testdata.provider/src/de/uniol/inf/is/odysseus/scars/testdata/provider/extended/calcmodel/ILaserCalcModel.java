package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.LaserCarModel;

public interface ILaserCalcModel extends IGenericCalcModel {

	public void setModel(LaserCarModel model);

	public void calculatePosx();

	public void calculatePosy();

	public void calculatePosz();

	public void calculateVelocity();
	
	public void calculatePosx_np();
	
	public void calculatePosy_np();
	
	public void calculatePosxList();
	
	public void calculatePosyList();

	public int initLaneid();

	public float initPosx();

	public float initPosy();

	public float initPosz();

	public float initVelocity();
	
	public float initPosx_np();
	
	public float initPosy_np();
	
	public ArrayList<Float> initPosxList();
	
	public ArrayList<Float> initPosyList();

}
