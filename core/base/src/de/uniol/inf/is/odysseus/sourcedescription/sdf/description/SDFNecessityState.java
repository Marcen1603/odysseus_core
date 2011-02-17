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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

public class SDFNecessityState extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -206499892417834148L;
	/**
	 * @uml.property  name="optional"
	 */
    boolean optional = false;


	public SDFNecessityState(String URI) {
		super(URI);
		if (URI
				.equals(SDFIntensionalDescriptions.Optional)) {
			optional = true;
		}
	}

    /**
     * 
     * @uml.property name="optional"
     */
    public boolean isOptional() {
        return this.optional;
    }

}