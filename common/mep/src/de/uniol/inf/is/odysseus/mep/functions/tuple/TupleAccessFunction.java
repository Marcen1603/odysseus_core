package de.uniol.inf.is.odysseus.mep.functions.tuple;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.IHasSecondAlias;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class TupleAccessFunction extends AbstractFunction<Object> implements
		IHasAlias, IHasSecondAlias {

	private static final long serialVersionUID = -6751879119439264427L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.TUPLE }, SDFDatatype.NUMBERS };

	public TupleAccessFunction() {
		super("elementAt", 2, accTypes, null, false);
	}

	@Override
	public Object getValue() {
		Tuple<?> l = (Tuple<?>) getInputValue(0);
		Number pos = getInputValue(1);
		if ((l == null) || (pos == null)) {
			return null;
		}
		return l.getAttribute(pos.intValue());
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		int pos = Integer.parseInt(args[1].getValue().toString());
		SDFDatatype dt = args[0].getReturnType();
		SDFSchema schema = dt.getSchema();
		if (schema != null) {
			return schema.get(pos).getDatatype();
		}

		return SDFDatatype.OBJECT;
	}

	@Override
	public String getAliasName() {
		return "[]";
	}
	
	@Override
	public String getSecondAliasName() {
		return "getElement";
	}

}
