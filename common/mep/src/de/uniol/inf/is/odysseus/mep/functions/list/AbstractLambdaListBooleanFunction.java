package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.MEP;

abstract public class AbstractLambdaListBooleanFunction extends
		AbstractFunction<Boolean> {

	private static final long serialVersionUID = -8269748747594828192L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.LIST }, { SDFDatatype.STRING } };

	SDFExpression expression;

	public AbstractLambdaListBooleanFunction(String name) {
		super(name, 2, accTypes, SDFDatatype.LIST, false);
	}

	@Override
	public Boolean getValue() {
		if (expression == null) {
			String expr = getInputValue(1);
			init(expr);
		}
		List<Boolean> out = new ArrayList<>();
		List<Object> in = getInputValue(0);
		for (Object o : in) {
			if (o instanceof Tuple) {
				expression.bindVariables(((Tuple<?>) o).getAttributes());
			} else {
				expression.bindVariables(o);
			}
			out.add(expression.getValue());
		}

		return calcValue(out);
	}


	abstract protected Boolean calcValue(List<Boolean> out);

	private void init(String expr) {
		expression = new SDFExpression(expr, null, MEP.getInstance());
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		if (args != null && args.length == 2) {
			String expr = getInputValue(1);
			init(expr);
			SDFDatatype subtype = expression.getType();

			if (subtype == SDFDatatype.TUPLE) {
				List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
				for (int i = 0; i < getArity(); i++) {
					attrs.add(new SDFAttribute("_gen", "_gen_" + i,
							SDFDatatype.OBJECT));
				}
				SDFSchema subSchema = SDFSchemaFactory.createNewTupleSchema(
						"_gen", attrs);
				SDFDatatype tuple_dt = new SDFDatatype(SDFDatatype.TUPLE + "_"
						+ subtype, KindOfDatatype.TUPLE, subSchema, false);
				SDFDatatype dt = new SDFDatatype("LIST_TUPLE",
						KindOfDatatype.LIST, tuple_dt);
				return dt;
			} else {
				for (SDFDatatype d : SDFDatatype.getLists()) {
					if (d.getSubType() == subtype) {
						return d;
					}
				}
			}

			return SDFDatatype.LIST;
		}
		throw new IllegalArgumentException("Types cannot be determined with "
				+ args);
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

}
