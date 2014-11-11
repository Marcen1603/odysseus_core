package de.uniol.inf.is.odysseus.wrapper.rpi.gpio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class RPiGPIOPushTransportHandler extends AbstractTransportHandler  {

	//Example PushTransportHandler: OPCDATransportHandler<T>
	
	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "RPiGPIOPush";
	}

	@Override
	public void processInOpen() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processInClose() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private void testFire(){
		Tuple<?> tuple = new Tuple(1, false);
		tuple.setAttribute(0, "pinNumber");
    	//tuple.setAttribute(1, Long.parseLong(1));
    	//TODO: 
    	//tuple.setAttribute(2, System.ut);
    	//Starttimestamp
    	
		//TODO:
		fireProcess(tuple);
	}
}
