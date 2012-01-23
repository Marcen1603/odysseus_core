package de.offis.salsa.wrapper.scooter.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.Current;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Properties;
import Ice.Util;
import TelemetriePublishSubscribe.Pose;
import TelemetriePublishSubscribe.TelemetriePublisherPrx;
import TelemetriePublishSubscribe.TelemetriePublisherPrxHelper;
import TelemetriePublishSubscribe.TelemetrieSubscriberPrxHelper;
import TelemetriePublishSubscribe._TelemetrieSubscriberDisp;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class ScooterSourceAdapter extends AbstractPushingSourceAdapter
		implements SourceAdapter {
	private static Logger LOG = LoggerFactory
			.getLogger(ScooterSourceAdapter.class);
	private final Map<SourceSpec, Thread> scooterThreads = new HashMap<SourceSpec, Thread>();
	private static final String SERVICE = "TelemetriePublisher";
	private static final String OWN_SERVICE = "TelemetrieSubscriberAdapter";
	private static final String PROTOCOL = "default";

	@Override
	public String getName() {
		return "Scooter";
	}

	@Override
	protected void doDestroy(SourceSpec source) {
		this.scooterThreads.get(source).interrupt();
		this.scooterThreads.remove(source);
	}

	@Override
	protected void doInit(SourceSpec source) {
		if (!scooterThreads.containsKey(source)) {
			final String host = source.getConfiguration().get("host")
					.toString();
			final int port = Integer.parseInt(source.getConfiguration()
					.get("port").toString());
			String username = "";
			String password = "";
			if (source.getConfiguration().containsKey("username")) {
				username = source.getConfiguration().get("username").toString();
			}
			if (source.getConfiguration().containsKey("password")) {
				password = source.getConfiguration().get("password").toString();
			}
			try {
				final ICEConnection connection = new ICEConnection(source,
						host, port, username, password, this);
				final Thread scooterThread = new Thread(connection);
				this.scooterThreads.put(source, scooterThread);
				scooterThread.start();
			} catch (final Exception e) {
				ScooterSourceAdapter.LOG.error(e.getMessage(), e);
			}
		}
	}

	private class ICEConnection extends _TelemetrieSubscriberDisp implements
			Runnable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1313572970926873682L;
		private final SourceSpec source;
		private final ScooterSourceAdapter adapter;
		private final int maxMessageSize = 10240;
		private final String proxy;

		public ICEConnection(final SourceSpec source, final String host,
				final int port, final String username, final String password,
				final ScooterSourceAdapter adapter) {
			this.source = source;
			this.adapter = adapter;
			this.proxy = SERVICE + ":" + PROTOCOL + " -h " + host + " -p "
					+ port;
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
							.createObjectAdapterWithEndpoints(
									OWN_SERVICE,
									PROTOCOL
											+ " -h 127.0.0.1 -p "
											+ (1024 + (int) (Math.random() * 1000)));
					ScooterSourceAdapter.LOG.debug(String.format(
							"Connecting to ICE endpoint %s",
							objectAdapter.toString()));
					ObjectPrx subscriberConnectionObject = objectAdapter.add(
							this, communicator
									.stringToIdentity("TelemetrieSubscriber"));
					objectAdapter.activate();
					final ObjectPrx base = communicator
							.stringToProxy(this.proxy);
					if (base != null) {
						TelemetriePublisherPrx publisherConnectionObject = TelemetriePublisherPrxHelper
								.checkedCast(base);
						publisherConnectionObject.ice_ping();
						publisherConnectionObject
								.subscribe(TelemetrieSubscriberPrxHelper
										.checkedCast(subscriberConnectionObject));
						while (!Thread.currentThread().isInterrupted()) {
							Thread.sleep((long) 20.0);
						}
					}
				} catch (final Exception e) {
					ScooterSourceAdapter.LOG.error(e.getMessage(), e);
				} finally {
					try {
						if (objectAdapter != null) {
							objectAdapter.deactivate();
							objectAdapter.destroy();
							objectAdapter = null;
						}
					} catch (final Exception e) {
						ScooterSourceAdapter.LOG.error(e.getMessage(), e);
					}
					try {
						if (communicator != null) {
							communicator.shutdown();
							communicator.destroy();
							communicator = null;
						}
					} catch (final Exception e) {
						ScooterSourceAdapter.LOG.error(e.getMessage(), e);
					}
				}
				ScooterSourceAdapter.LOG.debug("ICE endpoint disconnected");
				try {
					Thread.sleep((long) 1000.0);
				} catch (InterruptedException e) {
					ScooterSourceAdapter.LOG.error(e.getMessage(), e);
				}
			}
		}

		@Override
		public void _notify(Pose p, Current __current) {
			this.adapter.transfer(this.source, System.currentTimeMillis(),
					new Object[] { p.X, p.Y, p.orientation });
		}

	}
}
