/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import java.util.ArrayList;
import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.LaserCarModel;

public class LaserCalcModelAdapter implements ILaserCalcModel {
	
	protected LaserCarModel currentModel;
	protected LaserCarModel tempModel;
	protected int delay;

	@Override
	public void init(Map<String, Object> params) {
	}

	@Override
	public void setModel(LaserCarModel model) {
		this.currentModel = model;
	}

	@Override
	public void setDelay(int delay) {
		this.delay = delay;
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
		this.tempModel = (LaserCarModel) currentModel.clone();
		this.calculatePosx();
		this.calculatePosy();
		this.calculatePosz();
		this.calculateVelocity();
		this.calculatePosx_np();
		this.calculatePosy_np();
		this.calculatePosxList();
		this.calculatePosyList();
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

	@Override
	public void calculatePosx_np() {
		this.tempModel.setPosx_np(this.currentModel.getPosx_np());
	}

	@Override
	public void calculatePosy_np() {
		this.tempModel.setPosy_np(this.currentModel.getPosy_np());
	}

	@Override
	public void calculatePosxList() {
		this.tempModel.setPosxList(this.currentModel.getPosxList());
	}

	@Override
	public void calculatePosyList() {
		this.tempModel.setPosyList(this.currentModel.getPosyList());
	}

	@Override
	public float initPosx_np() {
		return 0.0f;
	}

	@Override
	public float initPosy_np() {
		return 0.0f;
	}

	@Override
	public ArrayList<Float> initPosxList() {
		ArrayList<Float> list = new ArrayList<Float>();
		list.add(0.0f);
		return list;
	}

	@Override
	public ArrayList<Float> initPosyList() {
		ArrayList<Float> list = new ArrayList<Float>();
		list.add(0.0f);
		return list;
	}

}

