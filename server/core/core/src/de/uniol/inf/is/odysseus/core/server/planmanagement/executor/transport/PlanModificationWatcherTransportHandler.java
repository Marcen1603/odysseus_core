package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.transport;

import java.io.IOException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanManager;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;

public class PlanModificationWatcherTransportHandler extends
		AbstractPushTransportHandler implements IPlanModificationListener {

	private static final String NAME = "PlanModificationWatcher";
	private IPlanManager planManager;

	public PlanModificationWatcherTransportHandler() {
		super();
		planManager = null;
	}

	public PlanModificationWatcherTransportHandler(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new PlanModificationWatcherTransportHandler(protocolHandler,
				options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		if (planManager == null){
			if (getExecutor() instanceof IPlanManager) {
				planManager = (IPlanManager) getExecutor();
			} else {
				throw new RuntimeException(NAME
						+ " cannot be used on this site. Must be on server!");
			}
		}
		planManager.addPlanModificationListener(this);
	}

	@Override
	public void processOutOpen() throws IOException {
	}

	@Override
	public void processInClose() throws IOException {
		planManager.removePlanModificationListener(this);
	}

	@Override
	public void processOutClose() throws IOException {
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return true;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs instanceof QueryPlanModificationEvent) {
			QueryPlanModificationEvent e = (QueryPlanModificationEvent) eventArgs;
			Tuple<IMetaAttribute> output = new Tuple<IMetaAttribute>(3, false);
			output.setAttribute(0, System.currentTimeMillis());
			output.setAttribute(1, e.getValue().getID());
			output.setAttribute(2, e.getEventType().toString());
			fireProcess(output);
		}
	}

}
