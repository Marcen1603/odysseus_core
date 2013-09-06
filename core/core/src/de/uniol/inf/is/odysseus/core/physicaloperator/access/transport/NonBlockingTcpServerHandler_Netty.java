package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class NonBlockingTcpServerHandler_Netty extends AbstractTransportHandler
		implements IAccessConnectionListener<ByteBuffer>, IConnectionListener {

	private int port;
	List<ChannelHandlerContext> channels = new CopyOnWriteArrayList<>();

	public NonBlockingTcpServerHandler_Netty(
			IProtocolHandler<?> protocolHandler, int port) {
		super(protocolHandler);
		this.port = port;
	}

	public NonBlockingTcpServerHandler_Netty() {
	}

	@Override
	public void send(byte[] message) throws IOException {
		for (ChannelHandlerContext ctx : channels) {
			final ByteBuf send = ctx.alloc().buffer(message.length);
			send.writeBytes(message);
			ctx.writeAndFlush(send);
		}

	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		int port = options.containsKey("port") ? Integer.parseInt(options
				.get("port")) : 8080;

		return new NonBlockingTcpServerHandler_Netty(protocolHandler, port);
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public String getName() {
		return "TCPServer2";
	}

	@Override
	public void notify(final IConnection connection,
			final ConnectionMessageReason reason) {
		switch (reason) {
		case ConnectionAbort:
			super.fireOnDisconnect();
			break;
		case ConnectionClosed:
			super.fireOnDisconnect();
			break;
		case ConnectionRefused:
			super.fireOnDisconnect();
			break;
		case ConnectionOpened:
			super.fireOnConnect();
			break;
		default:
			break;
		}
	}

	@Override
	public void processInOpen() throws IOException {
		processInOutOpen();
	}

	@Override
	public void processOutOpen() throws IOException {
		processInOutOpen();
	}

	private void processInOutOpen() throws IOException {
		try {
			MyTCPServer.getInstance().add(port, this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processInClose() throws IOException {
		processInOutClose();
	}

	@Override
	public void processOutClose() throws IOException {
		processInOutClose();
	}

	private void processInOutClose() throws IOException {
		try {
			MyTCPServer.getInstance().shutdown(port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> getOptions() {
		return null;
	}

	@Override
	public void process(ByteBuffer buffer) throws ClassNotFoundException {
		super.fireProcess(buffer);
	}

	@Override
	public void done() {

	}

	@Override
	public void socketDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void socketException(Exception ex) {
		// TODO Auto-generated method stub

	}

}

class MyTCPServer {

	private static MyTCPServer instance = new MyTCPServer();

	static synchronized MyTCPServer getInstance() {
		return instance;
	}

	private MyTCPServer() {
	}

	private ServerBootstrap b = null;
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	Map<Integer, ChannelFuture> portMapping = new HashMap<Integer, ChannelFuture>();

	public void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		b = null;
	}

	public void shutdown(int port) throws InterruptedException{
		ChannelFuture f = portMapping.get(port);
		f.channel().close();
		//f.channel().closeFuture().sync();
		portMapping.remove(port);
		if (portMapping.size() == 0){
			shutdown();
		}
	}

	public void add(int port, final NonBlockingTcpServerHandler_Netty caller)
			throws InterruptedException {

		if (portMapping.containsKey(port)) {
			throw new IllegalArgumentException("Server port " + port
					+ " already bound!");
		}

		if (b == null){
			b = new ServerBootstrap();
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup();
		}
		
		b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast(new MyServerHandler(caller));
					}
				}).option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture f = null;
		f = b.bind(port).sync();
		portMapping.put(port, f);

	}
	
	
}

class MyServerHandler extends ChannelInboundHandlerAdapter {

	final NonBlockingTcpServerHandler_Netty caller;
	ChannelHandlerContext ctx;

	MyServerHandler(NonBlockingTcpServerHandler_Netty caller) {
		this.caller = caller;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//System.err.println("Channel active " + ctx.name());
		caller.channels.add(ctx);
		this.ctx = ctx;
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		caller.channels.remove(ctx);
		this.ctx = null;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		caller.channels.remove(ctx);
		this.ctx = null;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		try {
			caller.process(((UnpooledUnsafeDirectByteBuf) msg).nioBuffer());
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

}
