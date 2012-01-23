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
import Ice.Properties;
import Ice.Util;
import de.uniol.inf.is.odysseus.salsa.model.Grid;
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
	private static final String SERVICE = "GridPublisher";
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
			String username = "";
			String password = "";
			if (sink.getConfiguration().containsKey("username")) {
				username = sink.getConfiguration().get("username").toString();
			}
			if (sink.getConfiguration().containsKey("password")) {
				password = sink.getConfiguration().get("password").toString();
			}

			try {
				final ICEConnection connection = new ICEConnection(sink, host,
						port, username, password, this);
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
		private final int maxMessageSize = 10240;
		private final List<GridSubscriberPrx> listeners = new CopyOnWriteArrayList<GridSubscriberPrx>();
		private int port;

		public ICEConnection(final SinkSpec sink, final String host,
				final int port, final String username, final String password,
				final ScooterSinkAdapter adapter) {
			this.port = port;
		}

		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				final Properties props = Util.createProperties();
				props.setProperty("Ice.MessageSizeMax",
						Integer.toString(this.maxMessageSize));
				final InitializationData initializationData = new InitializationData();

				initializationData.properties = props;
				Communicator communicator = null;
				ObjectAdapter objectAdapter = null;
				try {

					communicator = Util.initialize(initializationData);

					objectAdapter = communicator
							.createObjectAdapterWithEndpoints(SERVICE, PROTOCOL
									+ " -h 127.0.0.1 -p " + port);
					ScooterSinkAdapter.LOG.debug(String.format(
							"Creating ICE endpoint %s",
							objectAdapter.toString()));
					objectAdapter.add(this,
							communicator.stringToIdentity("GridPublisher"));
					objectAdapter.activate();
					while (!Thread.currentThread().isInterrupted()) {
						if (ScooterSinkAdapter.this.messageQueue.size() > 5) {
							ScooterSinkAdapter.LOG
									.debug(String
											.format("ICE queue overflow with %s messages ... clearing",
													ScooterSinkAdapter.this.messageQueue
															.size()));
							ScooterSinkAdapter.this.messageQueue.clear();
						}
						final Object[] data = ScooterSinkAdapter.this.messageQueue
								.take();
						Grid grid = (Grid) ((Object[]) data[1])[0];

						GridStruct iceGrid = new GridStruct();
						iceGrid.timestamp = (Long) data[0];
						iceGrid.x = grid.origin.x * grid.cellsize;
						iceGrid.y = grid.origin.y * grid.cellsize;
						iceGrid.cellsize = grid.cellsize;
						iceGrid.width = grid.width;
						iceGrid.height = grid.depth;
						iceGrid.data = new byte[iceGrid.width * iceGrid.height];
						for (int l = 0; l < grid.width; l++) {
							for (int w = 0; w < grid.depth; w++) {
								int index = l * grid.depth + w;
								iceGrid.data[index] = grid.get(l, w);
							}
						}
						try {
							for (GridSubscriberPrx listener : listeners) {
								listener._notify(iceGrid);
							}
						} catch (final Exception e) {
							ScooterSinkAdapter.LOG.error(e.getMessage(), e);
						}
					}
				} catch (final Exception e) {
					ScooterSinkAdapter.LOG.error(e.getMessage(), e);
				} finally {
					try {
						if (objectAdapter != null) {
							objectAdapter.deactivate();
							objectAdapter.destroy();
							objectAdapter = null;
						}
					} catch (final Exception e) {
						ScooterSinkAdapter.LOG.error(e.getMessage(), e);
					}
					try {
						if (communicator != null) {
							communicator.shutdown();
							communicator.destroy();
							communicator = null;
						}
					} catch (final Exception e) {
						ScooterSinkAdapter.LOG.error(e.getMessage(), e);
					}
				}
				ScooterSinkAdapter.LOG.debug("ICE endpoint closed");
				try {
					Thread.sleep((long) 1000.0);
				} catch (InterruptedException e) {
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
