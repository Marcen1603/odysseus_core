package de.uniol.inf.is.odysseus.wrapper.mosaik;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class MosaikTransportHandler extends AbstractTransportHandler {

	static Logger LOG = LoggerFactory.getLogger(MosaikTransportHandler.class);
	
	private MosaikHandler mosaikHandler;
	
	public MosaikTransportHandler() {
		super();
	}
	
	public MosaikTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap optionsMap) {
		super(protocolHandler, optionsMap);
		initSocketServer();
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new MosaikTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return "Mosaik";
	}
	
	private void initSocketServer() {
    	try {    	
	    	mosaikHandler = new MosaikHandler(this, 5554);
	    	Thread thread = new Thread(mosaikHandler);
	    	thread.start();
	    	thread.setName("MosaikTransportHandler");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
//		try {
//			return this.socket.getInputStream();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
//		try {
//			return this.socket.getOutputStream();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}
}
