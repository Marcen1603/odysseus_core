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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFDataschema extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6456013184930683544L;
	/**
	 * @uml.property  name="entityList"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity"
	 */
    private ArrayList<SDFEntity> entityList = new ArrayList<SDFEntity>();

	public SDFDataschema(String URI) {
		super(URI);
	}

	public SDFDataschema(SDFDataschema sdfDataschema) {
		super(sdfDataschema);
		this.entityList.addAll(sdfDataschema.entityList);
	}

	public void addEntity(SDFEntity entity) {
		entityList.add(entity);
	}

	public SDFEntity getEntity(int index) {
		return entityList.get(index);
	}

	public int getNoOfEntities() {
		return entityList.size();
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		for (int i = 0; i < getNoOfEntities(); i++) {
			ret.append(getEntity(i).toString() + "\n");
		}
		return ret.toString();
	}
	
	@Override
	public SDFElement clone() {
		return new SDFDataschema(this);
	}

}