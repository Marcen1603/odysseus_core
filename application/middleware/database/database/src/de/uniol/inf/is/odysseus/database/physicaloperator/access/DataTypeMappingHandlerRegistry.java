/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.database.physicaloperator.access;

import java.util.HashMap;


/**
 * @author Andr� Bolles, Stephan Jansen
 *
 */
public class DataTypeMappingHandlerRegistry {

	/**
	 * HashMap from datatype to data handler
	 */
	private static HashMap<String, IDataTypeMappingHandler> dataHandlers = new HashMap<String, IDataTypeMappingHandler>();
	
	public static void registerDataHandler(IDataTypeMappingHandler handler){
		String errMsg = "";
		for(String type: handler.getSupportedDataTypes()){
			if(dataHandlers.containsKey(type.toLowerCase())){
				errMsg += "Data handler for " + type + " already registered.\n";
			}
			else{
				dataHandlers.put(type.toLowerCase(), handler);
			}
		}
		
		if(errMsg != ""){
			throw new IllegalArgumentException(errMsg);
		}
	}
	
//	public static void removeDataHandler(String dataType){
//		dataHandlers.remove(dataType);
//	}
	
	public static void removeDataHandler(IDataTypeMappingHandler handler){
		for(String type: handler.getSupportedDataTypes()){
			if(dataHandlers.containsKey(type.toLowerCase())){
				dataHandlers.remove(type.toLowerCase());
			}
		}
	}
	
	public static IDataTypeMappingHandler getDataHandler(String dataType){
		return dataHandlers.get(dataType.toLowerCase());
	}
}
