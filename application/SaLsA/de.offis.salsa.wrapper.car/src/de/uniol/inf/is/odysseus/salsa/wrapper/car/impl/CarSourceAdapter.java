package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class CarSourceAdapter extends AbstractPushingSourceAdapter implements
		SourceAdapter {
	private static Logger LOG = LoggerFactory.getLogger(CarSourceAdapter.class);
	private static final int LENGTH = 24;
	private final Map<SourceSpec, Thread> serverThreads = new HashMap<SourceSpec, Thread>();

	@Override
	public String getName() {
		return "SALSA-Car";
	}

	@Override
	protected void doDestroy(SourceSpec source) {
		this.serverThreads.get(source).interrupt();
		this.serverThreads.remove(source);
	}

	@Override
	protected void doInit(SourceSpec source) {
		int port = 4444;
		if (source.getConfiguration().containsKey("port")) {
			port = Integer.parseInt(source.getConfiguration().get("port")
					.toString());
		}
		final CarServer server = new CarServer(source, this, port);
		final Thread serverThread = new Thread(server);
		this.serverThreads.put(source, serverThread);
		serverThread.start();
	}

	class CarServer implements Runnable {
		private ServerSocketChannel serverSocketChannel = null;
		private final SourceSpec source;
		private final CarSourceAdapter adapter;
		private int port;

		public CarServer(final SourceSpec source,
				final CarSourceAdapter adapter, final int port) {
			this.port = port;
			this.source = source;
			this.adapter = adapter;
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
					SocketChannel socket;
					try {
						socket = this.serverSocketChannel.accept();
						final CarProcessor processor = new CarProcessor(
								this.source, socket, this.adapter);
						final Thread processingThread = new Thread(processor);
						processingThreads.add(processingThread);
						processingThread.start();
					} catch (final IOException e) {
						CarSourceAdapter.LOG.error(e.getMessage(), e);
					}
				}
				for (final Thread processingThread : processingThreads) {
					processingThread.interrupt();
				}
			} catch (final Exception e) {
				if (CarSourceAdapter.LOG.isDebugEnabled()) {
					CarSourceAdapter.LOG.debug(e.getMessage(), e);
				}
			}
		}

		class CarProcessor implements Runnable {
			private final SocketChannel channel;
			private final CarSourceAdapter adapter;
			private final SourceSpec sourceSpec;

			public CarProcessor(final SourceSpec source,
					final SocketChannel channel, final CarSourceAdapter adapter) {
				this.channel = channel;
				this.adapter = adapter;
				this.sourceSpec = source;
			}

			@Override
			public void run() {
				try {

					final ByteBuffer buffer = ByteBuffer
							.allocateDirect(64 * 1024);
					int nbytes = 0;

					while (!Thread.currentThread().isInterrupted()) {
						if (channel.isOpen()) {
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
									Calendar calendar = Calendar.getInstance();
									calendar.clear();
									calendar.set(year, month - 1, day, hour,
											minute, second);
									calendar.add(Calendar.MILLISECOND,
											millisecond * 10);
									long timestamp = calendar.getTimeInMillis();
									int id = buffer.getShort();
									int x = buffer.getInt();
									int y = buffer.getInt();
									float speed = buffer.getFloat();
									float angle = buffer.getFloat();
									this.adapter.transfer(this.sourceSpec,
											timestamp, new Object[] { id,
													new Coordinate(x, y),
													speed, angle });

								} catch (final Exception e) {
									if (CarSourceAdapter.LOG.isDebugEnabled()) {
										CarSourceAdapter.LOG.debug(
												e.getMessage(), e);
										this.dumpPackage(buffer);
									}
									buffer.position(pos);
								}
								if (buffer.hasRemaining()) {
									buffer.compact();
								} else {
									buffer.clear();
								}
							}
						}
					}

				} catch (final IOException e) {
					CarSourceAdapter.LOG.error(e.getMessage(), e);
				}
			}

			private void dumpPackage(final ByteBuffer buffer)
					throws FileNotFoundException {
				final File debug = new File(getName() + "-"
						+ sourceSpec.getName() + "-debug.out");
				final FileOutputStream out = new FileOutputStream(debug, true);
				final FileChannel debugChannel = out.getChannel();
				if ((debugChannel != null) && (debugChannel.isOpen())) {
					try {
						buffer.flip();
						debugChannel.write(buffer);
					} catch (final IOException e) {
						CarSourceAdapter.LOG.error(e.getMessage(), e);
					}
				}
			}
		}
	}
}
