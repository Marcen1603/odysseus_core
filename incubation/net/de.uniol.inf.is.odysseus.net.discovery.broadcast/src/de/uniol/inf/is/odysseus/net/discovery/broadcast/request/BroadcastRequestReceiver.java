package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.discovery.OdysseusNetDiscoveryException;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class BroadcastRequestReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestSender.class);
	
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private final Bootstrap b = new Bootstrap();

	private ChannelFuture channelFuture;

	public BroadcastRequestReceiver() {
		b.group(workerGroup);
		b.channel(NioDatagramChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new BroadcastRequestAnswerHandler());
			}
		});
		b.option(ChannelOption.SO_BROADCAST, true);
	}

	public void start() throws OdysseusNetDiscoveryException {
		try {
			LOG.info("Binding netty server for receiving broadcasting messages to port {}", BroadcastDiscoveryPlugIn.BROADCAST_ANSWER_PORT);
			channelFuture = b.bind(BroadcastDiscoveryPlugIn.BROADCAST_ANSWER_PORT).sync();
			LOG.info("Binding for receiving messages was successful");
		} catch (InterruptedException e) {
			throw new OdysseusNetDiscoveryException("Could not start server for receiving broadcast messages", e);
		}
	}

	public void stop() {
		workerGroup.shutdownGracefully();

		channelFuture.channel().close();
		LOG.info("Netty server for receiving messages stopped");
	}

}
