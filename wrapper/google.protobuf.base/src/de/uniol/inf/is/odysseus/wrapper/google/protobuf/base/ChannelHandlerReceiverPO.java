package de.uniol.inf.is.odysseus.wrapper.google.protobuf.base;

import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;

import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;

public class ChannelHandlerReceiverPO<R extends MessageLite, W> extends AbstractSource<W>
		implements ITransferHandler<W> {

	Logger logger = LoggerFactory.getLogger(ChannelHandlerReceiverPO.class);
	private SocketAddress address;
	private R message;
	private ITransformer<R, W> transformer;

	public ChannelHandlerReceiverPO(SocketAddress address,
			R message, ITransformer<R,W> inputTransformer) {
		this.address = address;
		this.message = message;
		this.transformer = inputTransformer;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);

		ChannelPipelineFactory cpf = new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline cp = Channels.pipeline();

				cp.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				cp.addLast(
						"protobufDecoder",
						new ProtobufDecoder(message.getDefaultInstanceForType()));
				cp.addLast("application", new ChannelReceiverDelegate<R>(ChannelHandlerReceiverPO.this));
				return cp;
			}
		};

		bootstrap.setPipelineFactory(cpf);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(address);
		logger.info("Bound to: " + address + " for message type: "
				+ message.getClass().getSimpleName());		
	}
	
	public void newMessage(R message){
		transfer(this.transformer.transform(message));
	}

	@Override
	public AbstractSource<W> clone() {
		throw new RuntimeException("Clone currently not supported Exception");
	}
	
}

