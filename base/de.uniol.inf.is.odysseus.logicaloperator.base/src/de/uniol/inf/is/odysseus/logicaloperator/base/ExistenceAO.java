package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
public class ExistenceAO extends BinaryLogicalOp implements Cloneable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExistenceAO other = (ExistenceAO) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	private static final long serialVersionUID = -7471367531511438478L;

	public static enum Type {
		EXISTS, NOT_EXISTS
	};

	private Type type;

	public ExistenceAO() {
	}

	public ExistenceAO(ExistenceAO ao) {
		super(ao);
		this.type = ao.type;
	}

	@Override
	public ExistenceAO clone() {
		return new ExistenceAO(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPredicate(IPredicate predicate) {
		super.setPredicate(predicate);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(LEFT);
	}
	
}
