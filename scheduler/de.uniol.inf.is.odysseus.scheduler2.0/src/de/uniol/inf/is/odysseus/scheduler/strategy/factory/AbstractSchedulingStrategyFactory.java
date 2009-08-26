package de.uniol.inf.is.odysseus.scheduler.strategy.factory;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

public abstract class AbstractSchedulingStrategyFactory implements
		ISchedulingStrategyFactory {

	String name = null;
	
	protected void activate(ComponentContext context){
		setName(context.getProperties());
	}
	
	protected void setName(Dictionary properties){
		name = ((String)properties.get("component.readableName"));
		if (name == null){
			name = ((String)properties.get("component.name"));
		}
	}
	
	@Override
	public String getName() {
		return name;
	}

}
