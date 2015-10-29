package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.discovery.OdysseusNetDiscoveryException;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class BroadcastRequestSender {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestSender.class);
	private static final long DEFAULT_SEND_INTERVAL_MILLIS = 5000;

	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private final Bootstrap b = new Bootstrap();

	private ChannelFuture channelFuture;
	private BroadcastRequestSendThread sendThread;

	public BroadcastRequestSender(OdysseusNetStartupData data, IOdysseusNodeManager nodeManager) {
		Preconditions.checkNotNull(nodeManager, "nodeManager must not be null!");
		Preconditions.checkNotNull(data, "data must not be null!");
	
		b.group(workerGroup);
		b.channel(NioDatagramChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new BroadcastRequestHandler(data, nodeManager));
			}
		});
		b.option(ChannelOption.SO_BROADCAST, true);
	}

	public void start() throws OdysseusNetDiscoveryException {
		try {
			LOG.info("Binding request sender for broadcasting messages to port {}", BroadcastDiscoveryPlugIn.BROADCAST_PORT);
			channelFuture = b.bind(BroadcastDiscoveryPlugIn.BROADCAST_PORT).sync();
			LOG.info("Binding was successful");

			sendThread = new BroadcastRequestSendThread(determineSendInterval(), (DatagramChannel) channelFuture.channel());
			sendThread.start();

		} catch (InterruptedException e) {
			throw new OdysseusNetDiscoveryException("Could not start sending request messages", e);
		}
	}

	private static long determineSendInterval() {
		String updateIntervalStr = OdysseusNetConfiguration.get(BroadcastDiscoveryPlugIn.DISCOVERER_INTERVAL_CONFIG_KEY, "" + DEFAULT_SEND_INTERVAL_MILLIS);
		long updateInterval = tryCastToLong(updateIntervalStr, DEFAULT_SEND_INTERVAL_MILLIS);
		if( updateInterval < 1 ) {
			LOG.error("Update interval must be positive, not {}", updateInterval);
			return DEFAULT_SEND_INTERVAL_MILLIS;
		}
		
		return updateInterval;
	}

	private static long tryCastToLong(String text, long defaultValue) {
		try {
			return Long.parseLong(text);
		} catch( Throwable t ) {
			LOG.error("Invalid value for time interval: {}", text);
			return defaultValue;
		}
	}

	public void stop() {
		if (sendThread.isAlive()) {
			sendThread.stopRunning();
		}

		workerGroup.shutdownGracefully();

		channelFuture.channel().close();
		LOG.info("Request sender stopped");
	}

}
