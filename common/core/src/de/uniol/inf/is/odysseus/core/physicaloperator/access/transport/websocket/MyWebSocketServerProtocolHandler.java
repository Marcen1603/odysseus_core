package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.websocket;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpServerHandler_Netty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class MyWebSocketServerProtocolHandler extends WebSocketServerProtocolHandler {

	private NonBlockingTcpServerHandler_Netty handler;

	public MyWebSocketServerProtocolHandler(String path, String object, boolean b, NonBlockingTcpServerHandler_Netty handler) {
		super(path, object, b);
		this.handler = handler;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		handler.addChannel(ctx);
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		handler.removeChannel(ctx);
		super.channelInactive(ctx);
	}

}
