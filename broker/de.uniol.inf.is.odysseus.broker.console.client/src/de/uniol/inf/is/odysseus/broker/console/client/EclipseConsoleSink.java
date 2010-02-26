package de.uniol.inf.is.odysseus.broker.console.client;

import net.java.dev.jaxb.array.StringArray;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.broker.console.ConsoleServer;
import de.uniol.inf.is.odysseus.broker.console.ConsoleServerService;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class EclipseConsoleSink extends AbstractSink<Object> {

	private int port;
	private ConsoleServerService service;
	private ConsoleServer server;
	private boolean isConnected = false;

	public EclipseConsoleSink() {

	}

	private boolean connect(String[] attributes) {
		try {
			this.service = new ConsoleServerService();
			this.server = service.getConsoleServerPort();
			StringArray attArr = new StringArray();
			for (int i = 1; i <= attributes.length; i++) {
				attArr.getItem().add("Attribute " + i);
			}
			this.port = server.registerView(attArr);
			if(this.port==-1){
				return false;
			}else{
				return true;
			}			
		} catch (Exception e) {
			if(isConnected){
			System.err
					.println("The Console Output can not be reached. May be the plugin is missing?");
			}
			return false;
		}
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {	
		StringArray values = new StringArray();
		// hack because unable to access to relationaltuple...
		String objStr = object.toString();
		String[] parts = objStr.split("\\|");

		if (!isConnected) {
			isConnected = connect(parts);
		}

		if (isConnected) {
			try {
				for (String part : parts) {
					values.getItem().add(part);
				}
				server.sendTuple(this.port, values);

			} catch (Exception e) {
				LoggerFactory.getLogger(EclipseConsoleSink.class).warn(
						"Output Console closed");
				this.isConnected = false;
			}
		}

	}

	@Override
	public EclipseConsoleSink clone() throws CloneNotSupportedException {
		EclipseConsoleSink newSink = new EclipseConsoleSink();
		return newSink;
	}

}
