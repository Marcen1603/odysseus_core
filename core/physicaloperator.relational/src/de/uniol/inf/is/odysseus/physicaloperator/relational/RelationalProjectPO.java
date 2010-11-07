package de.uniol.inf.is.odysseus.physicaloperator.relational;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Jonas Jacobi
 */
public class RelationalProjectPO<T extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	Logger logger = LoggerFactory.getLogger(RelationalProjectPO.class);
	
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
			// System.out.println("RelationalTuple "+this+" "+object);
			RelationalTuple<T> out = object.restrict(this.restrictList, false);
		//	logger.debug(this+" transferNext() "+object);
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
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}	
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof RelationalProjectPO)) {
			return false;
		}
		RelationalProjectPO<T> rppo = (RelationalProjectPO<T>) ipo;
		if(this.hasSameSources(ipo) &&
				this.restrictList.length == rppo.restrictList.length) {
			for(int i = 0; i<this.restrictList.length; i++) {
				if(this.restrictList[i] != rppo.restrictList[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
