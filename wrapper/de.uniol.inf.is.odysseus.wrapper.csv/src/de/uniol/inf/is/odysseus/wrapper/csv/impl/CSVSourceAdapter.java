package de.uniol.inf.is.odysseus.wrapper.csv.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class CSVSourceAdapter extends AbstractPushingSourceAdapter implements
		SourceAdapter {
	private static Logger LOG = LoggerFactory.getLogger(CSVSourceAdapter.class);

	private final Map<SourceSpec, Thread> serverThreads = new HashMap<SourceSpec, Thread>();

	@Override
	public String getName() {
		return "CSV";
	}

	@Override
	protected void doInit(final SourceSpec source) {
		int port = 4444;
		if (source.getConfiguration().containsKey("port")) {
			port = Integer.parseInt(source.getConfiguration().get("port")
					.toString());
		}
		final CSVServer server = new CSVServer(source, this, port);
		final Thread serverThread = new Thread(server);

		this.serverThreads.put(source, serverThread);
		serverThread.start();

	}

	@Override
	protected void doDestroy(final SourceSpec source) {
		this.serverThreads.get(source).interrupt();
		this.serverThreads.remove(source);
	}

	class CSVServer implements Runnable {
		private ServerSocket serverSocket = null;
		private SourceSpec source;
		private CSVSourceAdapter adapter;

		public CSVServer(final SourceSpec source,
				final CSVSourceAdapter adapter, final int port) {
			try {
				this.serverSocket = new ServerSocket(port);
				this.source = source;
				this.adapter = adapter;
			} catch (final IOException e) {
				CSVSourceAdapter.LOG.error(e.getMessage(), e);
			}
		}

		@Override
		public void run() {
			final List<Thread> processingThreads = new ArrayList<Thread>();
			while (!Thread.currentThread().isInterrupted()) {
				Socket socket;
				try {
					socket = this.serverSocket.accept();
					final CSVProcessor processor = new CSVProcessor(
							this.source, socket, this.adapter);
					final Thread processingThread = new Thread(processor);
					processingThreads.add(processingThread);
					processingThread.start();
				} catch (final IOException e) {
					CSVSourceAdapter.LOG.error(e.getMessage(), e);
				}
			}
			for (final Thread processingThread : processingThreads) {
				processingThread.interrupt();
			}
		}

		class CSVProcessor implements Runnable {
			private final Socket server;
			private final CSVSourceAdapter adapter;
			private SourceSpec source;

			public CSVProcessor(final SourceSpec source, final Socket server,
					final CSVSourceAdapter adapter) {
				this.server = server;
				this.adapter = adapter;
				this.source = source;
			}

			@Override
			public void run() {
				LineNumberReader reader;
				try {
					reader = new LineNumberReader(new InputStreamReader(
							this.server.getInputStream()));
					String line = reader.readLine();
					if (line != null) {
						while (((line = reader.readLine()) != null)
								&& (!Thread.currentThread().isInterrupted())) {
							final String[] values = line.split(",");
								
							/*
							 * @FIXME
							 * System.currentTimeMillis() replace optional by incoming timestamp
							 * 
							 */
							 this.adapter.transfer(this.source, System.currentTimeMillis(), values);

						}
					}
				} catch (final IOException e) {
					CSVSourceAdapter.LOG.error(e.getMessage(), e);
				}
			}
		}

	}

}
