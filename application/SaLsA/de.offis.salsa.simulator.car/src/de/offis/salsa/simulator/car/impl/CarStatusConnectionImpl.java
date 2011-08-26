package de.offis.salsa.simulator.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CarStatusConnectionImpl extends Thread {
	private final String host;
	private final int port;
	private final ByteBuffer buffer;

	public CarStatusConnectionImpl(String host, int port) {
		this.host = host;
		this.port = port;
		this.buffer = ByteBuffer.allocateDirect(1024);
	}

	@Override
	public void run() {
		SocketChannel channel = null;
		try {
			channel = SocketChannel.open();
			final InetSocketAddress address = new InetSocketAddress(this.host,
					this.port);
			channel.connect(address);
			channel.configureBlocking(false);
			short id = (short) (Math.random() * 100);
			short x = 0;
			short y = 0;
			float speed = 0;
			float angle = 0;
			while ((!Thread.currentThread().isInterrupted())
					&& (channel.isOpen())) {
				long timestamp = System.currentTimeMillis();

				double direction = Math.random();
				short tmpX = x;
				short tmpY = y;
				if (direction > 0.5) {
					if (direction > 0.75) {
						x--;
						y++;
					} else {
						x++;
						y++;
					}
				} else {
					if (direction > 0.25) {
						x++;
						y--;
					} else {
						x--;
						y--;
					}
				}
				if (x < 0) {
					x = 0;
				}
				if (y < 0) {
					y = 0;
				}
				if (x - tmpX > 0) {
					angle = (float) Math.atan((y - tmpY) / (x - tmpX));
				} else {
					angle = 0;
				}
				speed = (float) (Math.random() * 100);
				sendStatus(channel, timestamp, id, x, y, speed, angle);
				Thread.sleep(50);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sendStatus(SocketChannel channel, long timestamp, short id,
			short x, short y, float speed, float angle) throws IOException {
		if (channel.isConnected()) {
			buffer.putLong(timestamp);
			buffer.putShort(id);
			buffer.putShort(x);
			buffer.putShort(y);
			buffer.putFloat(speed);
			buffer.putFloat(angle);
			buffer.flip();
			channel.write(buffer);
			if (buffer.hasRemaining()) {
				buffer.compact();
			} else {
				buffer.clear();
			}
		}
	}

}
