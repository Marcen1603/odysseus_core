package de.uniol.inf.is.odysseus.peer.smarthome.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class ActivityConfigurationTransportHandler extends
AbstractSimplePullTransportHandler<Tuple<?>> {

	private static final String ENTITY_NAME = "entity";
	private static final String ACTIVITY_NAME = "activity";
	public static final String NAME = "ActivityConfiguration";
	private String entityName;
	private String activityName;

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Tuple<?> getNext() {
		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(2, true);
		
		tuple.setAttribute(0, getEntityName());
		tuple.setAttribute(1, getActivityName());
		
		return tuple;
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		ActivityConfigurationTransportHandler tHandler = new ActivityConfigurationTransportHandler();

		if (options.containsKey(ENTITY_NAME)) {
			tHandler.setEntityName(options.get(ENTITY_NAME));
		}
		
		if (options.containsKey(ACTIVITY_NAME)) {
			tHandler.setActivityName(options.get(ACTIVITY_NAME));
		}

		protocolHandler.setTransportHandler(tHandler);

		return tHandler;
	}


	private void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	private void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getActivityName() {
		return activityName;
	}
}
