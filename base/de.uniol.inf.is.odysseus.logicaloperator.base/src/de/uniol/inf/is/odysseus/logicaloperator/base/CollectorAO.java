package de.uniol.inf.is.odysseus.logicaloperator.base;

public class CollectorAO extends AbstractLogicalOperator {

	public CollectorAO(AbstractLogicalOperator po) {
		super(po);
		setPOName("CollectorPO");
	}

	public CollectorAO() {
		super();
		setPOName("CollectorPO");
	}

	public @Override
	CollectorAO clone() {
		return new CollectorAO(this);
	}
	

}
