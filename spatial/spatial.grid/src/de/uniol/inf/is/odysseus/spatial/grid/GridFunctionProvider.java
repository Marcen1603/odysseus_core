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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.spatial.grid.functions.InverseGrid;
import de.uniol.inf.is.odysseus.spatial.grid.functions.MoveViewPoint;
import de.uniol.inf.is.odysseus.spatial.grid.functions.RotateGrid;
import de.uniol.inf.is.odysseus.spatial.grid.functions.RotateViewPoint;
import de.uniol.inf.is.odysseus.spatial.grid.functions.SubGrid;
import de.uniol.inf.is.odysseus.spatial.grid.functions.ToGrid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridFunctionProvider implements IFunctionProvider {

	public GridFunctionProvider() {
	}

	@Override
	public List<IFunction<?>> getFunctions() {

		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

		// Grid Functions
		functions.add(new InverseGrid());
		functions.add(new MoveViewPoint());
		functions.add(new RotateGrid());
		functions.add(new RotateViewPoint());
		functions.add(new SubGrid());
		functions.add(new ToGrid());

		return functions;
	}

}
