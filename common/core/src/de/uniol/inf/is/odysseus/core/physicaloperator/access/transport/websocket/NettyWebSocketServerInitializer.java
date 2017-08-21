package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.websocket;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpServerHandler_Netty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;

public class NettyWebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

	final private SslContext ssl;
	final private String path;
	private NonBlockingTcpServerHandler_Netty handler;

	public NettyWebSocketServerInitializer(SslContext ssl, String path, NonBlockingTcpServerHandler_Netty handler) {
		this.ssl = ssl;
		this.path = path;
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (ssl != null) {
			p.addLast(ssl.newHandler(ch.alloc()));
		}

		p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(65536));
		p.addLast(new WebSocketServerCompressionHandler());
		p.addLast(new MyWebSocketServerProtocolHandler(path, null, true, handler));
//		p.addLast(new WebSocketIndexPageHandler(path));
//		p.addLast(new WebSocketFrameHandler(handler));

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		handler.addChannel(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// System.err.println("Channel inactive " +
		// ctx.channel().localAddress());
		handler.removeChannel(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// System.err.println("ERROR");
		cause.printStackTrace();

		super.exceptionCaught(ctx, cause);
	}

}
