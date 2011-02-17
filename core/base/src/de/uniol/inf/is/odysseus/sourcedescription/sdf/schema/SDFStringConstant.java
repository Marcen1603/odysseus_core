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
/*
 * Created on 06.12.2004
 *
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author Marco Grawunder
 *
 */
public class SDFStringConstant extends SDFConstant {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3768336548373743717L;

	/**
     * @param URI
     * @param value
     */
    public SDFStringConstant(String URI, String value) {
        super(URI, value, SDFDatatypeFactory.getDatatype(SDFDatatypes.String));
    }

    /**
     * @param constValue
     */
    public SDFStringConstant(SDFConstant constValue) {
        super(constValue.getURI(false), constValue.getString(), SDFDatatypeFactory.getDatatype(SDFDatatypes.String));
    }

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public boolean isString() {
		return true;
	}

}
