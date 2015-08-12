package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.IEventMethod;

public class TupleEventMethod implements IEventMethod {

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
		return "processNext";
	}

	@Override
	public String getMethodName() {
		return "process_next";
	}

	@Override
	public List<EventMethodParameter> getEventMethodParameters() {
		List<EventMethodParameter> result =  new ArrayList<>();
		result.add(new EventMethodParameter("Tuple", "tuple"));
		result.add(new EventMethodParameter("int", "port"));
		return result;
	}

	@Override
	public String getReturnType() {
		return null;
	}

}