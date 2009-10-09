package de.uniol.inf.is.odysseus.logicaloperator.base;

public class CollectorAO extends AbstractLogicalOperator {

	public CollectorAO(AbstractLogicalOperator po) {
		super(po);
		setName("CollectorPO");
	}

	public CollectorAO() {
		super();
		setName("CollectorPO");
	}

	public @Override
	CollectorAO clone() {
		return new CollectorAO(this);
	}
	

}
