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
			final int ownPort = Integer.parseInt(source.getConfiguration()
					.get("ownPort").toString());
			try {
				final ICEConnection connection = new ICEConnection(source,
						host, port, "", "", ownPort, this);
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
		private Communicator communicator;
		private ObjectAdapter objectAdapter;
		private TelemetriePublisherPrx publisherConnectionObject;
		private final int maxMessageSize = 10240;
		private final String proxy;
		private ObjectPrx subscriberConnectionObject;

		public ICEConnection(final SourceSpec source, final String host,
				final int port, final String username, final String password,
				final int ownPort, final ScooterSourceAdapter adapter) {
			this.source = source;
			this.adapter = adapter;
			this.proxy = SERVICE + ":" + PROTOCOL + " -h " + host + " -p "
					+ port;
			try {
				ScooterSourceAdapter.LOG.info("Try proxy: {}", this.proxy);
				final Properties props = Util.createProperties();
				props.setProperty("Ice.MessageSizeMax",
						Integer.toString(this.maxMessageSize));
				final InitializationData initializationData = new InitializationData();
				initializationData.properties = props;
				this.communicator = Util.initialize(initializationData);

				this.objectAdapter = this.communicator
						.createObjectAdapterWithEndpoints(OWN_SERVICE, PROTOCOL
								+ " -h 127.0.0.1 -p " + ownPort);
				subscriberConnectionObject = this.objectAdapter.add(this, this.communicator
						.stringToIdentity("TelemetrieSubscriber"));
			} catch (final Exception e) {
				ScooterSourceAdapter.LOG.error(e.getMessage(), e);
			}

		}

		@Override
		public void run() {

			try {
				this.objectAdapter.activate();
				final ObjectPrx base = this.communicator
						.stringToProxy(this.proxy);
				if (base != null) {
					this.publisherConnectionObject = TelemetriePublisherPrxHelper
							.checkedCast(base);
					this.publisherConnectionObject.ice_ping();
					this.publisherConnectionObject.subscribe(TelemetrieSubscriberPrxHelper.checkedCast(subscriberConnectionObject));
					while (!Thread.currentThread().isInterrupted()) {
						Thread.sleep((long) 20.0);
					}
				}
				this.objectAdapter.deactivate();
			} catch (final Exception e) {
				ScooterSourceAdapter.LOG.error(e.getMessage(), e);
			} finally {
				try {
					if (this.objectAdapter != null) {
						this.objectAdapter.deactivate();
					}
				} catch (final Exception e) {
					ScooterSourceAdapter.LOG.error(e.getMessage(), e);
				}
				try {
					if (this.communicator != null) {
						this.communicator.destroy();
					}
				} catch (final Exception e) {
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
