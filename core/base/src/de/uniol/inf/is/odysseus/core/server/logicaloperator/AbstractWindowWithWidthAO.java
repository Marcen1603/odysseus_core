package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;

abstract public class AbstractWindowWithWidthAO extends AbstractWindowAO {

	private static final long serialVersionUID = -6061804236220025106L;
	
	
	@Override
	@Parameter(type = LongParameter.class, name = "SIZE", optional = false)
	public void setWindowSize(long windowSize) {
		super.setWindowSize(windowSize);
	}

	@Override
	@Parameter(type = LongParameter.class, name = "SLIDE", optional = true)
	public void setWindowSlide(long slide) {
		super.setWindowSlide(slide);
	}

	@Override
	@Parameter(type = LongParameter.class, name = "ADVANCE", optional = true)
	public void setWindowAdvance(long windowAdvance) {
		super.setWindowAdvance(windowAdvance);
	}

	
	public AbstractWindowWithWidthAO(WindowType windowType) {
		super(windowType);
	}

	public AbstractWindowWithWidthAO(AbstractWindowAO windowPO) {
		super(windowPO);
	}
	
	public AbstractWindowWithWidthAO(){
		super();
	}
	

}
