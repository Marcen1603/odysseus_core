package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractTransportHandler implements ITransportHandler {

	private List<ITransportHandlerListener> transportHandlerListener = new ArrayList<ITransportHandlerListener>();
	private int openCounter = 0;
	
	
	@Override
	public void addListener(ITransportHandlerListener listener) {
		this.transportHandlerListener.add(listener);
	}

	@Override
	public void removeListener(ITransportHandlerListener listener) {
		this.transportHandlerListener.remove(listener);
	}
	
	public void fireProcess(ByteBuffer message){
		for (ITransportHandlerListener l: transportHandlerListener){
			l.process(message);
		}
	}
	
	final synchronized public void open() throws UnknownHostException, IOException {
		if (openCounter == 0){
			process_open();
		}
		openCounter++;		
	}
	
	abstract public void process_open() throws UnknownHostException, IOException;

	@Override
	final synchronized public void close() throws IOException {
		openCounter--;
		if (openCounter == 0){
			process_close();
		}
	}
	
	abstract public void process_close() throws IOException;
	
}
