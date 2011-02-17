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
package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.entrance;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class EntranceFactory {

	private static EntranceFactory instance;

	private EntranceFactory() {

	}

	public static synchronized EntranceFactory getInstance() {
		if (instance == null)
			instance = new EntranceFactory();
		return instance;
	}

	public IEntrance buildEntrance(String schemaID, String calcModelID) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)
				&& calcModelID
						.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
			return new DefaultOvertakeEntrance();
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE)
				&& calcModelID
						.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
			return new AlternativeOvertakeEntrance();
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_LASER)
				&& calcModelID
						.equals(ExtendedProvider.CALCMODEL_SCARS_OVERTAKE)) {
			return new LaserOvertakeEntrance();
		}
		return null;
	}
}
