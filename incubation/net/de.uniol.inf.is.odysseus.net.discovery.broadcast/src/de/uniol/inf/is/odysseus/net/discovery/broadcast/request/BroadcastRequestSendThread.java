package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.discovery.broadcast.BroadcastDiscoveryPlugIn;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public final class BroadcastRequestSendThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(BroadcastRequestSendThread.class);
	
	private static final InetSocketAddress BROADCAST_SOCKET_ADDRESS = new InetSocketAddress("255.255.255.255", BroadcastDiscoveryPlugIn.BROADCAST_REQUEST_PORT);
	private final long intervalMillis;
	private final DatagramChannel channel;

	private boolean isRunning = false;

	public BroadcastRequestSendThread(long timeIntervalMillis, DatagramChannel datagramChannel) {
		Preconditions.checkArgument(timeIntervalMillis > 0, "Time interval (in ms) must be positive and non-zero");

		this.channel =  Preconditions.checkNotNull(datagramChannel, "channel must not be null!");
		this.intervalMillis = timeIntervalMillis;
		
		setName("Broadcast Request sender");
		setDaemon(true);
	}

	@Override
	public void run() {
		LOG.debug("Broadcast request sender thread started");
		isRunning = true;

		while (isRunning) {
			try {
				sendMessage(channel);
				
				waitTimeInterval(intervalMillis);
			} catch (InterruptedException e) {
				LOG.error("Exception during sending broadcast request message", e);
				isRunning = false;
			}
		}
		LOG.debug("Broadcast request sender thread stopped");
	}

	private static void sendMessage(Channel channel) throws InterruptedException {
		LOG.debug("Send broadcast request message");
		
		channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(BroadcastDiscoveryPlugIn.BROADCAST_REQUEST_CONTENT, CharsetUtil.UTF_8), BROADCAST_SOCKET_ADDRESS)).sync();
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
