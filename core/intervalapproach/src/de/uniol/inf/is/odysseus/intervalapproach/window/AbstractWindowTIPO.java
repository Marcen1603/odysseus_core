package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IWindow;

public abstract class AbstractWindowTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends AbstractPipe<T, T> implements IWindow {
	protected final long windowSize;
	protected final long windowAdvance;
	protected final WindowAO windowAO;
	
	public AbstractWindowTIPO(WindowAO ao) {
		this.windowSize = ao.getWindowSize();
		this.windowAdvance = ao.getWindowAdvance();
		this.windowAO = ao;
		setName(getName()+" s="+windowSize+" a="+windowAdvance);
	}

	public AbstractWindowTIPO(AbstractWindowTIPO<T> window) {
		this.windowSize = window.windowSize;
		this.windowAdvance = window.windowAdvance;
		this.windowAO = window.windowAO.clone();
	}

	@Override
	public long getWindowSize() {
		return windowSize;
	}

	public long getWindowAdvance() {
		return windowAdvance;
	}
	
	public WindowAO getWindowAO() {
		return windowAO;
	}
	
	@Override
	public WindowType getWindowType() {
		switch (this.windowAO.getWindowType()) {
		case PERIODIC_TIME_WINDOW:
		case SLIDING_TIME_WINDOW:
		case JUMPING_TIME_WINDOW:
		case FIXED_TIME_WINDOW:
			return WindowType.TIME_BASED;
		case PERIODIC_TUPLE_WINDOW:
		case SLIDING_TUPLE_WINDOW:
		case JUMPING_TUPLE_WINDOW:
			return WindowType.ELEMENT_BASED;
		default:
			return WindowType.OTHER;
		}
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}
	
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof AbstractWindowTIPO) || !this.getClass().toString().equals(ipo.getClass().toString())) {
			return false;
		}
		AbstractWindowTIPO awtipo = (AbstractWindowTIPO) ipo;
		if(this.getSubscribedToSource().equals(awtipo.getSubscribedToSource()) &&
				this.windowAO.equals(awtipo.getWindowAO())) {
			return true;
		}
		return false;
	}
}
