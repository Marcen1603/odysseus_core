package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.IEventMethod;

public class AOInitEventMethod implements IEventMethod {

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
		return "ao_init";
	}

	@Override
	public String getMethodName() {
		return "initialize";
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
