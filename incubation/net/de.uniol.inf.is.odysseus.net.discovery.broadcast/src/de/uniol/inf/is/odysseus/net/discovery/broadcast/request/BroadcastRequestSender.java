package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
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
	private static final long REQUEST_SEND_INTERVAL_MILLIS = 3000;

	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private final Bootstrap b = new Bootstrap();

	private ChannelFuture channelFuture;
	private BroadcastRequestSendThread sendThread;

	public BroadcastRequestSender(IOdysseusNodeManager nodeManager) {
		Preconditions.checkNotNull(nodeManager, "nodeManager must not be null!");
		
		b.group(workerGroup);
		b.channel(NioDatagramChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new BroadcastRequestAnswerHandler(nodeManager));
			}
		});
		b.option(ChannelOption.SO_BROADCAST, true);
	}

	public void start() throws OdysseusNetDiscoveryException {
		try {
			LOG.info("Binding request sender for broadcasting messages to port {}", BroadcastDiscoveryPlugIn.BROADCAST_ANSWER_PORT);
			channelFuture = b.bind(BroadcastDiscoveryPlugIn.BROADCAST_ANSWER_PORT).sync();
			LOG.info("Binding was successful");

			sendThread = new BroadcastRequestSendThread(REQUEST_SEND_INTERVAL_MILLIS, (DatagramChannel) channelFuture.channel());
			sendThread.start();

		} catch (InterruptedException e) {
			throw new OdysseusNetDiscoveryException("Could not start sending request messages", e);
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
