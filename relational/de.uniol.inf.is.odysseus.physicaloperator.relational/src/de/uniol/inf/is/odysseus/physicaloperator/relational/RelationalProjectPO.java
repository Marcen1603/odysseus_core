package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Jonas Jacobi
 */
public class RelationalProjectPO<T extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	private int[] restrictList;

	public RelationalProjectPO(int[] restrictList) {
		this.restrictList = restrictList;
		final RelationalProjectPO<T> t = this;
//		this.addMonitoringData("selectivity", new StaticValueMonitoringData<Double>(t,
//				"selectivity", 1d));
	}

	public RelationalProjectPO(RelationalProjectPO<T> relationalProjectPO) {
		super();
		int length = relationalProjectPO.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(relationalProjectPO.restrictList, 0, restrictList, 0, length);
		final RelationalProjectPO<T> t = this;
//		this.addMonitoringData("selectivity", new StaticValueMonitoringData<Double>(t,
//				"selectivity", 1d));
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	final protected void process_next(RelationalTuple<T> object, int port) {
		try {
			transfer(object.restrict(this.restrictList, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RelationalProjectPO<T> clone() {
		return new RelationalProjectPO<T>(this);
	}
}
