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

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.ILaserCalcModel;

public class LaserCarModel implements ICarModel {

	private int type;// Fahrzeugtyp: immer Wert 4
	private int id;// alles > 0
	private float posx; // relative koordinaten (in meter?)
	private float posy;
	private float posz;
	private float heading;// kann immer 0 sein
	private float velocity;// absolute geschwindigkeit in ??
	private float posx_np; // nearest point x koordinate
	private float posy_np; // nearest point y koordinate
	
	private ArrayList<Float> posxList;
	private ArrayList<Float> posyList;

	private ILaserCalcModel calcModel;

	public LaserCarModel(int id, ILaserCalcModel calcModel) {
		this.type = 4;
		this.id = id;
		this.heading = 0.0f;

		this.calcModel = calcModel;
		this.calcModel.setModel(this);
		this.posx = this.calcModel.initPosx();
		this.posy = this.calcModel.initPosy();
		this.posz = this.calcModel.initPosz();
		this.velocity = this.calcModel.initVelocity();
		this.posx_np = this.calcModel.initPosx_np();
		this.posy_np = this.calcModel.initPosy_np();
		this.posxList = this.calcModel.initPosxList();
		this.posyList = this.calcModel.initPosyList();
	}

	public LaserCarModel(LaserCarModel clone) {
		this.type = clone.type;
		this.id = clone.id;
		this.heading = clone.heading;
		this.calcModel = clone.calcModel;
		this.posx = clone.posx;
		this.posy = clone.posy;
		this.posz = clone.posz;
		this.velocity = clone.velocity;
		this.posx_np = clone.posx_np;
		this.posy_np = clone.posy_np;
		this.posxList = (ArrayList<Float>)clone.posxList.clone();
		this.posyList = (ArrayList<Float>)clone.posyList.clone();
	}

	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public float getPosz() {
		return posz;
	}

	public void setPosz(float posz) {
		this.posz = posz;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getPosx_np() {
		return posx_np;
	}

	public void setPosx_np(float posx_np) {
		this.posx_np = posx_np;
	}

	public float getPosy_np() {
		return posy_np;
	}

	public void setPosy_np(float posy_np) {
		this.posy_np = posy_np;
	}

	public ArrayList<Float> getPosxList() {
		return posxList;
	}

	public void setPosxList(ArrayList<Float> posxList) {
		this.posxList = posxList;
	}

	public ArrayList<Float> getPosyList() {
		return posyList;
	}

	public void setPosyList(ArrayList<Float> posyList) {
		this.posyList = posyList;
	}

	public ILaserCalcModel getCalcModel() {
		return calcModel;
	}

	public void setCalcModel(ILaserCalcModel calcModel) {
		this.calcModel = calcModel;
	}

	/**
	 * setzt die Werte eines Autos auf die Werte eines anderen Autos
	 */
	public void setValues(LaserCarModel tempModel) {
		this.setType(tempModel.getType());
		this.setId(tempModel.getId());
		this.setPosx(tempModel.getPosx());
		this.setPosy(tempModel.getPosy());
		this.setPosz(tempModel.getPosz());
		this.setHeading(tempModel.getHeading());
		this.setVelocity(tempModel.getVelocity());
		this.setPosx_np(tempModel.getPosx_np());
		this.setPosy_np(tempModel.getPosy_np());
		this.setPosxList((ArrayList<Float>)tempModel.getPosxList().clone());
		this.setPosyList((ArrayList<Float>)tempModel.getPosyList().clone());
	}

	@Override
	public String toString() {
		String str = "Car\n";
		str += "  type:     " + this.type + "\n";
		str += "  id:       " + this.id + "\n";
		str += "  x:        " + this.posx + "\n";
		str += "  y:        " + this.posy + "\n";
		str += "  z:        " + this.posz + "\n";
		str += "  heading:  " + this.heading + "\n";
		str += "  velocity: " + this.velocity + "\n";
		str += "  posx_np:  " + this.posx_np + "\n";
		str += "  posy_np:  " + this.posy_np + "\n";
		str += "  pointlist:\n";
		for (int i = 0; i < this.posxList.size(); i++) {
			str += "    (" + this.posxList.get(i) + ", " + this.posyList.get(i) + ")\n";
		}

		return str;
	}

	@Override
	public Object clone() {
		return new LaserCarModel(this);
	}

}
