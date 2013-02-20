package de.uniol.inf.is.odysseus.p2p_new.adv;

import com.google.common.base.Preconditions;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementSelector;

public class AdvertisementClassSelector implements IAdvertisementSelector {

	private final Class<? extends Advertisement> classToSelect;
	
	public AdvertisementClassSelector(Class<? extends Advertisement> classToSelect ) {
		this.classToSelect = Preconditions.checkNotNull(classToSelect, "Class to select advertisements must not be null!");
	}
	
	@Override
	public boolean isSelected(Advertisement advertisement) {
		return (classToSelect.isInstance(advertisement));
	}

}
