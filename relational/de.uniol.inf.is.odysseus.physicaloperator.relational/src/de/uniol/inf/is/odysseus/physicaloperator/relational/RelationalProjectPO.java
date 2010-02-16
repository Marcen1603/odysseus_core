package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.PointInTime;
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
	}

	public RelationalProjectPO(RelationalProjectPO<T> relationalProjectPO) {
		super();
		int length = relationalProjectPO.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(relationalProjectPO.restrictList, 0, restrictList, 0, length);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	final protected void process_next(RelationalTuple<T> object, int port) {
		try {
			RelationalTuple<T> out = object.restrict(this.restrictList, false);
			transfer(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RelationalProjectPO<T> clone() {
		return new RelationalProjectPO<T>(this);
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp) {
		sendPunctuation(timestamp);
	}	
	
}
