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
package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.SimpleColumnDefinition;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.TextEditingColumnDefinition;

class ExpressionString {
	public String expression = "";
	
	public ExpressionString( String expr ) {
		expression = expr;
	}
}

public class ExpressionsParameterEditor extends AbstractTableButtonListParameterEditor<SDFExpression, ExpressionString, String> implements IParameterEditor {

	@Override
	protected ExpressionString createNewDataRow() {
		return new ExpressionString("expression");
	}

	@Override
	protected ExpressionString convertFrom(SDFExpression element) {
		return new ExpressionString(element.toString());
	}

	@Override
	protected String convertTo(ExpressionString element) {
		return element.expression;
	}

	@Override
	protected List<SimpleColumnDefinition<ExpressionString>> createColumnDefinitions() {
		List<SimpleColumnDefinition<ExpressionString>> defs = new ArrayList<SimpleColumnDefinition<ExpressionString>>();
		
		defs.add(new TextEditingColumnDefinition<ExpressionString>("Expression") {
			@Override
			protected void setValue(ExpressionString element, String value) {
				element.expression = value;
			}

			@Override
			protected String getStringValue(ExpressionString element) {
				return element.expression;
			}
		});
		
		return defs;
	}
}
