package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.ParseException;

abstract public class AbstractLambdaListFunction extends AbstractFunction<List<Object>> {

	private static final long serialVersionUID = -8269748747594828192L;

	SDFExpression expression;

	private int[] positions;

	public AbstractLambdaListFunction(String name, int arity, SDFDatatype[][] accTypes) {
		super(name, arity, accTypes, SDFDatatype.LIST, false);
	}

	@Override
	public List<Object> getValue() {
		List<Object> out = new ArrayList<Object>();
		final List<Object> in = getInputValue(0);
	
		for (Object o : in) {
			Object[] objects = new Object[positions.length];

			if (o instanceof Tuple) {
				for (int i = 0; i < positions.length; i++) {
					objects[i] = ((Tuple<?>) o).getAttribute(positions[i]);
				}

			} else {
				objects[0] = o;
			}

			if (getArity() == 3) {
				Tuple<?> addValues = getInputValue(1);
				int addValueCounter = 0;
				for (int i=0;i<objects.length;i++) {
					if (objects[i] == null) {
						objects[i] = addValues.getAttribute(addValueCounter++);
					}		
				}
			}

			expression.bindVariables(objects);

			fillReturnList(out, o);
		}

		return out;
	}

	abstract protected void fillReturnList(List<Object> out, Object o);

	private void init(String expr, IMepExpression<?> inputExpression) {
		IAttributeResolver attributeResolver = null;
		// Check for subtypes
		if (inputExpression.getReturnType().getSubType() != null && inputExpression.getReturnType().getSubType().isTuple()) {
			attributeResolver = new DirectAttributeResolver(inputExpression.getReturnType().getSchema());
		}
		try {
			expression = new SDFExpression(expr, attributeResolver, MEP.getInstance());
		}catch(Exception e) {
			e.printStackTrace();
			throw new ParseException("Error parsing "+expr);
		}
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		if (args != null && (args.length == 2 || args.length == 3)) {
			String expr = args.length == 2 ? getInputValue(1) : getInputValue(2);
			init(expr, args[0]);

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
							if (args.length == 3) {
								positions[pos] = -1; 
							} else {
								throw new IllegalArgumentException(
										"Attribute " + in + " cannot be found in input schema");
							}
						}
						positions[pos] = index;
						pos++;
					}
				} else {
					// When no schema --> use order of attributes
					for (int i = 0; i < positions.length; i++) {
						positions[i] = i;
					}
				}

			}

			SDFDatatype subtype = expression.getType();

			if (subtype.isTuple()) {
				SDFSchema subSchema = null;
				if (expression.getSchema() == null || expression.getSchema().isEmpty()) {
					List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
					for (int i = 0; i < getArity(); i++) {
						attrs.add(new SDFAttribute("_gen", "_gen_" + i, SDFDatatype.OBJECT));
					}
					subSchema = SDFSchemaFactory.createNewTupleSchema("_gen", attrs);
				}else {
					subSchema = expression.getSchema().get(0);
				}
				SDFDatatype tuple_dt = new SDFDatatype(SDFDatatype.TUPLE + "_" + subtype, KindOfDatatype.TUPLE,
						subSchema, false);

				SDFDatatype dt = new SDFDatatype("LIST_TUPLE", KindOfDatatype.LIST, tuple_dt);
				return dt;
			} 
				
			if (subtype.hasSubType() && subtype.getSubType().isTuple()) {
					return  new SDFDatatype("LIST_TUPLE", KindOfDatatype.LIST,subtype.getSubType(), subtype.getSchema());					
			}
				
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

}
