package de.uniol.inf.is.odysseus.logicaloperator.base;

public class DuplicateEleminationAO extends UnaryLogicalOp {

	public DuplicateEleminationAO(DuplicateEleminationAO eleminationAO) {
		super(eleminationAO);
	}

	@Override
	public DuplicateEleminationAO clone() {
		return new DuplicateEleminationAO(this);
	}

}
