package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.salsa.model.Grid2D;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class CarGridUDPSourceAdapter extends AbstractPushingSourceAdapter {
	private static Logger LOG = LoggerFactory
			.getLogger(CarGridUDPSourceAdapter.class);
	private final Map<SourceSpec, GridConnection> connections = new ConcurrentHashMap<SourceSpec, GridConnection>();

	@Override
	public String getName() {
		return "GridUDP";
	}

	@Override
	protected void doDestroy(SourceSpec source) {
		if (this.connections.containsKey(source)) {
			this.connections.get(source).interrupt();
			this.connections.remove(source);
		}
	}

	@Override
	protected void doInit(SourceSpec source) {
		if (!connections.containsKey(source)) {
			final String host = source.getConfiguration().get("host")
					.toString();
			final int port = Integer.parseInt(source.getConfiguration()
					.get("port").toString());
			final GridConnection connection = new GridConnection(host, port);

			this.connections.put(source, connection);
			connection.setListener(source, this);
			connection.start();
		}
	}

	class GridConnection extends Thread {
		private final String host;
		private final int port;
		private final ByteBuffer buffer;
		private CarGridUDPSourceAdapter listener;
		private SourceSpec source;

		public GridConnection(String host, int port) {
			this.host = host;
			this.port = port;
			this.buffer = ByteBuffer.allocateDirect(64 * 1024);
		}

		@Override
		public void run() {
			SocketChannel channel = null;
			try {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						channel = SocketChannel.open();
						final InetSocketAddress address = new InetSocketAddress(
								this.host, this.port);
						channel.connect(address);
						channel.configureBlocking(false);
						while ((!Thread.currentThread().isInterrupted())
								&& (channel.isOpen())) {
							int nbytes = 0;
							while ((nbytes = channel.read(buffer)) > 0) {
								int pos = buffer.position();
								buffer.flip();
								try {
									int year = buffer.getChar();
									int month = buffer.get();
									int day = buffer.get();
									int hour = buffer.get();
									int minute = buffer.get();
									int second = buffer.get();
									int millisecond = buffer.get();
									Calendar calendar = Calendar
											.getInstance(TimeZone
													.getTimeZone("UTC"));
									calendar.clear();
									calendar.set(year, month - 1, day, hour,
											minute, second);
									calendar.add(Calendar.MILLISECOND,
											millisecond * 10);
									short id = buffer.getShort();
									short x = buffer.getShort();
									short y = buffer.getShort();
									short length = buffer.getShort();
									short width = buffer.getShort();
									short height = buffer.getShort();
									int cell = buffer.getInt();

									Grid2D grid = new Grid2D(new Coordinate(x,
											y), width, height, cell);
									for (int l = 0; l < length; l++) {
										for (int w = 0; w < width; w++) {
											for (int h = 0; h < height; h++) {
												byte value = buffer.get();
												if (value > 0x64) {
													grid.set(l, w, -1.0);
												} else {
													grid.set(l, w, value / 100);
												}
											}
										}
									}
									this.listener.transfer(source,
											calendar.getTimeInMillis(),
											new Object[] { id, grid });
								} catch (Exception e) {
									e.printStackTrace();
									buffer.position(pos);
								}
								if (buffer.hasRemaining()) {
									buffer.compact();
								} else {
									buffer.clear();
								}
							}
						}
					} catch (IOException e) {
						if (LOG.isDebugEnabled()) {
							LOG.debug(e.getMessage(), e);
						}
						System.err.println("Connection closed.");
					}
					Thread.sleep(1000);
					System.out.println("Reconnecting...");
				}
			} catch (final Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				if (channel != null) {
					try {
						channel.close();
					} catch (final IOException e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}

		}

		public void setListener(final SourceSpec source,
				final CarGridUDPSourceAdapter listener) {
			this.listener = listener;
			this.source = source;
		}
	}

}
