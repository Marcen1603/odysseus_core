package de.uniol.inf.is.odysseus.scars.testdata.provider.model;

public interface ICalculationModel {
	
	public void setModel(CarModel model);
	
	public void setModel(int delay);
	
	public void calculateLaneid();
	
	public void calculatePosx();
	
	public void calculatePosy();
	
	public void calculatePosz();
	
	public void calculateVelocity();

}
