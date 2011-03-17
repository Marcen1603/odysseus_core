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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class SDFExpressionParameter extends AbstractParameter<SDFExpression> {

	private static final long serialVersionUID = -3129785072529123574L;

	@Override
	protected void internalAssignment() {
		if (getAttributeResolver() != null) {
			setValue(new SDFExpression("", (String)inputValue,
					getAttributeResolver()));
		} else {
			throw new RuntimeException(
					"missing expression or attribute resolver");
		}
	}

}
