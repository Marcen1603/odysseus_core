package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class CarGridUDPSourceAdapter extends AbstractPushingSourceAdapter {
	private static Logger LOG = LoggerFactory
			.getLogger(CarGridUDPSourceAdapter.class);

	private final Map<SourceSpec, GridConnection> connections = new ConcurrentHashMap<SourceSpec, GridConnection>();

	@Override
	public String getName() {
		return "Grid-UDP";
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
			final int port = Integer.parseInt(source.getConfiguration()
					.get("port").toString());
			final GridConnection connection = new GridConnection(port);

			this.connections.put(source, connection);
			connection.setListener(source, this);
			connection.start();
		}
	}

	class GridConnection extends Thread {
		private final int port;
		private final ByteBuffer buffer;
		private CarGridUDPSourceAdapter listener;
		private SourceSpec source;

		public GridConnection(int port) {
			this.port = port;
			this.buffer = ByteBuffer.allocateDirect(64 * 1024);
		}

		@Override
		public void run() {
			DatagramChannel channel = null;
			long timestamp = 0l;
			try {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						channel = DatagramChannel.open();
						final InetSocketAddress address = new InetSocketAddress(
								this.port);
						channel.socket().bind(address);
						channel.configureBlocking(true);
						while ((!Thread.currentThread().isInterrupted())
								&& (channel.isOpen())) {
							if (buffer.position() > 28) {
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
									Coordinate origin = new Coordinate(
											buffer.getInt(), buffer.getInt());
									short width = buffer.getShort();
									short height = buffer.getShort();
									short future = buffer.getShort();
									int cell = buffer.getInt() / 10;

									CartesianGrid grid = new CartesianGrid(
											origin, width, height, cell);
									buffer.compact();
									while (buffer.position() < width * height
											* future) {
										channel.receive(buffer);
									}
									pos = buffer.position();
									buffer.flip();
									// FIXME Use 3D Grid when height>1
									for (int x = 0; x < width; x++) {
										for (int y = 0; y < height; y++) {
											grid.set(
													x,
													y,
													new Double(
															-Math.log(1.0 - buffer
																	.get() / 100)));
										}
									}
									if (calendar.getTimeInMillis() >= timestamp) {
										this.listener.transfer(source,
												calendar.getTimeInMillis(),
												new Object[] { id, grid });
										timestamp = calendar.getTimeInMillis();
									}
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
