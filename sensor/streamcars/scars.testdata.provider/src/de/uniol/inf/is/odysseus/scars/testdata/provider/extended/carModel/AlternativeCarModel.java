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
package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IAlternativeCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IGenericCalcModel;

public class AlternativeCarModel implements ICarModel {

	private int type;// Fahrzeugtyp: immer Wert 4
	private int id;// alles > 0
	private int laneid; // fahrspur: maximal 6
	private float posx; // relative koordinaten (in meter)
	private float posy;
	private float heading;
	private float velocity;// absolute geschwindigkeit in ??
	private float acceleration; // l�nge des autos in m

	private IAlternativeCalcModel calcModel;

	public AlternativeCarModel(int id, IAlternativeCalcModel calcModel) {
		this.type = 4;
		this.id = id;
		this.heading = 0.0f;

		this.calcModel = calcModel;
		this.calcModel.setModel(this);
		this.laneid = this.calcModel.initLaneid();
		this.posx = this.calcModel.initPosx();
		this.posy = this.calcModel.initPosy();
		this.velocity = this.calcModel.initVelocity();
		this.acceleration = this.calcModel.initAcceleration();
	}

	public AlternativeCarModel(AlternativeCarModel clone) {
		this.acceleration = clone.acceleration;
		this.calcModel = clone.calcModel;
		this.id = clone.id;
		this.laneid = clone.id;
		this.posx = clone.posx;
		this.posy = clone.posy;
		this.type = clone.type;
		this.velocity = clone.velocity;
		this.heading = clone.heading;
	}

	@Override
	public IGenericCalcModel getCalcModel() {
		return this.calcModel;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLaneid() {
		return laneid;
	}

	public void setLaneid(int laneid) {
		this.laneid = laneid;
	}

	public float getPosx() {
		return posx;
	}

	public void setPosx(float posx) {
		this.posx = posx;
	}

	public float getPosy() {
		return posy;
	}

	public void setPosy(float posy) {
		this.posy = posy;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	@Override
	public Object clone() {
		return new AlternativeCarModel(this);
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	/**
	 * setzt die Werte eines Autos auf die Werte eines anderen Autos
	 */
	public void setValues(AlternativeCarModel tempModel) {
		this.setType(tempModel.getType());
		this.setId(tempModel.getId());
		this.setLaneid(tempModel.getLaneid());
		this.setPosx(tempModel.getPosx());
		this.setPosy(tempModel.getPosy());
		this.setVelocity(tempModel.getVelocity());
		this.setAcceleration(tempModel.getAcceleration());
	}

}
