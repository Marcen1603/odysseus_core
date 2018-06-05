package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import java.net.InetSocketAddress;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public final class BroadcastRequestSendThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestSendThread.class);
	
	private final long intervalMillis;
	private final DatagramChannel channel;
	private final Collection<InetSocketAddress> broadcastAddresses;
	
	private boolean isRunning = false;

	public BroadcastRequestSendThread(long timeIntervalMillis, DatagramChannel datagramChannel) {
		Preconditions.checkArgument(timeIntervalMillis > 0, "Time interval (in ms) must be positive and non-zero");
		Preconditions.checkNotNull(datagramChannel, "datagramChannel must not be null!");

		this.channel =  Preconditions.checkNotNull(datagramChannel, "channel must not be null!");
		this.intervalMillis = timeIntervalMillis;
		this.broadcastAddresses = buildBroadcastAddresses();
		
		setName("Broadcast Request sender");
		setDaemon(true);
	}

	private static Collection<InetSocketAddress> buildBroadcastAddresses() {
		Collection<InetSocketAddress> result = Lists.newArrayList();
		for( int port = BroadcastDiscoveryPlugIn.BROADCAST_PORT_MIN; port <= BroadcastDiscoveryPlugIn.BROADCAST_PORT_MAX; port++) {
			InetSocketAddress address = new InetSocketAddress("255.255.255.255", port);
			result.add(address);
		}
		return result;
	}

	@Override
	public void run() {
		LOG.debug("Broadcast request sender thread started");
		isRunning = true;

		while (isRunning) {
			try {
				sendMessage(channel);
				
				waitTimeInterval(intervalMillis);
				
				OdysseusNodesChecker.detectLostNodes();
				
			} catch (InterruptedException e) {
				LOG.error("Exception during sending broadcast request message", e);
				isRunning = false;
			}
		}
		LOG.debug("Broadcast request sender thread stopped");
	}

	private void sendMessage(Channel channel) throws InterruptedException {
		//LOG.debug("Send broadcast request message");
		
		for( InetSocketAddress address : broadcastAddresses ) {
			DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(BroadcastDiscoveryPlugIn.BROADCAST_REQUEST_CONTENT, CharsetUtil.UTF_8), address);
			
			channel.writeAndFlush(packet).sync();
		}
	}

	private static void waitTimeInterval(long timeMillis) {
		try {
			Thread.sleep(timeMillis);
		} catch (InterruptedException e) {
		}
	}

	public void stopRunning() {
		isRunning = false;
	}
}
