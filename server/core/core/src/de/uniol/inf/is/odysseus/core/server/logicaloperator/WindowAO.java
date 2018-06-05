package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "WINDOW", category = { LogicalOperatorCategory.BASE }, doc = "use TimeWindow, ElementWindow or PredicateWindow instead", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Window+operator", hidden = true, deprecation = true)
public class WindowAO extends AbstractWindowWithWidthAO {

	private static final long serialVersionUID = 5024709707231623350L;

	@Override
	@Parameter(type = EnumParameter.class, name = "TYPE", possibleValues="getWindowTypes", deprecated=true)
	public void setWindowType(WindowType windowType) {
		super.setWindowType(windowType);
	}
	
	@Override
	public WindowType getWindowType() {
		return super.getWindowType();
	}
	
	@Override
	public List<String> getWindowTypes(){
		return WindowType.getValues();
	}
	
	public WindowAO(WindowType windowType) {
		super(windowType);
	}

	public WindowAO(AbstractWindowAO windowPO) {
		super(windowPO);
	}

	public WindowAO() {
	}

	@Override
	public WindowAO clone() {
		return new WindowAO(this);
	}
	
}
