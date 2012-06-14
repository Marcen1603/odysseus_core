/** Copyright [2012] [The Odysseus Team]
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

package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;


public class CreateOptionParameter extends AbstractParameter<Option> {

	private static final long serialVersionUID = 6059114351837558081L;

	public CreateOptionParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement,usage);
	}
	
	public CreateOptionParameter(String name, REQUIREMENT requirement) {
		super(name, requirement,USAGE.RECENT);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		List<String> list = (List<String>) inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException(
					"Wrong number of inputs for Option. Expecting name and value.");
		}
		Option option = new Option(list.get(0), list.get(1));
		setValue(option);
	}

}
