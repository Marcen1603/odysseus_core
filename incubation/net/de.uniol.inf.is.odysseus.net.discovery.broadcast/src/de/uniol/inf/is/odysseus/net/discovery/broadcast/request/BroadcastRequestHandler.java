package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import java.net.InetSocketAddress;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class BroadcastRequestHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestHandler.class);
	private static final int RANDOM_NUM = new Random().nextInt(1000);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		if(BroadcastDiscoveryPlugIn.BROADCAST_REQUEST_CONTENT.equals(msg.content().toString(CharsetUtil.UTF_8))) {
			LOG.debug("Got broadcast request message");
			
			JSONObject json = new JSONObject();
			json.put("nodeName", "OdysseusNode_" + RANDOM_NUM);
			
			InetSocketAddress address = new InetSocketAddress(msg.sender().getAddress(), BroadcastDiscoveryPlugIn.BROADCAST_ANSWER_PORT);
			DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(json.toString().getBytes()), address);
			
			LOG.debug("Sending request answer to {}", packet.recipient());
			ctx.write(packet);
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		ctx.flush();
	}
}
