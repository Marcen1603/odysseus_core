package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.AlternativeCarModel;

public class AlternativeCalcModelAdapter implements IAlternativeCalcModel {
	
	protected AlternativeCarModel currentModel;
	protected AlternativeCarModel tempModel;
	protected int delay;

	@Override
	public void calculateAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Map<String, Object> params) {
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public void setModel(AlternativeCarModel model) {
		this.currentModel = model;
	}

	@Override
	public void calculateLaneid() {
		this.tempModel.setLaneid(this.currentModel.getLaneid());
	}

	@Override
	public void calculatePosx() {
		this.tempModel.setPosx(this.currentModel.getPosx());
	}

	@Override
	public void calculatePosy() {
		this.tempModel.setPosy(this.currentModel.getPosy());
	}

	@Override
	public void calculateVelocity() {
		this.tempModel.setVelocity(this.currentModel.getVelocity());
	}

	@Override
	public int initLaneid() {
		return 0;
	}

	@Override
	public float initPosx() {
		return 0;
	}

	@Override
	public float initPosy() {
		return 0;
	}

	@Override
	public float initVelocity() {
		return 20;
	}

	@Override
	public float initAcceleration() {
		return 0;
	}

}
