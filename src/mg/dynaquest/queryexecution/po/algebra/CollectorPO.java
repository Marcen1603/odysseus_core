package mg.dynaquest.queryexecution.po.algebra;

public class CollectorPO extends AlgebraPO {

	public CollectorPO(AlgebraPO po) {
		super(po);
		setPOName("CollectorPO");
	}

	public CollectorPO() {
		super();
		setPOName("CollectorPO");
	}

	public @Override
	SupportsCloneMe cloneMe() {
		return new CollectorPO(this);
	}
	

}
