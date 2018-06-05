package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.IEventMethod;

public class ProcessDoneMethod implements IEventMethod {

	@Override
	public boolean isAO() {
		return false;
	}

	@Override
	public boolean isOverride() {
		return true;
	}

	@Override
	public String getEventName() {
		return "processDone";
	}

	@Override
	public String getMethodName() {
		return "process_done";
	}

	@Override
	public List<EventMethodParameter> getEventMethodParameters() {
		return new ArrayList<>();
	}

	@Override
	public String getReturnType() {
		return null;
	}

}
