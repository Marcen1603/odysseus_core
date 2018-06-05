package de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.xtext.common.types.JvmFormalParameter;

import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.AOInitEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.OutputSchemaEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.POInitEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.ProcessCloseMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.ProcessDoneMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.ProcessDoneMethod2;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.ProcessOpenMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.PunctuationEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.SourceSubscribedEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.SourceUnsubscribedEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.StreamObjectEventMethod;
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.impl.TupleEventMethod;

public class EventMethodsFactory {
	private Map<String , IEventMethod> aoEventMethods = new HashMap<>();
	private Map<String , IEventMethod> poEventMethods = new HashMap<>();
	private Set<IEventMethod> eventMethods = new HashSet<>();

	private static EventMethodsFactory instance;
	
	public static EventMethodsFactory getInstance() {
		if (instance == null) {
			instance = new EventMethodsFactory();
		}
		return instance;
	}
	
	private EventMethodsFactory() {
		registerDefaultMethods();
	}
	
	private void registerDefaultMethods() {
		this.addEventMethod(new AOInitEventMethod());
		this.addEventMethod(new OutputSchemaEventMethod());
		this.addEventMethod(new ProcessCloseMethod());
		this.addEventMethod(new ProcessDoneMethod());
		this.addEventMethod(new ProcessDoneMethod2());
		this.addEventMethod(new ProcessOpenMethod());
		this.addEventMethod(new PunctuationEventMethod());
		this.addEventMethod(new SourceSubscribedEventMethod());
		this.addEventMethod(new SourceUnsubscribedEventMethod());
		this.addEventMethod(new StreamObjectEventMethod());
		this.addEventMethod(new TupleEventMethod());
		this.addEventMethod(new POInitEventMethod());
	}
	
	private void addEventMethod(IEventMethod eventMethod) {
		if (eventMethod.isAO()) {
			aoEventMethods.put(createKey(eventMethod.getEventName(), eventMethod.getEventMethodParameters().size()), eventMethod);
		} else {
			poEventMethods.put(createKey(eventMethod.getEventName(), eventMethod.getEventMethodParameters().size()), eventMethod);
		}
		eventMethods.add(eventMethod);
	}
	
	private String createKey(String name, int args) {
		return name+args;
	}
	
	public boolean hasEventMethod(boolean ao, String name, List<JvmFormalParameter> parameters) {
		return getEventMethod(ao, name, parameters) != null;
	}
	
	public IEventMethod getEventMethod(boolean ao, String name, List<JvmFormalParameter> parameters) {
		if (ao) {
			return aoEventMethods.get(createKey(name, parameters.size()));
		} else {
			return poEventMethods.get(createKey(name, parameters.size()));
		}
	}

	public Collection<IEventMethod> getAllEventMethods() {
		return eventMethods;
	}

}
