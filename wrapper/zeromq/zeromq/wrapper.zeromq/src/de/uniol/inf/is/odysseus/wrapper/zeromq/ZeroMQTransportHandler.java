package de.uniol.inf.is.odysseus.wrapper.zeromq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.AZMQConnector;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.ZMQContextProvider;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.ZMQPullConsumer;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.ZMQPullPublisher;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.ZMQPushConsumer;
import de.uniol.inf.is.odysseus.wrapper.zeromq.communication.ZMQPushPublisher;

/**
 * 
 * This wrapper uses a Java implementation of ZeroMQ (see packages org.zeromq and zmq). ZeroMQ is licensed under LGPL (see: license/lgpl-3.0.txt) and can only be used or distributed as specified in the LGPL license.
 * 
 * @author Jan Benno Meyer zu Holte
 *
 */
public class ZeroMQTransportHandler extends AbstractTransportHandler {

	private final Logger LOG = LoggerFactory.getLogger(ZeroMQTransportHandler.class);
	
	public static final String HOST = "host";
	public static final String WRITEPORT = "writeport";
	public static final String READPORT = "readport";
	public static final String DELAYOFMSG = "delayofmsg";
	public static final String THREADS = "threads";
	public static final String NAME = "ZeroMQ";
	public static final String PARAMS = "params";
	public static final String TIMEOUT = "timeout";
	public static final String SUBSCRIPTIONFILTER = "subscriptionfilter";
	public static final String RECEIVE_BUFFER_SIZE = "receivebuffersize";

	private String host;
	private int writePort;
	private int readPort;
	private int delayOfMsg;
	private int communicationThreads;
	private int timeout;
	private String subscriptionFilter;
	private String[] params;
	private long receiveBufferSize;
	
	private int delayedMsgCounter = 1;
	private String delayedMsgs = "";

	private AZMQConnector publisher;
	private AZMQConnector consumer;
	
	/** In and output for data transfer */
	private InputStream input;
	private OutputStream output;
	
	private ZMQContextProvider context;

	public ZeroMQTransportHandler(){
	}
	
	public ZeroMQTransportHandler(IProtocolHandler<?> protocolHandler,
			OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		
		if (options.containsKey(HOST)) {
			host = options.get(HOST);
		}
		receiveBufferSize = options.getLong(RECEIVE_BUFFER_SIZE, -1);
		readPort = options.getInt(READPORT,-1);
		writePort = options.getInt(WRITEPORT, -1);
		delayOfMsg = options.getInt(DELAYOFMSG, -1);
		
		communicationThreads = options.getInt(THREADS, 1);
	
		if (options.containsKey(PARAMS)) {
			String paramString = options.get(PARAMS);
			params = paramString.split(",");
		} else {
			params = new String[1];
			params[0] = "";
		}
		timeout = options.getInt(TIMEOUT, 10);
		subscriptionFilter = options.get(SUBSCRIPTIONFILTER, "");
		
		createContext();
	}
	
	public void createContext(){
		if(context == null){
			context = new ZMQContextProvider(communicationThreads);
			context.start();
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		String txt = new String(message, "UTF-8");
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
		delayedMsgs += newTxt;
		if(delayedMsgCounter == delayOfMsg){
			try {
				publisher.send(delayedMsgs.getBytes());
				// System.out.println("Following data successfully sent: \n\t" + delayedMsgs + "\n");
				delayedMsgs = "";
				delayedMsgCounter = 1;
			} catch(Exception ex) {
				LOG.error("An exception occured sending following data: \n\t" + delayedMsgs);
				ex.printStackTrace();
			}
		} else {
			delayedMsgCounter++;
		}
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new ZeroMQTransportHandler(protocolHandler, options);
	}

	@Override
	public InputStream getInputStream() {
		createContext();
		try {
			processInClose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(input == null){
			input = new ByteArrayInputStream(new byte[0]);
		}
		consumer = new ZMQPullConsumer(this);
		consumer.start();
		return input;
	}


	@Override
	public OutputStream getOutputStream() {
		createContext();
		try {
			processOutClose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(output == null){
			output = new ByteArrayOutputStream();
		}
		if(publisher != null){
			publisher.close();
		}
		publisher = new ZMQPullPublisher(this);
		publisher.start();
		return output;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		createContext();
		if(input == null){
			input = new ByteArrayInputStream(new byte[0]);
		}
		if(consumer == null){
			consumer = new ZMQPushConsumer(this, receiveBufferSize);	
			consumer.start();
		}
	}

	@Override
	public void processOutOpen() throws IOException {
		createContext();
		if(output == null){
			output = new ByteArrayOutputStream();
		}
		if(publisher == null){
			publisher = new ZMQPushPublisher(this);
			publisher.start();
		}
	}

	@Override
	public void processInClose() throws IOException {
		if(input == null){
			input.close();
		}
		input = null;
		consumer.close();
		consumer = null;
	}

	@Override
	public void processOutClose() throws IOException {
		if(output == null){
			output.close();
		}
		output = null;
		publisher.close();
		publisher = null;
	}

	@SuppressWarnings("unused")
	private void internalClose() throws IOException {
		processInClose();
		processOutClose();
		context.close();
		context = null;
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

	public ZMQContextProvider getContext() {
		return context;
	}

	public void setContext(ZMQContextProvider context) {
		this.context = context;
	}

	public int getDelayOfMsg() {
		return delayOfMsg;
	}

	public void setDelayOfMsg(int delayTuple) {
		this.delayOfMsg = delayTuple;
	}

	public int getThreads() {
		return communicationThreads;
	}

	public void setThreads(int threads) {
		this.communicationThreads = threads;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int frequency) {
		this.timeout = frequency;
	}

	public String getSubscriptionFilter() {
		return subscriptionFilter;
	}

	public void setSubscriptionFilter(String subscription) {
		this.subscriptionFilter = subscription;
	}

	public InputStream getInput() {
		return input;
	}

	public void setInput(InputStream input) {
		this.input = input;
	}

	public OutputStream getOutput() {
		return output;
	}

	public void setOutput(OutputStream output) {
		this.output = output;
	}
}
