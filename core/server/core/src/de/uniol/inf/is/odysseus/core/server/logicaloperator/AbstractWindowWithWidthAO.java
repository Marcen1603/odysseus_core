package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

abstract public class AbstractWindowWithWidthAO extends AbstractWindowAO {

	private static final long serialVersionUID = -6061804236220025106L;
	
	
	@Override
	@Parameter(type = TimeParameter.class, name = "SIZE", optional = false)
	public void setWindowSize(TimeValueItem windowSize) {
		super.setWindowSize(windowSize);
	}
	
	@Override
	@Parameter(type = TimeParameter.class, name = "SLIDE", optional = true)
	public void setWindowSlide(TimeValueItem slide) {
		super.setWindowSlide(slide);
	}

	@Override
	@Parameter(type = TimeParameter.class, name = "ADVANCE", optional = true)
	public void setWindowAdvance(TimeValueItem windowAdvance) {
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
