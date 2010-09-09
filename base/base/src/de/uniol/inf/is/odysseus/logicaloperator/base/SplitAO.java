package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class SplitAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8015847502104587689L;
	List<IPredicate<?>> predicates = new ArrayList<IPredicate<?>>();
	
	public SplitAO(){
		super();
	}
	
	public SplitAO(SplitAO splitAO){
		super(splitAO);
		predicates = new ArrayList<IPredicate<?>>(splitAO.predicates);
	}

	public List<IPredicate<?>> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<IPredicate<?>> predicates) {
		this.predicates = new ArrayList<IPredicate<?>>(predicates);
	}
	
	public void addPredicate(IPredicate<?> predicate) {
		this.predicates.add(predicate);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SplitAO(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((predicates == null) ? 0 : predicates.hashCode());
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
		SplitAO other = (SplitAO) obj;
		if (predicates == null) {
			if (other.predicates != null)
				return false;
		} else if (!predicates.equals(other.predicates))
			return false;
		return true;
	}
	
}
