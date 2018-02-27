package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public abstract class AbstractListPosElementFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = 2477719899607385117L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.getLists() };

	public AbstractListPosElementFunction(String name) {
		super(name, 1, accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	final public Object getValue() {
		List<Object> l = getInputValue(0);
		int pos = getPos(l);
		if (pos >=0 && pos < l.size()) {
			return l.get(pos);
		} else {
			return null;
		}
	}

	abstract protected int getPos(List<Object> l);

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		if (args != null) {
			return args[0].getReturnType().getSubType();
		}
		throw new IllegalArgumentException("Types cannot be determined with " + args);
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

}
