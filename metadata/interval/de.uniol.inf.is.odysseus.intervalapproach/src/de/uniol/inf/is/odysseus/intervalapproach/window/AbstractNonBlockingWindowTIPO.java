package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode;

public abstract class AbstractNonBlockingWindowTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends AbstractWindowTIPO<T> {


	public AbstractNonBlockingWindowTIPO(WindowAO algebraOp) {
		super(algebraOp);
	}

	public AbstractNonBlockingWindowTIPO(AbstractNonBlockingWindowTIPO<T> po) {
		super(po);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		ITimeInterval time = object.getMetadata();
		time.setEnd(this.calcWindowEnd(time));
		this.transfer(object);
	}

	protected abstract PointInTime calcWindowEnd(ITimeInterval interval);

	public void process_open() {
	}

	public void process_close() {
	}

}
