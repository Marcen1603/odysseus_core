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

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IGenericCalcModel;

public interface ICarModel {
	
	/**
	 * returns the underlying calculation model instance
	 * @return
	 */
	public IGenericCalcModel getCalcModel();
	
	/**
	 * returns the unique car id
	 * @return
	 */
	public int getId();
	
	/**
	 * sets the unique car id
	 * @param id
	 */
	public void setId(int id);
}
