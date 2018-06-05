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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;

public class UncheckedExpressionParamter extends AbstractParameter<String[]> {

	private static final long serialVersionUID = -3129785072529123574L;

	@Override
	protected void internalAssignment() {
		String[] stringArray = new String[2];
		if (inputValue instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> in = ((List<String>) inputValue);
			if (in.size() == 2) {
				stringArray[0] = in.get(1);
				stringArray[1] = in.get(0);
			} else {
				throw new RuntimeException(
						" Could not determine name/expression pair!");
			}
		}  else {
			stringArray[0] = "";
			stringArray[1] = (String) inputValue;
		}
		setValue(stringArray);
	}

	@Override
	protected String getPQLStringInternal() {
		if (inputValue instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> in = ((List<String>) inputValue);
			if (in.size() == 2) {
				return "['" + in.get(0) + "', '" + in.get(1) + "']";
			} 
			throw new RuntimeException("Could not determine name/expression pair!");
			
		} else {
			return "'" + inputValue + "'";
		}
	}
}