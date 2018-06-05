package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods;

import java.util.List;

public interface IEventMethod {
	public boolean isAO();
	public boolean isOverride();
	public String getEventName();
	public String getMethodName();
	public String getReturnType();
	public List<EventMethodParameter> getEventMethodParameters();
}
