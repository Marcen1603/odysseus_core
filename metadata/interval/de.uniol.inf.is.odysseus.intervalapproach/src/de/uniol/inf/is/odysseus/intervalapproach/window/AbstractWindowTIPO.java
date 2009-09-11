package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public abstract class AbstractWindowTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {
	protected final long windowSize;
	protected final long windowAdvance;
	private final WindowAO windowAO;
	
	public AbstractWindowTIPO(WindowAO ao) {
		this.windowSize = ao.getWindowSize();
		this.windowAdvance = ao.getWindowAdvance();
		this.windowAO = ao;
	}

	public AbstractWindowTIPO(AbstractWindowTIPO<T> window) {
		this.windowSize = window.windowSize;
		this.windowAdvance = window.windowAdvance;
		this.windowAO = window.windowAO;
	}

	public long getWindowSize() {
		return windowSize;
	}

	public long getWindowAdvance() {
		return windowAdvance;
	}
	
	public WindowAO getWindowAO() {
		return windowAO;
	}
}
