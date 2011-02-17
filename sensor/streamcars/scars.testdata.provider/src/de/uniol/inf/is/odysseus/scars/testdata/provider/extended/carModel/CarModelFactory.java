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

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IAlternativeCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IDefaultCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IGenericCalcModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.ILaserCalcModel;

public class CarModelFactory {
	
	private static CarModelFactory instance;
	
	private CarModelFactory() {
		
	}
	
	public static synchronized CarModelFactory getInstance() {
		if (instance == null)
			instance = new CarModelFactory();
		return instance;
	}
	
	public ICarModel buildCarModel(String schemaID, int id, IGenericCalcModel calcModel) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)) {
			return new DefaultCarModel(id, (IDefaultCalcModel)calcModel);
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE)) {
			return new AlternativeCarModel(id, (IAlternativeCalcModel)calcModel);
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_LASER)) {
			return new LaserCarModel(id, (ILaserCalcModel)calcModel);
		}
		return null;
	}

}
