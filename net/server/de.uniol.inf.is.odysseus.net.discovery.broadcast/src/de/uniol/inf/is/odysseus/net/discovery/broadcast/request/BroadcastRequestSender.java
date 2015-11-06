package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
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
	private int usedPort = BroadcastDiscoveryPlugIn.BROADCAST_PORT_MIN;
	

	public BroadcastRequestSender(IOdysseusNode localNode, IOdysseusNodeManager nodeManager) {
		Preconditions.checkNotNull(nodeManager, "nodeManager must not be null!");
		Preconditions.checkNotNull(localNode, "localNode must not be null!");

		b.group(workerGroup);
		b.channel(NioDatagramChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new BroadcastHandler(localNode, usedPort));
			}
		});
		b.option(ChannelOption.SO_BROADCAST, true);
	}

	public void start() throws OdysseusNetDiscoveryException {
		for( usedPort = BroadcastDiscoveryPlugIn.BROADCAST_PORT_MIN; usedPort <= BroadcastDiscoveryPlugIn.BROADCAST_PORT_MAX; usedPort++) {
			try {
				LOG.debug("Trying binding port {}", usedPort);
				channelFuture = b.bind(usedPort).sync();
				LOG.info("Binding was successful with port {}", usedPort);
				
				break; // found port
			} catch (Throwable t) {
				LOG.debug("Binding port {} failed", usedPort, t);
				if( usedPort == BroadcastDiscoveryPlugIn.BROADCAST_PORT_MAX) {
					throw new OdysseusNetDiscoveryException("Could not find a free port between " + BroadcastDiscoveryPlugIn.BROADCAST_PORT_MIN + " and " + BroadcastDiscoveryPlugIn.BROADCAST_PORT_MAX);
				}
			}
		}
		sendThread = new BroadcastRequestSendThread(determineSendInterval(), (DatagramChannel) channelFuture.channel());
		sendThread.start();
	}

	private static long determineSendInterval() {
		String updateIntervalStr = OdysseusNetConfiguration.get(BroadcastDiscoveryPlugIn.DISCOVERER_INTERVAL_CONFIG_KEY, "" + DEFAULT_SEND_INTERVAL_MILLIS);
		long updateInterval = tryCastToLong(updateIntervalStr, DEFAULT_SEND_INTERVAL_MILLIS);
		if (updateInterval < 1) {
			LOG.error("Update interval must be positive, not {}", updateInterval);
			return DEFAULT_SEND_INTERVAL_MILLIS;
		}

		return updateInterval;
	}

	private static long tryCastToLong(String text, long defaultValue) {
		try {
			return Long.parseLong(text);
		} catch (Throwable t) {
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
