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

abstract public class AbstractLambdaListFunction extends AbstractFunction<List<Object>> {

	private static final long serialVersionUID = -8269748747594828192L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.LIST },
			{ SDFDatatype.STRING } };

	SDFExpression expression;

	private int[] positions;

	public AbstractLambdaListFunction(String name) {
		super(name, 2, accTypes, SDFDatatype.LIST, false);
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
			if (o instanceof Tuple) {
				// TODO: use RelationalExpression
				Object[] objects = new Object[positions.length];
				for (int i=0;i<positions.length;i++) {
					objects[i] = ((Tuple<?>) o).getAttribute(positions[i]);
				}
				expression.bindVariables(objects);
			} else {
				expression.bindVariables(o);
			}
			fillReturnList(out, o);
		}

		return out;
	}

	abstract protected void fillReturnList(List<Object> out, Object o);

	private void init(String expr) {
		expression = new SDFExpression(expr, null, MEP.getInstance());
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		if (args != null && args.length == 2) {
			String expr = getInputValue(1);
			init(expr);

			// Test if input is Tuple with schema
			SDFDatatype inputType = args[0].getReturnType();
			List<SDFAttribute> attributes = expression.getAllAttributes();
			this.positions = new int[attributes.size()];

			if (inputType != null && inputType.getSubType() != null && inputType.getSubType().isTuple()) {
				SDFSchema subSchema = inputType.getSchema();
				if (subSchema != null) {
					int pos = 0;
					for (SDFAttribute in : attributes) {
						SDFAttribute a = subSchema.findAttribute(in.getAttributeName());
						int index = subSchema.indexOf(a);
						if (index == -1) {
							throw new IllegalArgumentException("Attribute " + a + " cannot be found in schema");
						}
						positions[pos] = index;
						pos++;
					}
				}else{
					// When no schema --> use order of attributes
					for(int i=0;i<positions.length; i++){
						positions[i] = i;
					}
				}

			}

			SDFDatatype subtype = expression.getType();

			if (subtype == SDFDatatype.TUPLE) {
				List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
				for (int i = 0; i < getArity(); i++) {
					attrs.add(new SDFAttribute("_gen", "_gen_" + i, SDFDatatype.OBJECT));
				}
				SDFSchema subSchema = SDFSchemaFactory.createNewTupleSchema("_gen", attrs);
				SDFDatatype tuple_dt = new SDFDatatype(SDFDatatype.TUPLE + "_" + subtype, KindOfDatatype.TUPLE,
						subSchema, false);
				SDFDatatype dt = new SDFDatatype("LIST_TUPLE", KindOfDatatype.LIST, tuple_dt);
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
		throw new IllegalArgumentException("Types cannot be determined with " + args);
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

}
