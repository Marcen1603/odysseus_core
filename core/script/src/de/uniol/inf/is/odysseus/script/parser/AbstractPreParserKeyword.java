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
package de.uniol.inf.is.odysseus.script.parser;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;


public abstract class AbstractPreParserKeyword implements IPreParserKeyword {
	
	
	protected IDataDictionary getDataDictionary() {
		return GlobalState.getActiveDatadictionary();
	}
	
	/**
	 * Get Parameters with blanc as delimmiter
	 * @param parameter
	 * @return
	 */
	protected String[] getSimpleParameters(String parameter) {
		if( parameter.contains(" ")) {
			return parameter.split("\\ ");
		} else {
			//only one parameter
			return new String[] { parameter, "" };
		}
	}



}
