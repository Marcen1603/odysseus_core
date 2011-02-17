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

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class CalcModelFactory {

	private static CalcModelFactory instance;

	private CalcModelFactory() {

	}

	public static synchronized CalcModelFactory getInstance() {
		if (instance == null) {
			instance = new CalcModelFactory();
		}
		return instance;
	}

	/**
	 * creates a new calculation model instance for the given schemaID and
	 * calcModelID. if one of the parameters is invalid or if there is no
	 * matching implementation for their combination null is returned.
	 * 
	 * @param schemaID
	 *            id of schema, not null.
	 * @param calcModelID
	 *            id of calculation model, not null.
	 * @return calculation model or null in case of errors.
	 */
	public IGenericCalcModel buildCalcModel(String schemaID, String calcModelID) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)) {
			if (calcModelID.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
				return new DefaultOvertakeCalcModel();
			}
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE)) {
			if (calcModelID.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
				return new AlternativeOvertakeCalcModel();
			}
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_LASER)) {
			if (calcModelID.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
				return new LaserOvertakeCalcModel();
			}
		}
		return null;
	}

}
