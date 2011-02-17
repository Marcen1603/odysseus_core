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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFPredicate;

public class SDFExtensionalSourceDescription extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3773514265256139526L;
	/**
	 * @uml.property  name="descriptionPredicates"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFPredicate"
	 */
    ArrayList<SDFPredicate> descriptionPredicates = new ArrayList<SDFPredicate>();

	public SDFExtensionalSourceDescription(String URI) {
		super(URI);
	}

	public void addDescriptionPredicate(SDFPredicate descPred) {
		this.descriptionPredicates.add(descPred);
	}

	public int getNoOfDescPred() {
		return this.descriptionPredicates.size();
	}

	public SDFPredicate getDescPredicate(int index) {
		return this.descriptionPredicates.get(index);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		for (int i = 0; i < descriptionPredicates.size(); i++) {
			ret.append("\t"
					+ ((SDFPredicate) descriptionPredicates.get(i)).toString()
					+ "\n");
		}
		return ret.toString();
	}

}