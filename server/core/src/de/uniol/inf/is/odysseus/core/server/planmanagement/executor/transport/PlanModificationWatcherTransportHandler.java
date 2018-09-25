package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanManager;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;

public class PlanModificationWatcherTransportHandler extends
		AbstractPushTransportHandler implements IPlanModificationListener {
	
	private static final String NAME = "PlanModificationWatcher";
	private IPlanManager planManager;
	private static SDFSchema schema;
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("", "ts",
				SDFDatatype.START_TIMESTAMP));
		attributes.add(new SDFAttribute("", "queryID",
				SDFDatatype.INTEGER));
		attributes.add(new SDFAttribute("", "eventType",
				SDFDatatype.STRING));
		attributes.add(new SDFAttribute("", "user",
				SDFDatatype.STRING));

		schema = SDFSchemaFactory.createNewSchema("",
				Tuple.class, attributes);	
	}
	
	final private List<Tuple<IMetaAttribute>> outputBuffer = new ArrayList<>();
	
	private Thread runner; 

	public PlanModificationWatcherTransportHandler() {
		super();
		setSchema(schema);
		planManager = null;
	}

	public PlanModificationWatcherTransportHandler(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		setSchema(schema);
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
		// Different threads can call planModificationEvent --> Assure correct ordering by sender threat
		runner = new Thread("PlanModificationWatcherTransportHandlerThread"){
			@Override
			public void run() {
				while (!interrupted()){
					synchronized (outputBuffer) {
						if (outputBuffer.size() > 0){
							PlanModificationWatcherTransportHandler.this.fireProcess(outputBuffer.remove(0));
						}else{
							try {
								outputBuffer.wait(1000);
							} catch (InterruptedException e) {
							}
						}
						
					}
				}
				
			};
		};
		runner.start();
		planManager.addPlanModificationListener(this);
	}

	@Override
	public void processOutOpen() throws IOException {
	}

	@Override
	public void processInClose() throws IOException {
		planManager.removePlanModificationListener(this);
		runner.interrupt();
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
	public synchronized void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs instanceof QueryPlanModificationEvent) {
			QueryPlanModificationEvent e = (QueryPlanModificationEvent) eventArgs;
			Tuple<IMetaAttribute> output = new Tuple<IMetaAttribute>(4, false);
			output.setAttribute(0, System.currentTimeMillis());
			output.setAttribute(1, e.getValue().getID());
			output.setAttribute(2, e.getEventType().toString());
			// TODO: Define User Type
			output.setAttribute(3, e.getValue().getSession().getUser().getName());
			// Different threads can call planModificationEvent --> Assure correct ordering by sender threat
			synchronized (outputBuffer) {
				outputBuffer.add(output);
				outputBuffer.notifyAll();
			}
		}
	}
	

}
