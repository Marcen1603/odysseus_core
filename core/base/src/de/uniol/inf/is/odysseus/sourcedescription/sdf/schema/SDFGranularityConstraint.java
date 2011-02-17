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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;

public class SDFGranularityConstraint extends SDFDatatypeConstraint {

	private static final long serialVersionUID = -3795271035835243780L;
	private boolean isInt;

	public SDFGranularityConstraint(String URI, boolean isInt) {
		super(URI, SDFDatatypeConstraints.hasGranularity);
		this.isInt = isInt;
	}

	public boolean isInt() {
		return isInt;
	}

}
