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
package de.uniol.inf.is.odysseus.spatial.grid;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class GridDatatypeProvider {
	private static IDataDictionary datadictionary = null;

	public void bindDictionary(IDataDictionary dataDictionary)
			throws PlanManagementException {
		datadictionary = dataDictionary;
		if (!datadictionary.existsDatatype(SDFGridDatatype.GRID.getURI())) {
			datadictionary.addDatatype(SDFGridDatatype.GRID.getURI(),
					SDFGridDatatype.GRID);
		}
	}

	public void unbindDictionary(IDataDictionary dd) {
		if (datadictionary.existsDatatype(SDFGridDatatype.GRID.getURI())) {
			datadictionary.removeDatatype(SDFGridDatatype.GRID.getURI());
		}
		datadictionary = null;
	}
}
