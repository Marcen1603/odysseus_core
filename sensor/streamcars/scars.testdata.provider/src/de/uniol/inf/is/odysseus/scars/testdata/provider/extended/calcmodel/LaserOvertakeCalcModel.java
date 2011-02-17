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

public class LaserOvertakeCalcModel extends LaserCalcModelAdapter {
	
	public static String LANE_SHIFT_FACTOR = "laneshiftfactor";

	private float laneShiftFactor;

	public LaserOvertakeCalcModel() {
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
	public void calculatePosx_np() {
		this.tempModel.setPosx_np(this.currentModel.getPosx_np()
				+ (this.currentModel.getVelocity() * this.delay / 1000.0f));
	}

	@Override
	public void calculatePosy_np() {
		if (this.currentModel.getPosx_np() > 20 && this.currentModel.getPosy_np() < 0) {
			this.tempModel.setPosy_np(this.currentModel.getPosy_np()
					+ (laneShiftFactor * delay / 1000.0f));
		}
	}

	@Override
	public void calculatePosxList() {
		ArrayList<Float> list = new ArrayList<Float>();
		list.add(this.currentModel.getPosx_np()
				+ (this.currentModel.getVelocity() * this.delay / 1000.0f));
		this.tempModel.setPosxList(list);
	}

	@Override
	public void calculatePosyList() {
		if (this.currentModel.getPosx_np() > 20 && this.currentModel.getPosy_np() < 0) {
			ArrayList<Float> list = new ArrayList<Float>();
			list.add(this.currentModel.getPosy()
					+ (laneShiftFactor * delay / 1000.0f));
			this.tempModel.setPosyList(list);
		}
	}

	@Override
	public float initPosy() {
		return -5.0f;
	}

	@Override
	public float initPosy_np() {
		return initPosy();
	}

	@Override
	public ArrayList<Float> initPosyList() {
		ArrayList<Float> list = new ArrayList<Float>();
		list.add(initPosy());
		return list;
	}

	/**
	 * Parameter: Float laneShiftFactor
	 */
	@Override
	public void init(Map<String, Object> params) {
		this.laneShiftFactor = (Float) params.get(LANE_SHIFT_FACTOR);
	}

}
