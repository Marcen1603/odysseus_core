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

import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.AlternativeCarModel;

public class AlternativeCalcModelAdapter implements IAlternativeCalcModel {
	
	protected AlternativeCarModel currentModel;
	protected AlternativeCarModel tempModel;
	protected int delay;

	@Override
	public void calculateAll() {
		this.tempModel = (AlternativeCarModel) currentModel.clone();
		this.calculateLaneid();
		this.calculatePosx();
		this.calculatePosy();
		this.calculateVelocity();
		this.currentModel.setValues(this.tempModel);
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
	public void calculateAcceleration() {
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
