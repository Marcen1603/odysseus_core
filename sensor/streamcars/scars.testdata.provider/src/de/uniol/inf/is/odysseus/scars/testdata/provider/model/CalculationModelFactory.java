package de.uniol.inf.is.odysseus.scars.testdata.provider.model;

import de.uniol.inf.is.odysseus.scars.testdata.provider.model.calculationmodels.OvertakeCalculationModel;

public class CalculationModelFactory {
	
	public static String OVERTAKE_CALCULATION_MODEL = "overtakeCalculationModel";
	
	private static CalculationModelFactory instance;
	
	private CalculationModelFactory() {
		
	}
	
	public static synchronized CalculationModelFactory getInstance() {
		if (instance == null) {
			instance = new CalculationModelFactory();
		}
		return instance;
	}
	
	public ICalculationModel getCalculationModel(String calcModelId) {
		if (calcModelId.equals(OVERTAKE_CALCULATION_MODEL)) {
			return new OvertakeCalculationModel();
		} else {
			return new CalculationModelAdapter();
		}
	}

}
