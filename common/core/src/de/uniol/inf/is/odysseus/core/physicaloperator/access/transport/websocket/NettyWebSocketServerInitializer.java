package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;

public class NettyWebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

	final private SslContext ssl;
	final private String path;

	public NettyWebSocketServerInitializer(SslContext ssl, String path){
		this.ssl = ssl;
		this.path = path;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (ssl != null){
			p.addLast(ssl.newHandler(ch.alloc()));
		}

		p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(65536));
		p.addLast(new WebSocketServerCompressionHandler());
		p.addLast(new WebSocketServerProtocolHandler(path, null, true));
		p.addLast(new WebSocketIndexPageHandler(path));
		p.addLast(new WebSocketFrameHandler());

	}

}
