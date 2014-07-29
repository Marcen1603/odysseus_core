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
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;

public class NonBlockingTcpServerHandler_Netty extends AbstractTransportHandler
		implements IAccessConnectionListener<ByteBuffer>, IConnectionListener,
		IHasAlias {

	private static final String PORT = "port";
	private static final Object MAX_BUFFER_SIZE = "maxbuffersize";
	private int port;
	List<ChannelHandlerContext> channels = new CopyOnWriteArrayList<>();
	private MyTCPServer tcpServer = null;
	static private MyTCPServer tcpServerStatic = null;
	boolean shareTCPServer = true;
	final List<byte[]> buffer = new LinkedList<>();
	final int maxBufferSize;

	/**
	 * @return the tcpServer
	 */
	public MyTCPServer getTcpServer() {
		MyTCPServer ret = null;
		if (shareTCPServer) {
			synchronized (MyTCPServer.class) {
				if (tcpServerStatic == null) {
					tcpServerStatic = new MyTCPServer();
				}
			}
			ret = tcpServerStatic;
		} else {
			synchronized (this) {
				if (tcpServer == null) {
					tcpServer = new MyTCPServer();
				}
			}
			ret = tcpServer;
		}
		return ret;
	}

	public NonBlockingTcpServerHandler_Netty(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		super(protocolHandler, options);
		port = options.containsKey(PORT) ? Integer.parseInt(options
				.get(PORT)) : 8080;
		maxBufferSize = options.containsKey(MAX_BUFFER_SIZE) ? Integer
				.parseInt(options.get(MAX_BUFFER_SIZE)) : 0;
	}

	public NonBlockingTcpServerHandler_Netty() {
		this.maxBufferSize = 0;
	}

	@Override
	public void send(byte[] message) throws IOException {
		// Buffer messages for later connecting clients
		if (maxBufferSize > 0) {
			synchronized (buffer) {
				if (buffer.size() == maxBufferSize) {
					buffer.remove(0);
				}
				buffer.add(message);
			}
		}
		for (ChannelHandlerContext ctx : channels) {
			send(message, ctx);
		}

	}

	public void send(byte[] message, ChannelHandlerContext ctx) {
		// System.err.println(this + " Sending to "
		// + ctx.channel().remoteAddress());
		final ByteBuf send = ctx.alloc().buffer(message.length);
		send.writeBytes(message);
		ctx.writeAndFlush(send);
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		NonBlockingTcpServerHandler_Netty result = new NonBlockingTcpServerHandler_Netty(
				protocolHandler, options);
		return result;
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
		return "TCPServer";
	}

	@Override
	public String getAliasName() {
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
			getTcpServer().add(port, this);
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
			Iterator<ChannelHandlerContext> channelIter = this.channels
					.iterator();
			while (channelIter.hasNext()) {
				ChannelHandlerContext e = channelIter.next();
				e.close();
			}
			channels.clear();
			getTcpServer().shutdown(port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof NonBlockingTcpServerHandler_Netty)) {
			return false;
		}
		NonBlockingTcpServerHandler_Netty other = (NonBlockingTcpServerHandler_Netty) o;
		if (this.port != other.port) {
			return false;
		}
		if (this.maxBufferSize != other.maxBufferSize) {
			return false;
		}

		return true;
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

class MyTCPServer extends ChannelInboundHandlerAdapter {

	// private static MyTCPServer instance = new MyTCPServer();
	//
	// static synchronized MyTCPServer getInstance() {
	// return instance;
	// }

	public MyTCPServer() {
	}

	private ServerBootstrap b = null;
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	Map<Integer, ChannelFuture> portMapping = new HashMap<Integer, ChannelFuture>();
	Map<Integer, NonBlockingTcpServerHandler_Netty> handlerMapping = new HashMap<>();

	public void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		b = null;
	}

	public void shutdown(int port) throws InterruptedException {
		ChannelFuture f = portMapping.get(port);
		f.channel().close();
		// f.channel().closeFuture().sync();
		portMapping.remove(port);
		handlerMapping.remove(port);
		if (portMapping.size() == 0) {
			shutdown();
		}
	}

	public void add(int port, final NonBlockingTcpServerHandler_Netty caller)
			throws InterruptedException {

		if (portMapping.containsKey(port)) {
			throw new IllegalArgumentException("Server port " + port
					+ " already bound!");
		}

		if (b == null) {
			b = new ServerBootstrap();
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ServerHandler handler = new ServerHandler(
									MyTCPServer.this);
							ch.pipeline().addLast(handler);
						}
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
		}

		ChannelFuture f = null;
		f = b.bind(port).sync();
		portMapping.put(port, f);
		handlerMapping.put(port, caller);
	}

	private int getPort(ChannelHandlerContext ctx) {
		return ((InetSocketAddress) ctx.channel().localAddress()).getPort();
	}

	private NonBlockingTcpServerHandler_Netty getHandler(
			ChannelHandlerContext ctx) {
		return handlerMapping.get(getPort(ctx));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.
	 * channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// System.err.println("Channel active " + ctx.channel().localAddress());
		// Send cache elements to new connected receiver
		if (getHandler(ctx).maxBufferSize > 0) {
			synchronized (getHandler(ctx).buffer) {
				for (byte[] message : getHandler(ctx).buffer) {
					getHandler(ctx).send(message, ctx);
				}
			}
		}

		getHandler(ctx).channels.add(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty
	 * .channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// System.err.println("Channel inactive " +
		// ctx.channel().localAddress());
		getHandler(ctx).channels.remove(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel
	 * .ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		try {
			ByteBuffer buffer = ((UnpooledUnsafeDirectByteBuf) msg).nioBuffer();
			buffer.position(buffer.limit());
			getHandler(ctx).process(buffer);
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			ReferenceCountUtil.release(msg);
		}
	}

}

/**
 * For each Connection there must be a handler class
 * 
 * @author Marco Grawunder
 * 
 */
class ServerHandler extends ChannelInboundHandlerAdapter {

	private MyTCPServer instance;

	/**
	 * @param instance
	 */
	public ServerHandler(MyTCPServer instance) {
		this.instance = instance;
		// System.err.println("New Server Handler created for " + instance);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		instance.channelRead(ctx, msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		instance.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		instance.channelInactive(ctx);
	}

}
