package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public class TimeSplitTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {

	private long size;

	public TimeSplitTIPO(long size) {
		this.size = size;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		long start = object.getMetadata().getStart().getMainPoint();
		long end = object.getMetadata().getStart().getMainPoint();
		long i = start;
		for (; i + size < end; i += size) {
			T newObject = (T) object.clone();
			newObject.getMetadata().setStart(new PointInTime(i));
			newObject.getMetadata().setEnd(new PointInTime(i + size));
			transfer(newObject);
		}
		T newObject = (T) object.clone();
		newObject.getMetadata().setStart(new PointInTime(i));
		transfer(newObject);
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new TimeSplitTIPO<T>(size);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof TimeSplitTIPO)) {
			return false;
		}
		TimeSplitTIPO tstipo = (TimeSplitTIPO) ipo;
		if(this.getSubscribedToSource().equals(tstipo.getSubscribedToSource()) &&
				this.size == tstipo.size) {
			return true;
		}
		return false;
	}
}
