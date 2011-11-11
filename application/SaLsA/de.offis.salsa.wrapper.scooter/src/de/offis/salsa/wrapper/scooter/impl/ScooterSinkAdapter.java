package de.offis.salsa.wrapper.scooter.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import GridPublishSubscribe.GridStruct;
import GridPublishSubscribe.GridSubscriberPrx;
import GridPublishSubscribe._GridPublisherDisp;
import Ice.Communicator;
import Ice.Current;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Properties;
import Ice.Util;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractSinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class ScooterSinkAdapter extends AbstractSinkAdapter implements
		SinkAdapter {
	private static Logger LOG = LoggerFactory
			.getLogger(ScooterSinkAdapter.class);
	private final static double FREE = 0.0;
	private final static double UNKNOWN = -1.0;
	private final static double OBSTACLE = 1.0;
	private static final String SERVICE = "GridAdapter";
	private static final String OWN_SERVICE = "GridPublisher";
	private static final String PROTOCOL = "default";
	private final Map<SinkSpec, Thread> scooterThreads = new HashMap<SinkSpec, Thread>();
	private final BlockingQueue<Object[]> messageQueue = new LinkedBlockingQueue<Object[]>();

	@Override
	public String getName() {
		return "Scooter";
	}

	@Override
	public void transfer(SinkSpec sink, long timestamp, Object[] data) {
		this.messageQueue.offer(new Object[] { timestamp, data });

	}

	@Override
	protected void destroy(SinkSpec sink) {
		this.scooterThreads.get(sink).interrupt();
		this.scooterThreads.remove(sink);

	}

	@Override
	protected void init(SinkSpec sink) {
		if (!scooterThreads.containsKey(sink)) {
			final String host = sink.getConfiguration().get("host").toString();
			final int port = Integer.parseInt(sink.getConfiguration()
					.get("port").toString());
			final int ownPort = Integer.parseInt(sink.getConfiguration()
					.get("ownPort").toString());
			try {
				final ICEConnection connection = new ICEConnection(sink, host,
						port, "", "", ownPort, this);
				final Thread iceThread = new Thread(connection);
				this.scooterThreads.put(sink, iceThread);
				iceThread.start();
			} catch (final Exception e) {
				ScooterSinkAdapter.LOG.error(e.getMessage(), e);
			}
		}
	}

	private class ICEConnection extends _GridPublisherDisp implements Runnable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7206385627240128444L;
		private Communicator communicator;
		private ObjectAdapter objectAdapter;
		private GridSubscriberPrx connectionObject;
		private final int maxMessageSize = 10240;
		private final String proxy;
		private final List<GridSubscriberPrx> listeners = new CopyOnWriteArrayList<GridSubscriberPrx>();
		private ObjectPrx publisherConnectionObject;

		public ICEConnection(final SinkSpec sink, final String host,
				final int port, final String username, final String password,
				final int ownPort, final ScooterSinkAdapter adapter) {
			this.proxy = SERVICE + ":" + PROTOCOL + " -h " + host + " -p "
					+ port;
			try {
				ScooterSinkAdapter.LOG.info("Provide proxy: {}", this.proxy);
				final Properties props = Util.createProperties();
				props.setProperty("Ice.MessageSizeMax",
						Integer.toString(this.maxMessageSize));
				final InitializationData initializationData = new InitializationData();

				initializationData.properties = props;
				this.communicator = Util.initialize(initializationData);

				this.objectAdapter = this.communicator
						.createObjectAdapterWithEndpoints(OWN_SERVICE, PROTOCOL
								+ " -h 127.0.0.1 -p " + ownPort);
				publisherConnectionObject = this.objectAdapter.add(this,
						this.communicator.stringToIdentity("GridPublisher"));

			} catch (final Exception e) {
				ScooterSinkAdapter.LOG.error(e.getMessage(), e);
			}
		}

		@Override
		public void run() {

			try {
				this.objectAdapter.activate();
				while (!Thread.currentThread().isInterrupted()) {
					final Object[] data = ScooterSinkAdapter.this.messageQueue
							.take();
					Grid2D grid = (Grid2D) data[1];

					GridStruct iceGrid = new GridStruct();
					iceGrid.cellsize = grid.cellsize;
					iceGrid.width = grid.grid.length;
					iceGrid.height = grid.grid[0].length;
					iceGrid.data = new byte[iceGrid.width * iceGrid.height];
					for (int l = 0; l < grid.grid.length; l++) {
						for (int w = 0; w < grid.grid[l].length; w++) {
							if (grid.get(l, w) == FREE) {
								iceGrid.data[l * w] = (byte) 0x00;
							} else if (grid.get(l, w) < FREE) {
								iceGrid.data[l * w] = (byte) 0xFF;
							} else {
								iceGrid.data[l * w] = (byte) 0x64;
							}
						}
					}
					for (GridSubscriberPrx listener : listeners) {
						listener._notify((Long) data[0], grid.origin.x
								* grid.cellsize, grid.origin.x * grid.cellsize,
								iceGrid);
					}
				}
				this.objectAdapter.deactivate();
			} catch (final Exception e) {
				ScooterSinkAdapter.LOG.error(e.getMessage(), e);
			} finally {
				try {
					if (this.objectAdapter != null) {
						this.objectAdapter.deactivate();
					}
				} catch (final Exception e) {
					ScooterSinkAdapter.LOG.error(e.getMessage(), e);
				}
				try {
					if (this.communicator != null) {
						this.communicator.destroy();
					}
				} catch (final Exception e) {
					ScooterSinkAdapter.LOG.error(e.getMessage(), e);
				}
			}
		}

		@Override
		public void subscribe(GridSubscriberPrx sub, Current __current) {
			this.listeners.add(sub);

		}
	}
}
