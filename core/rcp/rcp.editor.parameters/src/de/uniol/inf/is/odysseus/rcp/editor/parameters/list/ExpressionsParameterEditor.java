package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.SimpleColumnDefinition;
import de.uniol.inf.is.odysseus.rcp.editor.parameters.editing.TextEditingColumnDefinition;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

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
