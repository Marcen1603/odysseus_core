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
package de.uniol.inf.is.odysseus.spatial;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.spatial.access.SpatialByteHandler;
import de.uniol.inf.is.odysseus.spatial.access.SpatialStringHandler;

/**
 * @author André Bolles
 *
 */
public class SpatialDataTypeHandlerProvider {

	public List<IAtomicDataHandler> getDataHandler(){
		List<IAtomicDataHandler> handler = new ArrayList<IAtomicDataHandler>();
		
		handler.add(new SpatialByteHandler());
		handler.add(new SpatialStringHandler());
		
		return handler;
	}
}
