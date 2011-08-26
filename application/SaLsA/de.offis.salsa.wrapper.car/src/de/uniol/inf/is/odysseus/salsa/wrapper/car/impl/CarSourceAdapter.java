package de.uniol.inf.is.odysseus.salsa.wrapper.car.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		private ServerSocket serverSocket = null;
		private SourceSpec source;
		private CarSourceAdapter adapter;

		public CarServer(final SourceSpec source,
				final CarSourceAdapter adapter, final int port) {
			try {
				this.serverSocket = new ServerSocket(port);
			} catch (final IOException e) {
				CarSourceAdapter.LOG.error(e.getMessage(), e);
			}
		}

		@Override
		public void run() {
			final List<Thread> processingThreads = new ArrayList<Thread>();
			while (!Thread.currentThread().isInterrupted()) {
				Socket socket;
				try {
					socket = this.serverSocket.accept();
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
		}

		class CarProcessor implements Runnable {
			private final Socket server;
			private final CarSourceAdapter adapter;
			private SourceSpec sourceSpec;

			public CarProcessor(final SourceSpec source, final Socket server,
					final CarSourceAdapter adapter) {
				this.server = server;
				this.adapter = adapter;
			}

			@Override
			public void run() {
				SocketChannel channel = this.server.getChannel();
				try {

					final ByteBuffer buffer = ByteBuffer
							.allocateDirect(64 * 1024);
					int nbytes = 0;
					int pos = 0;
					int size = 0;

					while (!Thread.currentThread().isInterrupted()) {
						while ((nbytes = channel.read(buffer)) > 0) {
							size += nbytes;
							for (int i = pos; i < size; i++) {
								if (i == LENGTH) {
									buffer.position(i + 1);
									buffer.flip();

									try {
										long timestamp = buffer.getLong();
										int id = buffer.getShort();
										int x = buffer.getShort();
										int y = buffer.getShort();
										float speed = buffer.getFloat();
										float angle = buffer.getFloat();
										this.adapter.transfer(sourceSpec,
												timestamp, new Object[] { id,
														x, y, speed, angle });

									} catch (final Exception e) {
										if (CarSourceAdapter.LOG
												.isDebugEnabled()) {
											CarSourceAdapter.LOG.debug(
													e.getMessage(), e);
											this.dumpPackage(buffer);
										}
									}
									buffer.limit(size);

									buffer.compact();
									size -= (i + 1);
									pos = 0;
									i = 0;
								}
							}
							pos++;
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
