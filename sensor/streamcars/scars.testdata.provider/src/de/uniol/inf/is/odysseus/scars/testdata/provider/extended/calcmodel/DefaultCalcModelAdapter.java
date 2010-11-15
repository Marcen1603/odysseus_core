package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.DefaultCarModel;

public class DefaultCalcModelAdapter implements IDefaultCalcModel {
	
	protected DefaultCarModel currentModel;
	protected DefaultCarModel tempModel;
	protected int delay;

	@Override
	public void init(Map<String, Object> params) {
	}

	@Override
	public void setModel(DefaultCarModel model) {
		this.currentModel = model;
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
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
	public void calculatePosz() {
		this.tempModel.setPosz(this.currentModel.getPosz());
	}

	@Override
	public void calculateVelocity() {
		this.tempModel.setVelocity(this.currentModel.getVelocity());
	}

	@Override
	public void calculateAll() {
		this.tempModel = (DefaultCarModel) currentModel.clone();
		this.calculateLaneid();
		this.calculatePosx();
		this.calculatePosy();
		this.calculatePosz();
		this.calculateVelocity();
		this.currentModel.setValues(this.tempModel);
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
	public float initPosz() {
		return 0;
	}

	@Override
	public float initVelocity() {
		return 20;
	}

}

