package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.document.Advertisement;

import com.google.common.collect.ImmutableCollection;

public interface IAdvertisementManager {

	public void addAdvertisementListener(IAdvertisementListener listener);

	public void removeAdvertisementListener(IAdvertisementListener listener);

	public ImmutableCollection<Advertisement> getAdvertisements();
	public <T extends Advertisement> ImmutableCollection<T> getAdvertisements(Class<T> clazz);
	
	public void refreshAdvertisements();
}
