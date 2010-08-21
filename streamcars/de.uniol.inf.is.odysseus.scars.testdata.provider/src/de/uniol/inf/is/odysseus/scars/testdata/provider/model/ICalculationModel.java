package de.uniol.inf.is.odysseus.scars.testdata.provider.model;

public interface ICalculationModel {
	
	public void init(Object... params );
	
	public void setModel(CarModel model);
	
	public void setDelay(int delay);
	
	public void calculateLaneid();
	
	public void calculatePosx();
	
	public void calculatePosy();
	
	public void calculatePosz();
	
	public void calculateVelocity();
	
	public void calculateAll();
	
	public int initLaneid();
	
	public float initPosx();
	
	public float initPosy();
	
	public float initPosz();
	
	public float initVelocity();

}
