package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "WINDOW", category={LogicalOperatorCategory.BASE}, doc="The window sets – dependent on the used parameters – the validity of the tuple. If a time based window is used, the default time granularity is in milliseconds. So, if you have another time granularity, you may use the unit-parameter (e.g. use 5 for size and SECONDS for the unit parameter) or you have to adjust the arity (e.g. use 5000 for size without the unit parameter)", hidden=true)
public class WindowAO extends AbstractWindowWithWidthAO {

	private static final long serialVersionUID = 5024709707231623350L;

	@Parameter(type = EnumParameter.class, name = "TYPE", possibleValues="getWindowTypes")
	public void setWindowType(WindowType windowType) {
		super.setWindowType(windowType);
	}
	
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
