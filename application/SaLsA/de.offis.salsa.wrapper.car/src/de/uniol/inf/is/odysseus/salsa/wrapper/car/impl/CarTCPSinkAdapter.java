package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractSinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class CarTCPSinkAdapter extends AbstractSinkAdapter implements
		SinkAdapter {
	private static Logger LOG = LoggerFactory
			.getLogger(CarTCPSinkAdapter.class);

	private final BlockingQueue<Object[]> messageQueue = new LinkedBlockingQueue<Object[]>();
	private final Map<SinkSpec, Thread> connectionThreads = new HashMap<SinkSpec, Thread>();

	class CarServer extends Thread {
		private final int port;
		private ServerSocketChannel serverSocketChannel = null;

		public CarServer(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			try {
				this.serverSocketChannel = ServerSocketChannel.open();
				final InetSocketAddress address = new InetSocketAddress(
						this.port);
				this.serverSocketChannel.socket().bind(address);

				final List<Thread> processingThreads = new ArrayList<Thread>();
				while (!Thread.currentThread().isInterrupted()) {
					SocketChannel socket = null;
					try {
						socket = this.serverSocketChannel.accept();
						final CarConnectionHandler processor = new CarConnectionHandler(
								socket);
						final Thread processingThread = new Thread(processor);
						processingThreads.add(processingThread);
						processingThread.start();
					} catch (final IOException e) {
						LOG.error(e.getMessage(), e);
					}

				}
				for (final Thread processingThread : processingThreads) {
					processingThread.interrupt();
				}

			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	class CarConnectionHandler implements Runnable {
		private SocketChannel channel;
		private final ByteBuffer buffer;

		public CarConnectionHandler(SocketChannel socket) {
			this.channel = socket;
			this.buffer = ByteBuffer.allocateDirect(64 * 1024);
		}

		@Override
		public void run() {
			try {
				while ((!Thread.currentThread().isInterrupted())
						&& (this.channel.isConnected())) {
					Object[] event = messageQueue.take();
					long timestamp = (Long) event[0];
					Object[] data = (Object[]) event[1];
					Calendar calendar = Calendar.getInstance(TimeZone
							.getTimeZone("UTC"));
					calendar.clear();
					calendar.setTimeInMillis(timestamp);
					try {
						if (data.length >= 2) {
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
							buffer.putShort(((Double) data[0]).shortValue());
							// Grid
							Grid grid = (Grid) data[1];

							// X Position
							buffer.putInt((int) grid.origin.x);
							// Y Position
							buffer.putInt((int) grid.origin.y);
							// Grid Length
							buffer.putShort((short) grid.width);
							// Grid Width
							buffer.putShort((short) grid.height);
							// Grid Height
							buffer.putShort((short) 1);
							// Cell Size
							buffer.putInt((int) grid.cellsize * 10);

							for (int x = 0; x < grid.width; x++) {
								for (int y = 0; y < grid.height; y++) {
									buffer.put((byte) ((1.0 - Math.exp(-grid
											.get(x, y))) * 100));
								}
							}
							buffer.flip();
							this.channel.write(buffer);
							if (buffer.hasRemaining()) {
								buffer.compact();
							} else {
								buffer.clear();
							}
						} else {
							LOG.error("Invalid Parameters in Car-Sink");
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
						buffer.clear();
						if (this.channel != null) {
							this.channel.close();
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
		return "SALSA-Car-TCP";
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
			if (sinkSpec.getConfiguration().containsKey("port")) {
				port = Integer.parseInt(sinkSpec.getConfiguration().get("port")
						.toString());
			}
			CarServer server = new CarServer(port);
			this.connectionThreads.put(sinkSpec, server);
			server.start();
		}
	}

}
