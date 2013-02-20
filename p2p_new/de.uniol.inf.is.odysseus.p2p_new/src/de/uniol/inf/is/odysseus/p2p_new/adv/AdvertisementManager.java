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
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementSelector;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class AdvertisementManager implements IAdvertisementManager, DiscoveryListener {

	private static class Entry {

		public final IAdvertisementListener listener;
		public final IAdvertisementSelector selector;

		public Entry(IAdvertisementListener listener, IAdvertisementSelector selector) {
			this.listener = Preconditions.checkNotNull(listener, "Advertisement listener must not be null!");
			this.selector = Preconditions.checkNotNull(selector, "Advertisement selector must not be null!");
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Entry)) {
				return false;
			}
			Entry other = (Entry) obj;

			return other.listener == listener;
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManager.class);
	private static AdvertisementManager instance;

	private final List<Entry> listenerEntries = Lists.newArrayList();

	// called by OSGi-DS
	public final void activate() {
		P2PNewPlugIn.getDiscoveryService().addDiscoveryListener(this);

		instance = this;
		LOG.debug("Advertisement manager activated");
	}

	// called by OSGi-DS
	public final void deactivate() {
		P2PNewPlugIn.getDiscoveryService().removeDiscoveryListener(this);

		instance = null;
		LOG.debug("Advertisement manager deactivated");
	}

	@Override
	public void addAdvertisementListener(IAdvertisementListener listener) {
		addAdvertisementListener(listener, AdvertisementAllSelector.INSTANCE);
	}

	@Override
	public void addAdvertisementListener(IAdvertisementListener listener, Class<? extends Advertisement> listenFor) {
		addAdvertisementListener(listener, new AdvertisementClassSelector(listenFor));
	}

	@Override
	public void addAdvertisementListener(IAdvertisementListener listener, IAdvertisementSelector selector) {
		Preconditions.checkNotNull(listener, "Advertisement listener must not be null!");
		Preconditions.checkNotNull(selector, "Advertisement selector must not be null!");

		synchronized (listenerEntries) {
			listenerEntries.add(new Entry(listener, selector));
		}
	}

	@Override
	public void removeAdvertisementListener(IAdvertisementListener listener) {
		synchronized (listenerEntries) {
			listenerEntries.remove(listener);
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		process(event);
	}

	public static AdvertisementManager getInstance() {
		return instance;
	}

	protected final void fireAdvertisementEvent(Advertisement advertisement) {
		synchronized (listenerEntries) {
			for (Entry entry : listenerEntries) {
				try {
					if (entry.selector.isSelected(advertisement)) {
						try {
							entry.listener.advertisementOccured(this, advertisement);
						} catch (Throwable t) {
							LOG.error("Exception during processing advertisement", t);
						}
					}
				} catch (Throwable t) {
					LOG.error("Exception during evaluating advertisement with selector", t);
				}
			}
		}
	}

	private void process(DiscoveryEvent event) {
		DiscoveryResponseMsg response = event.getResponse();
		Enumeration<Advertisement> advs = response.getAdvertisements();
		while (advs.hasMoreElements()) {
			Advertisement adv = advs.nextElement();
			fireAdvertisementEvent(adv);
		}
	}
}
