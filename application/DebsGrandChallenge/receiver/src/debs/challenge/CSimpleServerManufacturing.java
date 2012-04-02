/**
Copyright (c) 2011, Zbigniew Jerzak

All rights reserved.
 **/

package debs.challenge;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import debs.challenge.msg.CManufacturingMessages.CDataPoint;
import debs.challenge.msg.TupleReaderMessages.TupleMessage;

public class CSimpleServerManufacturing extends SimpleChannelHandler {
	private long i = 0;
	private long modulo = 1;

	private Logger logger = Logger.getLogger(CSimpleServerManufacturing.class
			.getCanonicalName());

	public CSimpleServerManufacturing(final InetSocketAddress sa, final long mod) {
		modulo = mod;
		new CTCPPacketReceiver(sa, this, TupleMessage.getDefaultInstance());
		logger.info("Started server @ "+sa.getAddress()+":" + sa.getPort() + ". Printing every " + modulo + " message." );
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		new CSimpleServerManufacturing(new InetSocketAddress(args[0], Integer.parseInt(args[1])), Long.parseLong(args[2]));
		new CSimpleServerManufacturing(new InetSocketAddress("localhost", 9999), 1);
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
	public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) {
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
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Client connected: " + ctx.getChannel().getRemoteAddress()
				+ " :> " + ctx.getChannel().getLocalAddress());
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
		if (++i % modulo == 0) {
			CDataPoint measurement = (CDataPoint) e.getMessage();
			logger.info("CFullMeasurement: "
					+ measurement.toString().replaceAll("\n", "; "));
		}
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
}
