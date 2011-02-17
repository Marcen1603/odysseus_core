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

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.LaserCarModel;

public interface ILaserCalcModel extends IGenericCalcModel {

	public void setModel(LaserCarModel model);

	public void calculatePosx();

	public void calculatePosy();

	public void calculatePosz();

	public void calculateVelocity();
	
	public void calculatePosx_np();
	
	public void calculatePosy_np();
	
	public void calculatePosxList();
	
	public void calculatePosyList();

	public int initLaneid();

	public float initPosx();

	public float initPosy();

	public float initPosz();

	public float initVelocity();
	
	public float initPosx_np();
	
	public float initPosy_np();
	
	public ArrayList<Float> initPosxList();
	
	public ArrayList<Float> initPosyList();

}
