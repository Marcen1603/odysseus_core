package de.uniol.inf.is.odysseus.broker.console.client;

import net.java.dev.jaxb.array.StringArray;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.broker.console.ConsoleServer;
import de.uniol.inf.is.odysseus.broker.console.ConsoleServerService;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class EclipseConsoleSink extends AbstractSink<Object> {

	private int port;
	private ConsoleServerService service;
	private ConsoleServer server;
	private boolean isConnected = false;

	public EclipseConsoleSink() {

	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		super.setOutputSchema(outputSchema);
		String[] attrNames = new String[outputSchema.getAttributeCount()];
		for (int i = 0; i < outputSchema.getAttributeCount(); i++) {
			attrNames[i] = outputSchema.getAttribute(i).getAttributeName();
		}
		if(!isConnected){
			connect(attrNames);
		}
	}

	private void connect(String[] attributes) {
		try {			
			this.service = new ConsoleServerService();
			this.server = service.getConsoleServerPort();
			StringArray attArr = new StringArray();
			for (int i = 0; i < attributes.length; i++) {
				attArr.getItem().add(attributes[i]);
			}
			this.port = server.registerView(attArr);
			this.isConnected = true;

		} catch (Exception e) {
			System.err
					.println("The Console Output can not be reached. May be the plugin is missing or view is not opened?");	
			System.err.println("Caused by: "+e.getMessage());
			e.printStackTrace();
			this.isConnected = false;
		}
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {		
		if (isConnected) {
			StringArray values = new StringArray();
			// hack because unable to access to relationaltuple...
			String objStr = object.toString();
			String[] parts = objStr.split("\\|");

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
	public void subscribeToSource(ISource<? extends Object> source,
			int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		super.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
		this.setOutputSchema(schema);
	}

	@Override
	public EclipseConsoleSink clone()  {
		EclipseConsoleSink newSink = new EclipseConsoleSink();
		return newSink;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EclipseConsoleSink other = (EclipseConsoleSink) obj;
		if (port != other.port)
			return false;
		return true;
	}

}
