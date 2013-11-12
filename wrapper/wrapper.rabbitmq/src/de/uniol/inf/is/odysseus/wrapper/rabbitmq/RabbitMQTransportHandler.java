package de.uniol.inf.is.odysseus.wrapper.rabbitmq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class RabbitMQTransportHandler extends AbstractTransportHandler {

	public static final String QUEUE_NAME = "QUEUE_NAME";
	public static final String CONSUMER_TAG = "CONSUMER_TAG";
	public static final String HOST = "HOST";
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	public static final String VIRTUALHOST = "VIRTUALHOST";
	public static final String PORT = "PORT";
	public static final String NAME = "RabbitMQ";

	private String queueName;
	private String consumerTag;
	private String host;
	private String username;
	private String password;
	private String virtualhost;
	private int port;

	Channel channel;
	Connection connection;

	public RabbitMQTransportHandler(IProtocolHandler<?> protocolHandler,
			Map<String, String> options) {
		super(protocolHandler);
		init(options);
	}

	private void init(Map<String, String> options) {
		// TODO: Check which options are optional and which one are required
		if (options.containsKey(QUEUE_NAME)) {
			queueName = options.get(QUEUE_NAME);
		}
		if (options.containsKey(HOST)) {
			host = options.get(HOST);
		}
		if (options.containsKey(USERNAME)) {
			username = options.get(USERNAME);
		}
		if (options.containsKey(PASSWORD)) {
			password = options.get(PASSWORD);
		}
		if (options.containsKey(VIRTUALHOST)) {
			virtualhost = options.get(VIRTUALHOST);
		}
		if (options.containsKey(CONSUMER_TAG)){
			consumerTag = options.get(CONSUMER_TAG);
		}
		if (options.containsKey(PORT)) {
			port = Integer.parseInt(options.get(PORT));
		} else {
			port = -1;
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		channel.basicPublish("", queueName, null, message);
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		return new RabbitMQTransportHandler(protocolHandler, options);
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
		internalOpen();
		// Create Consumer
		boolean autoAck = false;
		channel.basicConsume(queueName, autoAck, consumerTag,
				new DefaultConsumer(channel) {
					public void handleDelivery(
							String consumerTag,
							com.rabbitmq.client.Envelope envelope,
							com.rabbitmq.client.AMQP.BasicProperties properties,
							byte[] body) throws IOException {
//						String routingKey = envelope.getRoutingKey();
//						String contentType = properties.getContentType();
						long deliveryTag = envelope.getDeliveryTag();
						fireProcess(ByteBuffer.wrap(body));
						channel.basicAck(deliveryTag, false);
					};
				});
	}

	@Override
	public void processOutOpen() throws IOException {
		internalOpen();
	}

	private void internalOpen() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		if (username != null) {
			factory.setUsername(username);
		}
		if (password != null) {
			factory.setPassword(password);
		}
		if (virtualhost != null) {
			factory.setVirtualHost(virtualhost);
		}
		if (host != null) {
			factory.setHost(host);
		}
		if (port > 0) {
			factory.setPort(port);
		}
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(queueName, false, false, false, null);
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
		channel.close();
		connection.close();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

}
