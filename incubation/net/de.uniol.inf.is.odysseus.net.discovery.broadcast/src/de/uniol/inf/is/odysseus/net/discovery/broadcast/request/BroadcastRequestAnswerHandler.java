package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.impl.BroadcastOdysseusNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class BroadcastRequestAnswerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestAnswerHandler.class);
	
	private final IOdysseusNodeManager manager;
	
	public BroadcastRequestAnswerHandler(IOdysseusNodeManager manager) {
		this.manager = Preconditions.checkNotNull(manager, "manager must not be null!");
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		LOG.debug("Received answer from {}", msg.sender());
		String jsonString = msg.content().toString(CharsetUtil.UTF_8);
		
		JSONObject jsonObject = new JSONObject(jsonString);
		if( jsonObject.has("nodeName") && jsonObject.has("nodeID")) {
			String nodeName = jsonObject.getString("nodeName");
			String nodeIDStr = jsonObject.getString("nodeID");
			
			IOdysseusNode newNode = new BroadcastOdysseusNode(OdysseusNodeID.fromString(nodeIDStr), nodeName, msg.sender().getAddress());
			if( !manager.existsNode(newNode)) {
				manager.addNode(newNode);
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		
		ctx.flush();
	}
}
