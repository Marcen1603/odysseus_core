package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class AdvertisementManager implements IAdvertisementManager, DiscoveryListener {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManager.class);
	private static final long DISCOVERY_INTERVAL_MILLIS = 5 * 1000;

	private static AdvertisementManager instance;

	private final List<IAdvertisementListener> listeners = Lists.newArrayList();

	private DiscoveryThread discoveryThread;

	// called by OSGi-DS
	public final void activate() {
		P2PNewPlugIn.getDiscoveryService().addDiscoveryListener(this);
		discoveryThread = new DiscoveryThread(DISCOVERY_INTERVAL_MILLIS);
		discoveryThread.start();

		instance = this;
		LOG.debug("Advertisement manager activated");
	}

	@Override
	public void addAdvertisementListener(IAdvertisementListener listener) {
		Preconditions.checkNotNull(listener, "Advertisement listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	// called by OSGi-DS
	public final void bindAdvertisementListener(IAdvertisementListener listener) {
		addAdvertisementListener(listener);

		LOG.debug("Bound advertisement listener {}", listener);
	}

	// called by OSGi-DS
	public final void deactivate() {
		discoveryThread.stopRunning();
		P2PNewPlugIn.getDiscoveryService().removeDiscoveryListener(this);

		instance = null;
		LOG.debug("Advertisement manager deactivated");
	}

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		process(event);
	}

	@Override
	public void removeAdvertisementListener(IAdvertisementListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	// called by OSGi-DS
	public final void unbindAdvertisementListener(IAdvertisementListener listener) {
		removeAdvertisementListener(listener);

		LOG.debug("Unbound advertisement listener {}", listener);
	}

	protected final void fireAdvertisementEvent(Advertisement advertisement) {
		synchronized (listeners) {
			for (final IAdvertisementListener entry : listeners) {
				try {
					if (entry.isSelected(advertisement)) {
						try {
							entry.advertisementOccured(this, advertisement);
						} catch (final Throwable t) {
							LOG.error("Exception during processing advertisement {}", advertisement, t);
						}
					}
				} catch (final Throwable t) {
					LOG.error("Exception during evaluating advertisement with selector: {}", advertisement, t);
				}
			}
		}
	}

	private void process(DiscoveryEvent event) {
		final DiscoveryResponseMsg response = event.getResponse();
		final Enumeration<Advertisement> advs = response.getAdvertisements();
		while (advs.hasMoreElements()) {
			final Advertisement adv = advs.nextElement();

			LOG.debug("Got advertisement of type {}", adv.getClass().getSimpleName());
			fireAdvertisementEvent(adv);
		}
	}

	public static AdvertisementManager getInstance() {
		return instance;
	}
}
