package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NonBlockingTcpClientHandler_Netty extends AbstractPushTransportHandler{

	class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

		final NonBlockingTcpClientHandler_Netty handler;

		public ClientHandler(NonBlockingTcpClientHandler_Netty handler) {
			this.handler = handler;
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			if (initializeCommand != null) {
				// TODO: is copiedBuffer the right methods (btw. it is only called once!)
				ctx.writeAndFlush(Unpooled.copiedBuffer(initializeCommand));
			}
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
			ByteBuffer buffer = msg.nioBuffer();
			buffer.position(buffer.limit());
			handler.fireProcess(ctx.hashCode(), buffer);
			buffer.clear();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			// TODO: Better error handling
			cause.printStackTrace();
		}

	}

	public static final String NAME = "TCPClient1";

	// The host name
	public static final String HOST = "host";
	// The port
	public static final String PORT = "port";
	// The read buffer size
	public static final String READ_BUFFER = "read";
	// The write buffer size
	public static final String WRITE_BUFFER = "write";
	// Auto reconnect on disconnect
	public static final String AUTOCONNECT = "autoconnect";
	// The initialization command
	public static final String INITIALIZE = "init";

	static final InfoService infoService = InfoServiceFactory.getInfoService(NonBlockingTcpClientHandler_Netty.class);

	private String host;
	private int port;

	private byte[] initializeCommand;

	private ChannelFuture f;

	private NioEventLoopGroup group;

	public NonBlockingTcpClientHandler_Netty() {
		super();
	}

	public NonBlockingTcpClientHandler_Netty(IProtocolHandler<?> protocolHandler, OptionMap optionsMap) {
		super(protocolHandler, optionsMap);
		this.init();
	}

	/**
	 * @param initializeCommand
	 *            the initializeCommand to set
	 */
	private void setInitializeCommand(final String initializeCommand) {
		if (initializeCommand != null) {
			this.initializeCommand = initializeCommand.replace("\\\\n", "\n").replace("\\n", "\n").getBytes();
		} else {
			this.initializeCommand = null;
		}
	}

	private void init() {
		final OptionMap options = this.getOptionsMap();
		host = options.getString(HOST, "localhost");
		port = options.getInt(PORT, 8080);

		if (options.containsKey(INITIALIZE)) {
			setInitializeCommand(options.getString(INITIALIZE));
		}

	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new NonBlockingTcpClientHandler_Netty(protocolHandler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new ClientHandler(NonBlockingTcpClientHandler_Netty.this));
					};
				});
		try {
			f = b.connect().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new RuntimeException("Sorry. Currenlty not implemented for " + NAME);

	}

	@Override
	public void processInClose() throws IOException {
		try {
			f.channel().closeFuture().sync();
			group.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processOutClose() throws IOException {
		throw new RuntimeException("Sorry. Currenlty not implemented for " + NAME);

	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new RuntimeException("Sorry. Currenlty not implemented for " + NAME);

	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}



}
