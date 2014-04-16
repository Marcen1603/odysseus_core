package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.provider.JxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public abstract class AdvertisementCollector<A extends Advertisement, R extends Advertisement> extends RepeatingJobThread {

	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementCollector.class);
	private static final int UPDATE_INTERVAL_MILLIS = 3500;
	
	private final List<A> collectedAdvertisements = Lists.newArrayList();
	
	public AdvertisementCollector() {
		super(UPDATE_INTERVAL_MILLIS, "Advertisement collector");
	}
	
	public void add( A advertisement ) {
		Preconditions.checkNotNull(advertisement, "Advertisement for collector must not be null!");
		
		synchronized(collectedAdvertisements ) {
			collectedAdvertisements.add(advertisement);
		}
	}
	
	@Override
	public void doJob() {
		R resultAdvertisement = null;
		
		synchronized(collectedAdvertisements) {
			if( collectedAdvertisements.size() == 1 ) {
				tryPublish(collectedAdvertisements.remove(0));
			} else if( !collectedAdvertisements.isEmpty() ) {
				resultAdvertisement = merge( Lists.newArrayList(collectedAdvertisements));
				collectedAdvertisements.clear();
			}
		}
		
		if( resultAdvertisement != null ) {
			tryPublish(resultAdvertisement);
		}
	}

	private void tryPublish(Advertisement adv) {
		try {
			JxtaServicesProvider.getInstance().publish(adv);
			JxtaServicesProvider.getInstance().remotePublish(adv);
		} catch (IOException e) {
			LOG.error("Could not publish result advertisement of collector", e);
		}
	}
	
	public abstract R merge(Collection<A> advertisements);
}
