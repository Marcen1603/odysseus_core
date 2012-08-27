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

import java.util.List;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;
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

import com.google.protobuf.MessageLite;

public class ChannelReceiverDelegate<R extends MessageLite> extends
		SimpleChannelHandler {

	public static Logger logger = LoggerFactory
			.getLogger(ChannelReceiverDelegate.class);
	private ChannelHandlerReceiverPO<R, ?> channelHandlerReceiverPO;
	private ServerBootstrap bootstrap;
	private Channel openChannel;
	private long printMessageEach = 10000;
	private long counter = 0;
	private List<ChannelHandlerContext> channelHandlerContextList = new CopyOnWriteArrayList<ChannelHandlerContext>();
			
	public ChannelReceiverDelegate(
			ChannelHandlerReceiverPO<R, ?> channelHandlerReceiverPO) {
		this.channelHandlerReceiverPO = channelHandlerReceiverPO;
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
	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Object m = e.getMessage();
		if (logger.isDebugEnabled()) {
			if (++counter % printMessageEach == 0) {
				logger.debug(new Date() + ":Received message No." + counter);
			}
		}
		channelHandlerReceiverPO.newMessage((R) m);
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
		logger.error("Exception caught: " + e.toString());
		ctx.getChannel().close();
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
					cp.addLast("application", ChannelReceiverDelegate.this);
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

	public synchronized void close() {
		for (ChannelHandlerContext ctx:channelHandlerContextList){
			ctx.getChannel().close();
		}
		openChannel.disconnect();
		openChannel.close().awaitUninterruptibly();
	}

}
