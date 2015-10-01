package de.uniol.inf.is.odysseus.net.discovery.broadcast.request;

import com.google.common.base.Preconditions;

import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramChannel;

public final class BroadcastRequestSendThread extends Thread {

	private final long intervalMillis;
	private final DatagramChannel channel;

	private boolean isRunning = false;

	public BroadcastRequestSendThread(long timeIntervalMillis, DatagramChannel datagramChannel) {
		Preconditions.checkArgument(timeIntervalMillis > 0, "Time interval (in ms) must be positive and non-zero");
		this.channel =  Preconditions.checkNotNull(datagramChannel, "channel must not be null!");

		this.intervalMillis = timeIntervalMillis;
	}

	@Override
	public void run() {
		isRunning = true;

		while (isRunning) {
			sendMessage(channel);

			waitTimeInterval(intervalMillis);
		}
	}

	private static void sendMessage(Channel channel) {
//		channel.wr
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
