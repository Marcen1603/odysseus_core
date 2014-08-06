package de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator.access;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;

import com.codeminders.hidapi.ClassPathLibraryLoader;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IIteratable;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class Temper1TransportHandler extends AbstractTransportHandler implements IIteratable<Tuple<?>>{

	private static final String NAME = "Temper1";
	private static final Random RAND = new Random();
	
	static {
    	ClassPathLibraryLoader.loadNativeHIDLibrary();
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		Temper1TransportHandler tHandler = new Temper1TransportHandler();
		
		protocolHandler.setTransportHandler(tHandler);
		
		return tHandler;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
    private static float readDevice() {
    	return 20f + ( 10 * RAND.nextFloat());
    }

	@Override
	public boolean hasNext() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getNext() {
		Tuple<?> tuple = new Tuple(1, false);
		tuple.setAttribute(0, readDevice());
		return tuple;
	}
	
	@Override
	public void processInOpen() throws IOException {
	}

	@Override
	public void processOutOpen() throws IOException {
	}

	@Override
	public void processInClose() throws IOException {
	}

	@Override
	public void processOutClose() throws IOException {
	}

	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
