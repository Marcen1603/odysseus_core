package de.uniol.inf.is.odysseus.scars.testdata.provider.model.calculationmodels;

import de.uniol.inf.is.odysseus.scars.testdata.provider.model.CarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.model.ICalculationModel;

public class OvertakeCalculationModel implements ICalculationModel {
	
	CarModel model;
	int delay;
	
	public OvertakeCalculationModel() {
	}

	@Override
	public void calculateLaneid() {
	}

	@Override
	public void calculatePosx() {
		model.setPosx(model.getPosx() + (model.getVelocity() * this.delay / 1000));
	}

	@Override
	public void calculatePosy() {
	}

	@Override
	public void calculatePosz() {
	}

	@Override
	public void calculateVelocity() {
	}

	@Override
	public void setModel(CarModel model) {
		this.model = model;
	}

	@Override
	public void setModel(int delay) {
		this.delay = delay;
	}

}
