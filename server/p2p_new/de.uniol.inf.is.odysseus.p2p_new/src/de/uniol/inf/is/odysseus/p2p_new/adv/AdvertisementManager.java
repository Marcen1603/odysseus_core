package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.document.Advertisement;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class AdvertisementManager implements IAdvertisementManager {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManager.class);

	private static AdvertisementManager instance;

	private final List<IAdvertisementListener> listeners = Lists.newArrayList();
	
	private Map<ID, Advertisement> knownAdvertisements = Maps.newHashMap();
	private RepeatingJobThread discoveryThread;

	// called by OSGi-DS
	public final void activate() {
		discoveryThread = new AdvertisementDiscoverer() {
			@Override
			public void process(Enumeration<Advertisement> advertisements) {
				processAdvertisements(advertisements);
			}
		};
		
		discoveryThread.start();

		instance = this;
		LOG.debug("Advertisement manager activated");
	}

	// called by OSGi-DS
	public final void deactivate() {
		discoveryThread.stopRunning();

		instance = null;
		LOG.debug("Advertisement manager deactivated");
	}

	// called by OSGi-DS
	public final void bindAdvertisementListener(IAdvertisementListener listener) {
		addAdvertisementListener(listener);

		LOG.debug("Bound advertisement listener {}", listener);
	}

	// called by OSGi-DS
	public final void unbindAdvertisementListener(IAdvertisementListener listener) {
		removeAdvertisementListener(listener);

		LOG.debug("Unbound advertisement listener {}", listener);
	}
	
	@Override
	public void addAdvertisementListener(IAdvertisementListener listener) {
		Preconditions.checkNotNull(listener, "Advertisement listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}

		fireLateAdvertisementEvents(listener);
	}

	private void fireLateAdvertisementEvents(IAdvertisementListener listener) {
		synchronized (knownAdvertisements) {
			for (Advertisement adv : knownAdvertisements.values()) {
				try {
					listener.advertisementAdded(this, adv);
				} catch( Throwable t ) {
					LOG.error("Exception during processing advertisement add {}", adv, t);
				}
			}
		}
	}
	
	@Override
	public void removeAdvertisementListener(IAdvertisementListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public ImmutableCollection<Advertisement> getAdvertisements() {
		synchronized( knownAdvertisements ) {
			return ImmutableList.copyOf(knownAdvertisements.values());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Advertisement> ImmutableCollection<T> getAdvertisements(Class<T> clazz) {
		Preconditions.checkNotNull(clazz, "Advertisement class must not be null!");
		
		ImmutableCollection<Advertisement> advertisements = getAdvertisements();
		List<T> result = Lists.newArrayList();
		for( Advertisement adv : advertisements ) {
			if( adv.getClass().equals(clazz)) {
				result.add((T) adv);
			}
		}
		return ImmutableList.copyOf(result);
	}

	@Override
	public void refreshAdvertisements() {
		JxtaServicesProvider.getInstance().getRemoteAdvertisements();
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

	private void processAdvertisements(Enumeration<Advertisement> advs) {
		Map<ID, Advertisement> newAdvertisements = Maps.newHashMap();
		Map<ID, Advertisement> oldAdvertisements = null;
		synchronized( knownAdvertisements ) {
			oldAdvertisements = Maps.newHashMap(knownAdvertisements);
		}
		
		while (advs.hasMoreElements()) {
			final Advertisement adv = advs.nextElement();
			newAdvertisements.put(adv.getID(), adv);

			if( oldAdvertisements.containsKey(adv.getID())) {
				oldAdvertisements.remove(adv.getID());
			} else {
				fireAdvertisementAddEvent(adv);
			}
		}
		
		for( Advertisement removedAdvertisement : oldAdvertisements.values()) {
			fireAdvertisementRemoveEvent(removedAdvertisement);
		}
		
		synchronized( knownAdvertisements ) {
			knownAdvertisements = newAdvertisements;
		}
	}

	public static AdvertisementManager getInstance() {
		return instance;
	}
}
