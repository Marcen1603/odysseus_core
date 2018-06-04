package de.uniol.inf.is.odysseus.mep.functions.tuple;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class TupleLastFunction extends AbstractFunction<Object>  {

	private static final long serialVersionUID = -6751879119439264427L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.TUPLE }};

	public TupleLastFunction() {
		super("last", 1, accTypes, null, false);
	}

	@Override
	public Object getValue() {
		Tuple<?> l = (Tuple<?>) getInputValue(0);
		if (l == null) {
			return null;
		}
		return l.getAttribute(l.size());
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		SDFDatatype dt = args[0].getReturnType();
		SDFSchema schema = dt.getSchema();
		if (schema != null) {
			return schema.get(schema.size()).getDatatype();
		}

		return SDFDatatype.OBJECT;
	}


}
