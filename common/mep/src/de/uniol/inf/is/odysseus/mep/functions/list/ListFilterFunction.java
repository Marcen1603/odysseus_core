package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.MEP;

public class ListFilterFunction extends AbstractFunction<List<Object>> {

	private static final long serialVersionUID = -8269748747594828192L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.LIST }, { SDFDatatype.STRING } };

	SDFExpression expression;

	public ListFilterFunction() {
		super("filter", 2, accTypes, SDFDatatype.LIST, false);
	}

	@Override
	public List<Object> getValue() {
		if (expression == null) {
			String expr = getInputValue(1);
			init(expr);
		}
		List<Object> out = new ArrayList<Object>();
		List<Object> in = getInputValue(0);
		for (Object o : in) {
			expression.bindVariables(o);
			if (expression.getValue()) {
				out.add(o);
			}
		}

		return out;
	}

	private void init(String expr) {
		expression = new SDFExpression(expr, MEP.getInstance());
	}

}
