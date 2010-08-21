package de.uniol.inf.is.odysseus.scars.testdata.provider.model.calculationmodels;

import de.uniol.inf.is.odysseus.scars.testdata.provider.model.CalculationModelAdapter;

public class OvertakeCalculationModel extends CalculationModelAdapter {

	private float laneShiftFactor;

	public OvertakeCalculationModel() {
	}

	@Override
	public void calculatePosx() {
		this.tempModel.setPosx(this.currentModel.getPosx()
				+ (this.currentModel.getVelocity() * this.delay / 1000.0f));
	}

	@Override
	public void calculatePosy() {
		if (this.currentModel.getPosx() > 20 && this.currentModel.getPosy() < 0) {
			this.tempModel.setPosy(this.currentModel.getPosy()
					+ (laneShiftFactor * delay / 1000.0f));
		}
	}

	@Override
	public float initPosy() {
		return -10.0f;
	}

	/**
	 * Parameter: Float laneShiftFactor
	 */
	@Override
	public void init(Object... params) {
		this.laneShiftFactor = (Float) params[0];
	}

}
