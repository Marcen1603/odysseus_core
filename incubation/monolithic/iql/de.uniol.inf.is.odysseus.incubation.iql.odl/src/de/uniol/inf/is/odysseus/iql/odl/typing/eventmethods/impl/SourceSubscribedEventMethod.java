package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodParameter;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.IEventMethod;

public class SourceSubscribedEventMethod implements IEventMethod {

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
		return "sourceSubscribed";
	}

	@Override
	public String getMethodName() {
		return "newSourceSubscribed";
	}

	@Override
	public List<EventMethodParameter> getEventMethodParameters() {
		List<EventMethodParameter> result =  new ArrayList<>();
		result.add(new EventMethodParameter("AbstractPhysicalSubscription", "subscription"));
		return result;
	}

	@Override
	public String getReturnType() {
		return null;
	}

}
