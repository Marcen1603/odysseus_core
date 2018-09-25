package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.connection.ConnectionMessageReason;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.connection.IConnection;
import de.uniol.inf.is.odysseus.core.connection.IConnectionListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.websocket.NettyWebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class NonBlockingTcpServerHandler_Netty extends AbstractTransportHandler
		implements IAccessConnectionListener<ByteBuffer>, IConnectionListener, IHasAlias {

	static Logger logger = LoggerFactory.getLogger(TransportHandlerRegistry.class);

	public static final String PORT = "port";
	public static final String MAX_BUFFER_SIZE = "maxbuffersize";
	public static final String WEBSOCKET = "websocket";
	public static final String PATH_OPTION = "path";
	
	private int port;
	boolean isWebSocket = false;
	// TODO:
	boolean SSL;
	String path = "/websocket";

	private List<ChannelHandlerContext> channels = new CopyOnWriteArrayList<>();
	private MyTCPServer tcpServer = null;
	static private MyTCPServer tcpServerStatic = null;
	boolean shareTCPServer = true;
	final List<byte[]> buffer = new LinkedList<>();
	final int maxBufferSize;
	boolean shutdown = false;

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

	public NonBlockingTcpServerHandler_Netty(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		port = options.getInt(PORT, 8080);
		maxBufferSize = options.getInt(MAX_BUFFER_SIZE, 0);
		isWebSocket = options.getBoolean(WEBSOCKET, false);
		this.path = options.getString(PATH_OPTION, this.path);
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
		// wait until channel is writable to avoid out of memory
		// Problem: Blocks all other (potentially faster) clients
		// Solution could be: Keep elements for channel if not writable and send
		// later until a fixed size and delay than...
		while (!shutdown && !ctx.channel().isWritable() && ctx.channel().isOpen()) {
			synchronized (this) {
				try {
					this.wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (isWebSocket) {
			ChannelFuture result = ctx.writeAndFlush(new BinaryWebSocketFrame(send));
			if (!result.isSuccess()) {
				logger.debug("Sending data via websocket not successful");
			}
		} else {
			ctx.writeAndFlush(send);
		}
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		NonBlockingTcpServerHandler_Netty result = new NonBlockingTcpServerHandler_Netty(protocolHandler, options);
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
	public void notify(final IConnection connection, final ConnectionMessageReason reason) {
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
		shutdown = false;
		try {
			getTcpServer().add(port, this);
		} catch (Exception e) {
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
		shutdown = true;
		try {
			Iterator<ChannelHandlerContext> channelIter = this.channels.iterator();
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
	public void process(long callerId, ByteBuffer buffer) throws ClassNotFoundException {
		super.fireProcess(callerId, buffer);
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

	public void addChannel(ChannelHandlerContext ctx) {
		channels.add(ctx);
	}

	public void removeChannel(ChannelHandlerContext ctx) {
		channels.remove(ctx);
	}

}

class MyTCPServer {

	public MyTCPServer() {
	}

	final private Map<Integer, ServerBootstrap> bootsTraps = new HashMap<>();
	private Map<Integer, EventLoopGroup> bossGroups = new HashMap<>();
	private Map<Integer, EventLoopGroup> workerGroups = new HashMap<>();

	Map<Integer, ChannelFuture> portMapping = new HashMap<Integer, ChannelFuture>();
	Map<Integer, NonBlockingTcpServerHandler_Netty> handlerMapping = new HashMap<>();

	public void shutdown() {
		for (EventLoopGroup workerGroup : workerGroups.values()) {
			workerGroup.shutdownGracefully();
		}
		workerGroups.clear();
		for (EventLoopGroup bossGroup : bossGroups.values()) {
			bossGroup.shutdownGracefully();
		}
		bossGroups.clear();
		bootsTraps.clear();
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
			throws InterruptedException, CertificateException, SSLException {

		if (portMapping.containsKey(port)) {
			throw new IllegalArgumentException("Server port " + port + " already bound!");
		}

		ServerBootstrap b = new ServerBootstrap();
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		final SslContext sslCtx;
		if (caller.SSL) {
			SelfSignedCertificate ssc;
			ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		if (caller.isWebSocket) {
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new NettyWebSocketServerInitializer(sslCtx, caller.path, caller))
					.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		} else {
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							final ServerHandler handler = new ServerHandler(caller);
							ch.pipeline().addLast(handler);
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		}

		bootsTraps.put(port, b);
		bossGroups.put(port, bossGroup);
		workerGroups.put(port, workerGroup);

		ChannelFuture f = null;
		f = b.bind(port).sync();
		portMapping.put(port, f);
		handlerMapping.put(port, caller);
	}

}

/**
 * For each Connection there must be a handler class
 *
 * @author Marco Grawunder
 *
 */

class ServerHandler extends ByteToMessageDecoder {

	private NonBlockingTcpServerHandler_Netty handler;

	/**
	 * @param instance
	 * @param caller
	 */
	public ServerHandler(NonBlockingTcpServerHandler_Netty handler) {
		this.handler = handler;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		sendToServer(ctx, in.readBytes(in.readableBytes()), this);
	}

	public synchronized void sendToServer(ChannelHandlerContext ctx, ByteBuf msg, ServerHandler serverHandler)
			throws Exception {
		try {
			// System.err.println("ChannelRead " + msg);
			// System.err.println("Context " + ctx);
			// System.err.println("ServerHandler " + serverHandler);
			// System.err.println("Content "
			// + msg.toString(io.netty.util.CharsetUtil.UTF_8));

			ByteBuffer buffer = msg.nioBuffer();
			buffer.position(buffer.limit());
			handler.process(ctx.hashCode(), buffer);
			buffer.clear();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// Called from decode
			// ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// System.err.println("Channel active " + ctx.channel().localAddress());
		// Send cache elements to new connected receiver
		if (handler.maxBufferSize > 0) {
			synchronized (handler.buffer) {
				for (byte[] message : handler.buffer) {
					handler.send(message, ctx);
				}
			}
		}

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
