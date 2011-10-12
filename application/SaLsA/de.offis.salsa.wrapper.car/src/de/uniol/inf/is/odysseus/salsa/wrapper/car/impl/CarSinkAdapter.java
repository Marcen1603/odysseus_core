package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

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
			buffer = ByteBuffer.allocateDirect(64 * 1024);
		}

		public void run() {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					this.channel = SocketChannel.open();
					final InetSocketAddress address = new InetSocketAddress(
							this.host, this.port);
					this.channel.connect(address);
					this.channel.configureBlocking(false);
					while ((!Thread.currentThread().isInterrupted())
							&& (this.channel.isOpen())) {
						Object[] event = messageQueue.take();
						long timestamp = (Long) event[0];
						Object[] data = (Object[]) event[1];
						Calendar calendar = Calendar.getInstance();
						calendar.clear();
						calendar.setTimeInMillis(timestamp);
						try {
							buffer.putChar((char) calendar.get(Calendar.YEAR));
							buffer.put((byte) (calendar.get(Calendar.MONTH) + 1));
							buffer.put((byte) calendar.get(Calendar.DATE));
							buffer.put((byte) calendar
									.get(Calendar.HOUR_OF_DAY));
							buffer.put((byte) calendar.get(Calendar.MINUTE));
							buffer.put((byte) calendar.get(Calendar.SECOND));
							buffer.put((byte) (calendar
									.get(Calendar.MILLISECOND) / 10));
							// ID
							buffer.putShort(((Integer) data[0]).shortValue());
							// Position
							Coordinate position = (Coordinate) data[1];
							// Grid
							Double[][] grid = (Double[][]) data[3];

							// Cellsize
							Double cellsize = (Double) data[2];

							int globalX = (int) (position.x / cellsize
									.intValue());
							int globalY = (int) (position.y / cellsize
									.intValue());
							// X Position
							buffer.putShort((short) globalX);
							// Y Position
							buffer.putShort((short) globalY);
							// Grid Length
							buffer.putShort((short) grid.length);
							// Grid Width
							buffer.putShort((short) grid[0].length);
							// Grid Height
							buffer.putShort((short) 1);
							// Cell Size
							buffer.putInt(cellsize.intValue());

							for (int l = 0; l < grid.length; l++) {
								for (int w = 0; w < grid[l].length; w++) {
									if (grid[l][w] == 0.0) {
										buffer.put((byte) 0x00);
									} else if (grid[l][w] < 0.0) {
										buffer.put((byte) 0xFF);
									} else {
										buffer.put((byte) 0x64);
									}
								}
							}
							buffer.flip();
							this.channel.write(buffer);
							if (buffer.hasRemaining()) {
								buffer.compact();
							} else {
								buffer.clear();
							}
						} catch (Exception e) {
							buffer.clear();
						}
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
