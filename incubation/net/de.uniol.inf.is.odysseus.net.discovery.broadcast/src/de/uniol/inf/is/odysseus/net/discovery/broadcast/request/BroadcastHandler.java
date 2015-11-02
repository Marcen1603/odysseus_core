package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class BroadcastHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastHandler.class);

	private final IOdysseusNode localNode;
	private final int ownBroadcastPort;
	
	private final IOdysseusNodeManager manager;
	

	public BroadcastHandler(IOdysseusNode localNode, IOdysseusNodeManager manager, int broadcastPort) {
		Preconditions.checkNotNull(manager, "manager must not be null!");
		Preconditions.checkNotNull(localNode, "localNode must not be null!");

		this.localNode = localNode;
		this.manager = manager;
		this.ownBroadcastPort = broadcastPort;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		if( isOwnMessage(msg)) {
			return;
		}
		
		String msgText = msg.content().toString(CharsetUtil.UTF_8);
		if (BroadcastDiscoveryPlugIn.BROADCAST_REQUEST_CONTENT.equals(msgText)) {
			LOG.debug("Got broadcast request message from {}", msg.sender());
			sendAnswerMessage(ctx, msg.sender());
			
		} else {
			LOG.debug("Process answer message from {}", msg.sender());
			processAnswerMessage(msg.sender().getAddress(), msgText);
		}
	}

	private boolean isOwnMessage(DatagramPacket msg) {
		try {
			InetSocketAddress sender = msg.sender();
			return sender.getAddress().equals(InetAddress.getLocalHost()) && sender.getPort() == ownBroadcastPort;
		} catch (UnknownHostException e) {
			return false;
		}
	}

	private void sendAnswerMessage(ChannelHandlerContext ctx, InetSocketAddress destination) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("nodeName", localNode.getName());
		json.put("nodeID", localNode.getID());
		
		JSONObject propertyJson = new JSONObject();
		for( String propertyKey : localNode.getProperyKeys() ) {
			String propertyValue = localNode.getProperty(propertyKey).get();
			propertyJson.put(propertyKey, propertyValue);
		}
		json.put("properties", propertyJson);
		
		String jsonString = json.toString();
		DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(jsonString, CharsetUtil.UTF_8), destination);

		LOG.debug("Sending request answer to {}", packet.recipient());
		ctx.write(packet);
	}

	private void processAnswerMessage(InetAddress sender, String msgText) throws JSONException {
		JSONObject jsonObject = new JSONObject(msgText);
		if (jsonObject.has("nodeName") && jsonObject.has("nodeID")) {
			String nodeName = jsonObject.getString("nodeName");
			String nodeIDStr = jsonObject.getString("nodeID");
			IOdysseusNode newNode = new OdysseusNode(OdysseusNodeID.fromString(nodeIDStr), nodeName);
			
			JSONObject propertyJson = jsonObject.getJSONObject("properties");
			Iterator<?> keyIterator = propertyJson.keys();
			while( keyIterator.hasNext() ) {
				String key = (String)keyIterator.next();
				String value = propertyJson.getString(key);
				newNode.addProperty(key, value);
			}

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
