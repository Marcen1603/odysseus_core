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
package de.uniol.inf.is.odysseus.args.marshaller;

import java.util.ListIterator;

import de.uniol.inf.is.odysseus.args.ArgsException;

/**
 * @author Jonas Jacobi
 */
public class CharacterMarshaller implements IParameterMarshaller {

	Character value;

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void parse(ListIterator<String> args) throws ArgsException {
		if (!args.hasNext()) {
			throw new ArgsException("missing parameter");
		}

		String value = args.next();
		if (value.length() != 1) {
			throw new ArgsException("illegal value for character parameter");
		}

		this.value = value.charAt(0);
	}

}
