package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class AdvertisementManager implements IAdvertisementManager {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManager.class);
	private static final long DISCOVERY_INTERVAL_MILLIS = 3 * 1000;
	private static final int REMOTE_DISCOVERY_COUNT = 5;

	private static AdvertisementManager instance;

	private final List<IAdvertisementListener> listeners = Lists.newArrayList();
	
	private List<Advertisement> knownAdvertisements = Lists.newArrayList();
	private RepeatingJobThread discoveryThread;

	// called by OSGi-DS
	public final void activate() {
		discoveryThread = new RepeatingJobThread(DISCOVERY_INTERVAL_MILLIS, "Advertisement discovery thread") {
			
			private int remoteCounter = 0;
			
			@Override
			public void doJob() {
				if (JxtaServicesProvider.isActivated()) {
					
					if( remoteCounter == 0 ) {
						JxtaServicesProvider.getInstance().getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV, null, null, 99);
					}
					remoteCounter = (remoteCounter + 1 ) % REMOTE_DISCOVERY_COUNT;
					
					try {
						Enumeration<Advertisement> localAdvertisements = JxtaServicesProvider.getInstance().getDiscoveryService().getLocalAdvertisements(DiscoveryService.ADV, null, null);
						process(localAdvertisements);
						
					} catch (IOException e) {
						LOG.error("Could not get local advertisements", e);
					}
				}
			}
		};
		
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

		synchronized (knownAdvertisements) {
			for (Advertisement adv : knownAdvertisements) {
				try {
					listener.advertisementAdded(this, adv);
				} catch( Throwable t ) {
					LOG.error("Exception during processing advertisement add {}", adv, t);
				}
			}
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

		instance = null;
		LOG.debug("Advertisement manager deactivated");
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

	protected final void fireAdvertisementAddEvent(Advertisement advertisement) {
		synchronized (listeners) {
			for (final IAdvertisementListener entry : listeners) {
				try {
					entry.advertisementAdded(this, advertisement);
				} catch (final Throwable t) {
					LOG.error("Exception during processing advertisement add {}", advertisement, t);
				}
			}
		}
	}
	
	protected final void fireAdvertisementRemoveEvent(Advertisement advertisement) {
		synchronized (listeners) {
			for (final IAdvertisementListener entry : listeners) {
				try {
					entry.advertisementRemoved(this, advertisement);
				} catch (final Throwable t) {
					LOG.error("Exception during processing advertisement remove {}", advertisement, t);
				}
			}
		}
	}

	private void process(Enumeration<Advertisement> advs) {
		List<Advertisement> newAdvertisements = Lists.newArrayList();
		List<Advertisement> oldAdvertisements = null;
		synchronized( knownAdvertisements ) {
			oldAdvertisements = Lists.newArrayList(knownAdvertisements);
		}
		
		while (advs.hasMoreElements()) {
			final Advertisement adv = advs.nextElement();
			newAdvertisements.add(adv);

			if( oldAdvertisements.contains(adv)) {
				oldAdvertisements.remove(adv);
			} else {
				fireAdvertisementAddEvent(adv);
			}
		}
		
		for( Advertisement removedAdvertisement : oldAdvertisements) {
			fireAdvertisementRemoveEvent(removedAdvertisement);
		}
		
		synchronized( knownAdvertisements ) {
			knownAdvertisements = newAdvertisements;
		}
	}

	public static AdvertisementManager getInstance() {
		return instance;
	}

	@Override
	public ImmutableCollection<Advertisement> getAdvertisements() {
		synchronized( knownAdvertisements ) {
			return ImmutableList.copyOf(knownAdvertisements);
		}
	}
}
