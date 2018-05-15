package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.MEP;

public class ListForeachPairFunction extends AbstractFunction<List<Object>> {

	private static final long serialVersionUID = -8269748747594828192L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.LIST },
			{ SDFDatatype.STRING } };

	SDFExpression expression;

	public ListForeachPairFunction() {
		super("foreachpair", 2, accTypes, SDFDatatype.LIST, false);
	}

	@Override
	public List<Object> getValue() {
		if (expression == null) {
			String expr = getInputValue(1);
			init(expr);
		}
		List<Object> out = new ArrayList<Object>();
		List<Object> in = getInputValue(0);

		// Calc pairs and process for pairs

		for (int i = 0; i < in.size(); i++) {
			for (int j = i + 1; j < in.size(); j++) {
				Object[] values = new Object[2];
				values[0] = in.get(i);
				values[1] = in.get(j);
				expression.bindVariables(values);
				out.add(expression.getValue());
			}
		}
		return out;
	}

	private void init(String expr) {
		expression = new SDFExpression(expr, null, MEP.getInstance());
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		if (args != null && args.length == 2) {
			String expr = getInputValue(1);
			init(expr);
			SDFDatatype subtype = expression.getType();

			for (SDFDatatype d : SDFDatatype.getLists()) {
				if (d.getSubType() == subtype) {
					return d;
				}
			}

			return SDFDatatype.LIST;
		}
		throw new IllegalArgumentException("Types cannot be determined with " + args);
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

	public static void main(String[] args) {

	}
}
