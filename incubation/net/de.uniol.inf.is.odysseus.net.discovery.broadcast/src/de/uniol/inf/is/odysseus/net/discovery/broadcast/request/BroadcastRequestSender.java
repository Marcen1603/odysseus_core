package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.discovery.OdysseusNetDiscoveryException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class BroadcastRequestSender {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestSender.class);
	
	private static final int BROADCAST_REQUEST_PORT = 65212;

	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private final ServerBootstrap b = new ServerBootstrap();

	private ChannelFuture channelFuture;

	public BroadcastRequestSender() {
		b.group(bossGroup, workerGroup);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new BroadcastRequestAnswerHandler());
			}
		});
		b.option(ChannelOption.SO_BACKLOG, 128);
		b.childOption(ChannelOption.SO_KEEPALIVE, true);
	}

	public void start() throws OdysseusNetDiscoveryException {
		try {
			LOG.info("Binding netty server for broadcasting messages to port {}", BROADCAST_REQUEST_PORT);
			channelFuture = b.bind(BROADCAST_REQUEST_PORT).sync();
			LOG.info("Binding was successful");
		} catch (InterruptedException e) {
			throw new OdysseusNetDiscoveryException("Could not start server for sending broadcast messages", e);
		}
	}

	public void stop() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();

		channelFuture.channel().close();
		LOG.info("Netty server for broadcasting messages stopped");
	}

}
