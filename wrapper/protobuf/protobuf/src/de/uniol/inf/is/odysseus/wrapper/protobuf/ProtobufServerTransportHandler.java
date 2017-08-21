/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.protobuf;

/**
 * This is a transport handler that opens a tcp server on a distinct port and receives messages coded with the google protobuf format
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Descriptors.FieldDescriptor;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandlerDelegate;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandlerListener;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class ProtobufServerTransportHandler<R extends MessageLite,T> extends
		SimpleChannelHandler implements ITransportHandler{

	final AbstractTransportHandlerDelegate<Tuple<IMetaAttribute>> delegate;

	private static final String NAME = "ProtobufServer";
	public static Logger logger = LoggerFactory
			.getLogger(ProtobufServerTransportHandler.class);
	private ServerBootstrap bootstrap;
	private Channel openChannel;
	private long printMessageEach = 10000;
	private long counter = 0;
	private List<ChannelHandlerContext> channelHandlerContextList = new CopyOnWriteArrayList<ChannelHandlerContext>();
	final private SocketAddress address;
	final private R messagePrototype;

	private IExecutor executor;

	public ProtobufServerTransportHandler() {
		delegate = new AbstractTransportHandlerDelegate<>(null, null, this,null);
		address = null;
		messagePrototype = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ProtobufServerTransportHandler(IProtocolHandler protocolHandler, OptionMap options) {
		int port = Integer.parseInt(options.get("port"));

		this.address = new InetSocketAddress("0.0.0.0",port);
		this.messagePrototype = (R) ProtobufTypeRegistry.getMessageType(options.get("type"));
		if (messagePrototype == null){
			throw new RuntimeException( new IllegalArgumentException("No valid type given: " +options.get("type")));
		}
		delegate = new AbstractTransportHandlerDelegate<>(protocolHandler.getExchangePattern(), protocolHandler.getDirection(), this, options);

		protocolHandler.setTransportHandler(this);
		delegate.addListener(protocolHandler);

	}

	@Override
	public IExecutor getExecutor() {
		return executor;
	}

	@Override
	public void setExecutor(IExecutor executor) {
		this.executor = executor;
	}

	@Override
	public void updateOption(String key, String value) {
		throw new UnsupportedOperationException("Sorry. Update of options not supported by "+this.getName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#channelBound(org.jboss.netty
	 * .channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public synchronized void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Channel bound: "
				+ ((InetSocketAddress) e.getValue()).toString());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public synchronized void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Client connected: " + ctx.getChannel().getRemoteAddress()
				+ " :> " + ctx.getChannel().getLocalAddress());
		this.channelHandlerContextList.add(ctx);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#channelDisconnected(org.
	 * jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		logger.info("Client disconnected: "
				+ ctx.getChannel().getRemoteAddress() + " :> "
				+ ctx.getChannel().getLocalAddress());
		this.channelHandlerContextList.remove(ctx);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Object m = e.getMessage();
		if (logger.isDebugEnabled()) {
			if (++counter % printMessageEach == 0) {
				logger.debug(new Date() + ":Received message No." + counter);
			}
		}

		GeneratedMessage input = (GeneratedMessage) m;

		Map<FieldDescriptor, Object> test = input.getAllFields();
		Tuple<IMetaAttribute> ret = new Tuple<>(delegate.getSchema().size(), false);

		for (Entry<FieldDescriptor, Object> ent : test.entrySet()) {
			if (ent.getValue() != null && ent.getKey() != null) {
				// Numbers starting with 1
				ret.setAttribute(ent.getKey().getNumber()-1, ent.getValue());
			}
		}


		delegate.fireProcess(ret);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.jboss.netty.channel.SimpleChannelHandler#exceptionCaught(org.jboss
	 * .netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("Exception caught: " + e);
		ctx.getChannel().close();
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		delegate.open();
	}

	@Override
	public void processInOpen() throws IOException {
		// Do not connect to source in open, as element are send directly, when
		// connection is established
	}

	@Override
	public void start() {
		open(address, messagePrototype);
	}

	@Override
	public void setSchema(SDFSchema schema) {
		delegate.setSchema(schema);
	}

	@Override
	public SDFSchema getSchema() {
		return delegate.getSchema();
	}

	@Override
	public void processOutOpen() throws IOException {

	}

	public void open(SocketAddress address, final R message) {
		if (bootstrap == null) {
			ChannelFactory factory = new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());

			bootstrap = new ServerBootstrap(factory);

			ChannelPipelineFactory cpf = new ChannelPipelineFactory() {
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					ChannelPipeline cp = Channels.pipeline();

					cp.addLast("frameDecoder",
							new ProtobufVarint32FrameDecoder());
					MessageLite m = message.getDefaultInstanceForType();
					ProtobufDecoder dec = new ProtobufDecoder(m);
					cp.addLast("protobufDecoder", dec);
					cp.addLast("application", ProtobufServerTransportHandler.this);
					return cp;
				}
			};

			bootstrap.setPipelineFactory(cpf);
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			openChannel = bootstrap.bind(address);
			logger.info("Bound to: " + address + " for message type: "
					+ message.getClass().getSimpleName());
		}
	}

	@Override
	public void processInStart() {
	}


	@Override
	public void processInClose() throws IOException {
		for (ChannelHandlerContext ctx:channelHandlerContextList){
			ctx.getChannel().close();
		}
		openChannel.disconnect();
		openChannel.close().awaitUninterruptibly();
	}

	@Override
	public void processOutClose() throws IOException {
		throw new RuntimeException("Not implemented");

	}

	@Override
	public synchronized void close() throws IOException {
		delegate.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addListener(ITransportHandlerListener listener) {
		delegate.addListener(listener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void removeListener(ITransportHandlerListener listener) {
		delegate.removeListener(listener);
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void send(Object message) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void send(String message, boolean addNewline) throws IOException {
		throw new RuntimeException("Not implemented");
	}
	
	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new ProtobufServerTransportHandler<>( protocolHandler, options);
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		return delegate.getExchangePattern();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSemanticallyEqual(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation) {
		// Nothing to do
	}

}
