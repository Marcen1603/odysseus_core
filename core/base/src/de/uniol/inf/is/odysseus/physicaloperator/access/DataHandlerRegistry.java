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
package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.util.HashMap;

/**
 * @author André Bolles
 *
 */
public class DataHandlerRegistry {

	private static HashMap<String, IAtomicDataHandler> dataHandlers = new HashMap<String, IAtomicDataHandler>();
	
	public static void addDataHandlerProvider(IDataHandlerProvider provider){
		for(IAtomicDataHandler curHandler: provider.getDataHandlers()){
			if(dataHandlers.containsKey(curHandler.getName())){
				throw new IllegalArgumentException("Data handler with name " + curHandler.getName() + " is already registered.");
			}
			
			dataHandlers.put(curHandler.getName(), curHandler);
		}
	}
	
	public static void removeDataHandler(String name){
		dataHandlers.remove(name);
	}
	
	public static IAtomicDataHandler getDataHandler(String name){
		return dataHandlers.get(name);
	}
}
