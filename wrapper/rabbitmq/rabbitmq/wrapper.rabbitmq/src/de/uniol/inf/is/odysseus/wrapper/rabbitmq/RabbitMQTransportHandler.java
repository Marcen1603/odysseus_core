package de.uniol.inf.is.odysseus.wrapper.rabbitmq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.TopologyRecoveryException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class RabbitMQTransportHandler extends AbstractTransportHandler implements UncaughtExceptionHandler, ExceptionHandler {

	static final Logger LOG = LoggerFactory.getLogger(RabbitMQTransportHandler.class);

	public static final String QUEUE_NAME = "queue_name";
	public static final String EXCHANGE_NAME = "exchange_name";
	public static final String CONSUMER_TAG = "consumer_tag";
	public static final String PUBLISH_STYLE = "publish_style";
	public static final String HOST = "host";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String VIRTUALHOST = "virtualhost";
	public static final String PORT = "port";
	public static final String NAME = "RabbitMQ";
	public static final String DURABLE = "durable";
	public static final String EXCLUSIVE = "exclusive";
	public static final String AUTO_DELETE = "auto_delete";

	public static final String OPTIONS_PREFIX = "rabbit.";

	public enum PublishStyle {
		WorkQueue, PublishSubscribe
	};

	private String queueName;
	private String exchangeName;
	private String consumerTag;
	private String host;
	private String username;
	private String password;
	private String virtualhost;
	private int port;
	private PublishStyle publishStyle;

	private OptionMap options;

	Channel channel;
	Connection connection;

	public RabbitMQTransportHandler() {
	}

	public RabbitMQTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) {
		// TODO: Check which options are optional and which one are required
		this.options = options;

		queueName = options.get(QUEUE_NAME, "");
		exchangeName = options.get(EXCHANGE_NAME, "");

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
		if (options.containsKey(CONSUMER_TAG)) {
			consumerTag = options.get(CONSUMER_TAG);
		}
		if (options.containsKey(PORT)) {
			port = Integer.parseInt(options.get(PORT));
		} else {
			port = -1;
		}

		String publishStyleOption = options.get(PUBLISH_STYLE, "workqueue");
		if (publishStyleOption.equals("workqueue"))
			publishStyle = PublishStyle.WorkQueue;
		else if (publishStyleOption.equals("publishsubscribe"))
			publishStyle = PublishStyle.PublishSubscribe;
		else
			throw new IllegalArgumentException(
					"Option PUBLISH_STYLE contains invalid value \"" + publishStyleOption + "\"");
	}

	@Override
	public void send(byte[] message) throws IOException {
		channel.basicPublish(exchangeName, queueName, null, message);
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
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
		try {
			internalOpen();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void processInStart() {
		try {
			if (publishStyle == PublishStyle.PublishSubscribe) {
				String queueName = channel.queueDeclare().getQueue();
				channel.queueBind(queueName, exchangeName, "");
			}

			// Create Consumer
			boolean autoAck = false;
			channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope,
						com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
					// String routingKey = envelope.getRoutingKey();
					// String contentType = properties.getContentType();
					long deliveryTag = envelope.getDeliveryTag();
					try {
						ByteBuffer wrapped = ByteBuffer.wrap(body);
						wrapped.position(wrapped.limit());
						fireProcess(wrapped);
					} catch (Exception e) {
						LOG.warn("Error processing input", e);
					}
					channel.basicAck(deliveryTag, false);
				};
			});

			connection.addShutdownListener(new ShutdownListener() {

				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					LOG.trace("Connection shutdown.", cause);
				}
			});

			channel.addShutdownListener(new ShutdownListener() {

				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					LOG.trace("Channel shutdown.", cause);
				}
			});
		} catch (IOException e) {
			throw new StartFailedException(e);
		}

	}

	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		LOG.error("Error in Thread " + thread.getName(), exception);
	}

	@Override
	public void processOutOpen() throws IOException {
		try {
			internalOpen();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void internalOpen() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setExceptionHandler(this);
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
		channel.basicQos(10);

		switch (publishStyle) {
		case WorkQueue:
			// Add a time to live (cause the server adds this and thus we have
			// to expect it)
			Map<String, Object> args = new HashMap<>();

			// Use options
			boolean durable = options.getBoolean(DURABLE, false);
			boolean exclusive = options.getBoolean(EXCLUSIVE, false);
			boolean autoDelete = options.getBoolean(AUTO_DELETE, false);

			Set<String> keySet = options.getKeySet();
			Object[] keys = keySet.stream().filter(key -> key.startsWith(OPTIONS_PREFIX)).toArray();
			for (Object key : keys) {
				if (key instanceof String) {
					String keyString = (String) key;
					Object value = options.getValue(keyString);
					// Remove the prefix when we insert it so the real args
					String argKey = keyString.replaceFirst(OPTIONS_PREFIX, "");
					args.put(argKey, value);
				}
			}

			channel.queueDeclare(queueName, durable, exclusive, autoDelete, args);
			break;

		case PublishSubscribe:
			channel.exchangeDeclare(exchangeName, "fanout");
			break;
		}
	}

	@Override
	public void processInClose() throws IOException {
		try {
			internalClose();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void processOutClose() throws IOException {
		try {
			internalClose();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void internalClose() throws IOException, TimeoutException {
		channel.close();
		connection.close();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {
		exception.printStackTrace();
	}

	@Override
	public void handleReturnListenerException(Channel channel, Throwable exception) {
		exception.printStackTrace();
	}

	@Override
	public void handleConfirmListenerException(Channel channel, Throwable exception) {
		exception.printStackTrace();
	}

	@Override
	public void handleBlockedListenerException(Connection connection, Throwable exception) {
		exception.printStackTrace();
	}

	@Override
	public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag,
			String methodName) {
		exception.printStackTrace();
	}

	@Override
	public void handleConnectionRecoveryException(Connection conn, Throwable exception) {
		exception.printStackTrace();
	}

	@Override
	public void handleChannelRecoveryException(Channel ch, Throwable exception) {
		exception.printStackTrace();
	}

	@Override
	public void handleTopologyRecoveryException(Connection conn, Channel ch, TopologyRecoveryException exception) {
		exception.printStackTrace();
	}

}
