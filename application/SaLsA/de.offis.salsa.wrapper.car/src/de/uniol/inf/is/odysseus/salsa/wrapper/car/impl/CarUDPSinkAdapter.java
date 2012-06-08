package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractSinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class CarUDPSinkAdapter extends AbstractSinkAdapter implements
		SinkAdapter {
	private static Logger LOG = LoggerFactory
			.getLogger(CarUDPSinkAdapter.class);

	private DatagramChannel channel;
	private final Map<SinkSpec, SocketAddress> sinks = new HashMap<SinkSpec, SocketAddress>();
	private final ByteBuffer buffer;

	public CarUDPSinkAdapter() {
		buffer = ByteBuffer.allocate(64 * 1024);
		try {
			channel = DatagramChannel.open();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public String getName() {
		return "SALSA-Car-UDP";
	}

	@Override
	public void transfer(SinkSpec sink, long timestamp, Object[] data) {
		SocketAddress addr = sinks.get(sink);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.clear();
		calendar.setTimeInMillis(timestamp);
		try {
			if (data.length >= 2) {
				synchronized (buffer) {
					buffer.putChar((char) calendar.get(Calendar.YEAR));
					buffer.put((byte) (calendar.get(Calendar.MONTH) + 1));
					buffer.put((byte) calendar.get(Calendar.DATE));
					buffer.put((byte) calendar.get(Calendar.HOUR_OF_DAY));
					buffer.put((byte) calendar.get(Calendar.MINUTE));
					buffer.put((byte) calendar.get(Calendar.SECOND));
					buffer.put((byte) (calendar.get(Calendar.MILLISECOND) / 10));
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
							buffer.put((byte) ((1.0 - Math.exp(-grid.get(x, y))) * 100));
						}
					}
					buffer.flip();
					this.channel.send(buffer, addr);
					if (buffer.hasRemaining()) {
						buffer.compact();
					} else {
						buffer.clear();
					}
				}
			} else {
				LOG.error("Invalid Parameters in Car-Sink");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			buffer.clear();
		}
	}

	@Override
	protected void destroy(SinkSpec sinkSpec) {
		this.sinks.remove(sinkSpec);
	}

	@Override
	protected void init(SinkSpec sinkSpec) {
		if (!sinks.containsKey(sinkSpec)) {
			String host = "";
			int port = 4444;
			if (sinkSpec.getConfiguration().containsKey("port")) {
				port = Integer.parseInt(sinkSpec.getConfiguration().get("port")
						.toString());
			}
			if (sinkSpec.getConfiguration().containsKey("host")) {
				host = sinkSpec.getConfiguration().get("host").toString();
			}
			this.sinks.put(sinkSpec, new InetSocketAddress(host, port));
		}
	}

}
