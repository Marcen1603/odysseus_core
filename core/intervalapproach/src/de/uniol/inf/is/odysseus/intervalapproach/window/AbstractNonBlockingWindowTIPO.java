package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

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
	
	@Override
	protected void process_next(T object, int port) {
		ITimeInterval time = object.getMetadata();
		time.setEnd(this.calcWindowEnd(time));
		this.transfer(object);
	}

	protected abstract PointInTime calcWindowEnd(ITimeInterval interval);

	@Override
	public void process_open() {
	}

	@Override
	public void process_close() {
	}

}
