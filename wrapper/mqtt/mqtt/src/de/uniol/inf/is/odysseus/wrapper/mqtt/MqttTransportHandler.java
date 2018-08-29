package de.uniol.inf.is.odysseus.wrapper.mqtt;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class MqttTransportHandler extends AbstractPushTransportHandler implements MqttCallback {

	private static final Logger LOG = LoggerFactory.getLogger(MqttTransportHandler.class);

	private static final String NAME = "MQTT";

	private static final String TOPIC = "topic";
	private static final String QOS = "qos";
	private static final String BROKER = "broker";
	private static final String CLIENT_ID = "client_id";

	private String topic;
	private int qos;
	private String broker;
	private String clientId;

	// TODO: Make configurable?
	MemoryPersistence persistence = new MemoryPersistence();

	MqttClient client;

	public MqttTransportHandler() {
	}

	public MqttTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		options.checkRequiredException(TOPIC, BROKER, CLIENT_ID);

		topic = options.get(TOPIC);
		broker = options.get(BROKER);
		qos = options.getInt(QOS, 2);
		clientId = options.get(CLIENT_ID);

	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new MqttTransportHandler(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		connectToBroker();
        client.setCallback(this);
        try {
			client.subscribe(topic);
		} catch (MqttException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void processOutOpen() throws IOException {
		connectToBroker();
	}

	private void connectToBroker() throws IOException {
		try {
			client = new MqttClient(broker, clientId, persistence);
		} catch (MqttException e) {
			throw new IOException(e);
		}
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		LOG.trace("Connecting to broker: " + broker);
		try {
			client.connect(connOpts);
		} catch (MqttException e) {
			throw new IOException(e);
		}
		LOG.trace("Connected");
	}

	@Override
	public void processInClose() throws IOException {
		disconnectFromBroker();
	}

	@Override
	public void processOutClose() throws IOException {
		disconnectFromBroker();
	}

	private void disconnectFromBroker() throws IOException {
		try {
			client.disconnect();
		} catch (MqttException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		 MqttMessage msg = new MqttMessage(message);
		 msg.setQos(qos);
		 try {
			client.publish(topic, msg);
		} catch (MqttException e) {
			throw new IOException(e);
		}
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		LOG.error("Connection lost to broker",cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		byte[] load = message.getPayload();
		ByteBuffer buf = ByteBuffer.allocate(load.length);
		buf.put(load);
		fireProcess(buf);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}



}
