package de.uniol.inf.is.odysseus.wrapper.zeromq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.ZMQConsumer;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.ZMQPublisher;

/**
 * 
 * This wrapper uses a Java implementation of ZeroMQ (see packages org.zeromq and zmq). ZeroMQ is licensed under LGPL (see: license/lgpl-3.0.txt) and can only be used or distributed as specified in the LGPL license.
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class ZeroMQTransportHandler extends AbstractTransportHandler {

	public static final String HOST = "host";
	public static final String WRITEPORT = "writeport";
	public static final String READPORT = "readport";
	public static final String NAME = "ZeroMQ";

	private String host;
	private int writePort;
	private int readPort;

	private ZMQPublisher publisher;
	private ZMQConsumer consumer;
	
	private Context context;

	public ZeroMQTransportHandler(){
	}
	
	public ZeroMQTransportHandler(IProtocolHandler<?> protocolHandler,
			Map<String, String> options) {
		super(protocolHandler);
		init(options);
	}

	private void init(Map<String, String> options) {
		if (options.containsKey(HOST)) {
			host = options.get(HOST);
		}
		if (options.containsKey(READPORT)) {
			readPort = Integer.parseInt(options.get(READPORT));
		} else {
			readPort = -1;
		}
		if (options.containsKey(WRITEPORT)) {
			writePort = Integer.parseInt(options.get(WRITEPORT));
		} else {
			writePort = -1;
		}
		context = ZMQ.context(1);
	}

	@Override
	public void send(byte[] message) throws IOException {
		String txt = new String(message);
		String newTxt = "";
		String[] splittedTxt = txt.split(":");
		for(int i = 1; i < splittedTxt.length; i++){
			String txtPart = "";
			if(i < 9){
				txtPart = splittedTxt[i].substring(0, (splittedTxt[i].length()-1));
			} else {
				txtPart = splittedTxt[i].substring(0, (splittedTxt[i].length()-2));
			}
			newTxt += " " + (i-1) + ":" + txtPart;
		}
		try {
			publisher.send(newTxt.getBytes());
			System.out.println("Following data successfully sent: " + newTxt);
		} catch(Exception ex) {
			System.err.println("An exception occured sending following data: " + newTxt);
			ex.printStackTrace();
		}
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
		if(consumer == null){
			consumer = new ZMQConsumer(this);
			consumer.start();
		}
	}

	@Override
	public void processOutOpen() throws IOException {
		if(publisher == null){
			publisher = new ZMQPublisher(this);
			publisher.start();
		}
	}

	@Override
	public void processInClose() throws IOException {
		consumer.close();
		consumer = null;
	}

	@Override
	public void processOutClose() throws IOException {
		publisher.close();
		publisher = null;
	}

	@SuppressWarnings("unused")
	private void internalClose() throws IOException {
		processInClose();
		processOutClose();
		context.term();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getWritePort() {
		return writePort;
	}

	public void setWritePort(int writePort) {
		this.writePort = writePort;
	}

	public int getReadPort() {
		return readPort;
	}

	public void setReadPort(int readPort) {
		this.readPort = readPort;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
