package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.IEventMethod;

public class OutputSchemaEventMethod implements IEventMethod {

	@Override
	public boolean isAO() {
		return true;
	}

	@Override
	public boolean isOverride() {
		return true;
	}

	@Override
	public String getEventName() {
		return "createOutputSchema";
	}

	@Override
	public String getMethodName() {
		return "getOutputSchemaIntern";
	}

	@Override
	public List<EventMethodParameter> getEventMethodParameters() {
		List<EventMethodParameter> result =  new ArrayList<>();
		result.add(new EventMethodParameter("int", "port"));
		return result;
	}

	@Override
	public String getReturnType() {
		return "SDFSchema";
	}

}
