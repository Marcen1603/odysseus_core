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
		return -5.0f;
	}

	@Override
	public void calculateLaneid() {
		if (this.currentModel.getPosy() > -2) {
			this.tempModel.setLaneid(0);
		}
	}

	@Override
	public int initLaneid() {
		return -1;
	}

	/**
	 * Parameter: Float laneShiftFactor
	 */
	@Override
	public void init(Object... params) {
		this.laneShiftFactor = (Float) params[0];
	}

}
