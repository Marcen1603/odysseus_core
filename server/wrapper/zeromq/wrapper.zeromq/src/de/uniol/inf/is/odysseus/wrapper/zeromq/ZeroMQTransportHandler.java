package de.uniol.inf.is.odysseus.wrapper.zeromq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.zeromq.consumer.ZMQPublisher;

public class ZeroMQTransportHandler extends AbstractTransportHandler {

	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String NAME = "ZeroMQ";

	private String host;
	private int port;
	
	private Context context;
	private Socket publisher;
	public Socket subscriber;
	protected BufferedReader reader;
	
	public ZeroMQTransportHandler(){
	}
	
	public ZeroMQTransportHandler(IProtocolHandler<?> protocolHandler,
			Map<String, String> options) {
		super(protocolHandler);
		init(options);
	}

	private void init(Map<String, String> options) {
		// TODO: Add options
		if (options.containsKey(HOST)) {
			host = options.get(HOST);
		}
		if (options.containsKey(PORT)) {
			port = Integer.parseInt(options.get(PORT));
		} else {
			port = -1;
		}
		context = ZMQ.context(1);
	}

	@Override
	public void send(byte[] message) throws IOException {
		String txt = new String(message);
		System.out.println(txt);
		publisher.send(message, 0);
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		return new ZeroMQTransportHandler(protocolHandler, options);
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Not implemented in this wrapper!");
	}


	@Override
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException("Not implemented in this wrapper!");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		// Out only atm
		// Prepare context, subscriber, publisher
//		subscriber = context.socket(ZMQ.SUB);
//		
//		if (host != null && port > 0) {
//			subscriber.bind("tcp://" + host + ":" + port);
//		}
		processOutOpen();
	}

	@Override
	public void processOutOpen() throws IOException {
		publisher = context.socket(ZMQ.PUB);
		if (host != null && port > 0) {
			publisher.bind("tcp://" + host + ":" + port);
		}
		new ZMQPublisher(this);
	}

	@Override
	public void processInClose() throws IOException {
		internalClose();
	}

	@Override
	public void processOutClose() throws IOException {
		internalClose();
	}

	private void internalClose() throws IOException {
		publisher.close();
		context.term();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

}
