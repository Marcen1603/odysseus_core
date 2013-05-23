package de.uniol.inf.is.odysseus.p2p_new.sources;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionary;

public class ViewAdvertisementReceiver implements IAdvertisementListener {

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof ViewAdvertisement ) {
			final ViewAdvertisement viewAdvertisement = (ViewAdvertisement) adv;
			
			if( P2PDictionary.getInstance().existsView(viewAdvertisement)) {
				return;
			}
			
			P2PDictionary.getInstance().addView(viewAdvertisement);
		}

	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		if (adv instanceof ViewAdvertisement) {
			final ViewAdvertisement viewAdvertisement = (ViewAdvertisement) adv;
			P2PDictionary.getInstance().removeView(viewAdvertisement);
			
//			DataDictionaryService.get().removeViewOrStream(((ViewAdvertisement)adv).getViewName(), SessionManagementService.getActiveSession());
		}		
	}
}
