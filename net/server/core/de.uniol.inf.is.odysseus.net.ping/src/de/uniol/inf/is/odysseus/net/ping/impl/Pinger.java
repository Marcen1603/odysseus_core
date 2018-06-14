package de.uniol.inf.is.odysseus.net.ping.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManagerListener;
import de.uniol.inf.is.odysseus.net.util.RepeatingJobThread;

public class Pinger extends RepeatingJobThread implements IOdysseusNodeCommunicatorListener, IOdysseusNetComponent, IOdysseusNodeConnectionManagerListener {

	private static final Logger LOG = LoggerFactory.getLogger(Pinger.class);
	private static final Random RAND = new Random();
	private static final int MIN_NODES_TO_PING = 10;
	private static final double NODES_TO_PING_PERCENTAGE = 0.70;

	private static final int PING_INTERVAL = 5000;
	private static final int MAX_PONG_WAIT_MILLIS = 30000;

	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNodeCommunicator nodeCommunicator;
	private static IOdysseusNodeConnectionManager connectionManager;
	private static Pinger instance;

	private final Map<IOdysseusNode, Long> waitingPongMap = Maps.newHashMap();

	public Pinger() {
		super(PING_INTERVAL, "Ping Thread");
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;

		nodeCommunicator.registerMessageType(PingMessage.class);
		nodeCommunicator.registerMessageType(PongMessage.class);
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {

			nodeCommunicator.unregisterMessageType(PingMessage.class);
			nodeCommunicator.unregisterMessageType(PongMessage.class);

			nodeCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		connectionManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		if (connectionManager == serv) {
			connectionManager = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		instance = this;

		nodeCommunicator.addListener(this, PingMessage.class);
		nodeCommunicator.addListener(this, PongMessage.class);

		connectionManager.addListener(this);
	}

	// called by OSGi-DS
	public void deactivate() {
		instance = null;

		if (nodeCommunicator != null) {
			nodeCommunicator.removeListener(this, PingMessage.class);
			nodeCommunicator.removeListener(this, PongMessage.class);
		}

		synchronized (waitingPongMap) {
			waitingPongMap.clear();
		}
		
		connectionManager.removeListener(this);
	}

	public static Pinger getInstance() {
		return instance;
	}

	@Override
	public void doJob() {
		Collection<IOdysseusNode> remoteNodes = nodeCommunicator.getDestinationNodes();

		cleanWaitingPongMap();

		Collection<IOdysseusNode> selectedNodes = selectRandomNodes(remoteNodes);

		try {
			if (!selectedNodes.isEmpty()) {
				IMessage pingMessage = new PingMessage();
				for (IOdysseusNode remoteNode : selectedNodes) {
					LOG.debug("Send ping-message to node {}", remoteNode);

					nodeCommunicator.send(remoteNode, pingMessage);
					synchronized (waitingPongMap) {
						waitingPongMap.put(remoteNode, System.currentTimeMillis());
					}
				}
			} else {
				if (LOG.isDebugEnabled()) {
					synchronized (waitingPongMap) {
						LOG.debug("No nodes for pinging currently available.");
						if (!waitingPongMap.isEmpty()) {
							LOG.debug("Waiting for {} pong messages...", waitingPongMap.size());
						}
					}
				}
			}
		} catch (OdysseusNodeCommunicationException e) {
			LOG.debug("Could not send ping message", e);
		}
	}

	private void cleanWaitingPongMap() {
		long timestamp = System.currentTimeMillis();
		synchronized (waitingPongMap) {
			for (IOdysseusNode waitingNode : waitingPongMap.keySet().toArray(new IOdysseusNode[0])) {
				if (timestamp - waitingPongMap.get(waitingNode) > MAX_PONG_WAIT_MILLIS) {
					waitingPongMap.remove(waitingNode);
				}
			}
		}
	}

	private Collection<IOdysseusNode> selectRandomNodes(Collection<IOdysseusNode> remoteNodes) {
		List<IOdysseusNode> availableNodes = Lists.newArrayList(remoteNodes);
		synchronized (waitingPongMap) {
			availableNodes.removeAll(waitingPongMap.keySet());
		}

		int nodesToPingCount = Math.max(MIN_NODES_TO_PING, (int) (availableNodes.size() * NODES_TO_PING_PERCENTAGE));

		if (availableNodes.size() <= nodesToPingCount) {
			return Lists.newArrayList(availableNodes);
		}

		Collection<IOdysseusNode> selectedNodes = Lists.newArrayList();
		while (!availableNodes.isEmpty() && selectedNodes.size() < nodesToPingCount) {
			int index = RAND.nextInt(availableNodes.size());

			selectedNodes.add(availableNodes.remove(index));
		}

		return selectedNodes;
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof PingMessage && PingMap.isActivated()) {
			LOG.debug("Got ping message from node {}", senderNode);
			PingMessage pingMessage = (PingMessage) message;

			IMessage pongMessage = new PongMessage(pingMessage, PingMap.getInstance().getLocalPosition());
			try {
				communicator.send(senderNode, pongMessage);
			} catch (OdysseusNodeCommunicationException e) {
				LOG.debug("Could not send pong-message", e);
			}
		} else if (message instanceof PongMessage) {
			LOG.debug("Got pong message from node {}", senderNode);
			PongMessage pongMessage = (PongMessage) message;
			synchronized (waitingPongMap) {
				waitingPongMap.remove(senderNode);
			}

			long latency = System.currentTimeMillis() - pongMessage.getTimestamp();
			LOG.debug("Latency to node {} is now {} ms", senderNode, latency);
			if(PingMap.isActivated()) {
				PingMap.getInstance().update(senderNode, pongMessage.getPosition(), latency);
			}
		}
	}

	// Important: start is called in RepeatingJobThread from
	// OdysseusNetComponent

	@Override
	public void init(IOdysseusNode localNode) throws OdysseusNetException {
		// do nothing
	}
	
	@Override
	public void startFinished() throws OdysseusNetException {
		// do nothing
	}

	@Override
	public void stop() {
		stopRunning();

		PingMap.getInstance().clearPingNodes();
	}

	@Override
	public void terminate(IOdysseusNode localNode) {
		// do nothing
	}

	@Override
	public void nodeConnected(IOdysseusNodeConnection connection) {
		// do nothing
	}

	@Override
	public void nodeDisconnected(IOdysseusNodeConnection connection) {
		PingMap.getInstance().removePingNode(connection.getOdysseusNode());
	}
}
