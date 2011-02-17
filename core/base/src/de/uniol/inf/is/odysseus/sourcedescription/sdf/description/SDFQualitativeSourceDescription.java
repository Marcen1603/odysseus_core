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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.quality.SDFQualityPair;

public class SDFQualitativeSourceDescription extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -753955215474820017L;
	/**
	 * @uml.property  name="qualityPairs"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.quality.SDFQualityPair"
	 */
    ArrayList<SDFQualityPair> qualityPairs = new ArrayList<SDFQualityPair>();

	public SDFQualitativeSourceDescription(String URI) {
		super(URI);
	}

	public void addQualityPair(SDFQualityPair qualPair) {
		this.qualityPairs.add(qualPair);
	}

	public int getNoOfQualityPairs() {
		return this.qualityPairs.size();
	}

	public SDFQualityPair getQualityPair(int index) {
		return (SDFQualityPair) this.qualityPairs.get(index);
	}
}