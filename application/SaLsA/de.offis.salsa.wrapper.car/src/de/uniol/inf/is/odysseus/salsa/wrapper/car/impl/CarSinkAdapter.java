package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractSinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class CarSinkAdapter extends AbstractSinkAdapter implements SinkAdapter {
	private static Logger LOG = LoggerFactory.getLogger(CarSinkAdapter.class);
	private final BlockingQueue<Object[]> messageQueue = new LinkedBlockingQueue<Object[]>();
	private final Map<SinkSpec, Thread> connectionThreads = new HashMap<SinkSpec, Thread>();

	class CarConnectionHandler extends Thread {

		private final String host;
		private final int port;
		private SocketChannel channel;
		private final ByteBuffer buffer;

		public CarConnectionHandler(final String host, final int port) {
			this.host = host;
			this.port = port;
			buffer = ByteBuffer.allocateDirect(1024);
		}

		public void run() {
			try {
				this.channel = SocketChannel.open();
				final InetSocketAddress address = new InetSocketAddress(
						this.host, this.port);
				this.channel.connect(address);
				this.channel.configureBlocking(false);
				while (!Thread.currentThread().isInterrupted()) {
					Object[] event = messageQueue.take();
					long timestamp = (Long) event[0];
					Object[] data = (Object[]) event[1];
					buffer.putLong(timestamp);
					// ID
					buffer.putShort((Short) data[0]);
					// X Position
					buffer.putShort((Short) data[1]);
					// Y Position
					buffer.putShort((Short) data[2]);
					// Grid Length
					buffer.putShort((Short) data[3]);
					// Grid Width
					buffer.putShort((Short) data[4]);
					// Grid Height
					buffer.putShort((Short) data[5]);
					// Cell Size
					buffer.putInt((Integer) data[6]);
					// Grid
					Byte[][] grid = (Byte[][]) data[7];
					for (int l = 0; l < grid.length; l++) {
						for (int w = 0; w < grid[l].length; w++) {
							buffer.put(grid[l][w]);
						}
					}
					buffer.flip();
					this.channel.write(buffer);
					if (buffer.hasRemaining()) {
						buffer.compact();
					} else {
						buffer.clear();
					}

				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				if (this.channel != null) {
					try {
						this.channel.close();
					} catch (final IOException e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	@Override
	public String getName() {
		return "SALSA-Car";
	}

	@Override
	public void transfer(SinkSpec sink, long timestamp, Object[] data) {
		this.messageQueue.offer(new Object[] { timestamp, data });
	}

	@Override
	protected void destroy(SinkSpec sinkSpec) {
		this.connectionThreads.get(sinkSpec).interrupt();
		this.connectionThreads.remove(sinkSpec);
	}

	@Override
	protected void init(SinkSpec sinkSpec) {
		if (!connectionThreads.containsKey(sinkSpec)) {
			int port = 4444;
			String host = "localhost";
			if (sinkSpec.getConfiguration().containsKey("port")) {
				port = Integer.parseInt(sinkSpec.getConfiguration().get("port")
						.toString());
			}
			if (sinkSpec.getConfiguration().containsKey("host")) {
				host = sinkSpec.getConfiguration().get("host").toString();
			}
			CarConnectionHandler connection = new CarConnectionHandler(host,
					port);
			this.connectionThreads.put(sinkSpec, connection);
			connection.start();
		}
	}

}
