package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.impl.BroadcastOdysseusNode;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class BroadcastRequestHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestHandler.class);

	private final String ownNodeName;
	private final OdysseusNodeID ownNodeID;
	private final IOdysseusNodeManager manager;

	public BroadcastRequestHandler(OdysseusNetStartupData data, IOdysseusNodeManager manager) {
		Preconditions.checkNotNull(manager, "manager must not be null!");
		Preconditions.checkNotNull(data, "Data must not be null!");

		this.ownNodeName = data.getNodeName();
		this.ownNodeID = data.getNodeID();
		this.manager = manager;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		String msgText = msg.content().toString(CharsetUtil.UTF_8);
		if (BroadcastDiscoveryPlugIn.BROADCAST_REQUEST_CONTENT.equals(msgText)) {
			sendAnswerMessage(ctx, msg.sender());
		} else {
			processAnswerMessage(msg.sender().getAddress(), msgText);
		}
	}

	private void sendAnswerMessage(ChannelHandlerContext ctx, InetSocketAddress destination) throws JSONException {
		LOG.debug("Got broadcast request message");

		JSONObject json = new JSONObject();
		json.put("nodeName", ownNodeName);
		json.put("nodeID", ownNodeID);

		String jsonString = json.toString();
		DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(jsonString, CharsetUtil.UTF_8), destination);

		LOG.debug("Sending request answer to {}", packet.recipient());
		ctx.write(packet);
	}

	private void processAnswerMessage(InetAddress sender, String msgText) throws JSONException {
		LOG.debug("Process answer message from {}", sender);
		JSONObject jsonObject = new JSONObject(msgText);
		if (jsonObject.has("nodeName") && jsonObject.has("nodeID")) {
			String nodeName = jsonObject.getString("nodeName");
			String nodeIDStr = jsonObject.getString("nodeID");

			IOdysseusNode newNode = new BroadcastOdysseusNode(OdysseusNodeID.fromString(nodeIDStr), nodeName, sender);
			if (!manager.existsNode(newNode)) {
				manager.addNode(newNode);
				LOG.debug("New node added: {}", newNode);
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		ctx.flush();
	}
}
