package de.uniol.inf.is.odysseus.p2p_new.adv;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;

public final class SourceAdvertisementInstantiator implements AdvertisementFactory.Instantiator {
	/**
	 * Returns the identifying type of this Advertisement.
	 * 
	 * @return String the type of advertisement
	 */
	@Override
	public String getAdvertisementType() {
		return SourceAdvertisement.getAdvertisementType();
	}

	/**
	 * Constructs an instance of <CODE>Advertisement</CODE> matching the
	 * type specified by the <CODE>advertisementType</CODE> parameter.
	 * 
	 * @return The instance of <CODE>Advertisement</CODE> or null if it
	 *         could not be created.
	 */
	@Override
	public Advertisement newInstance() {
		return new SourceAdvertisement();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Advertisement newInstance(Element root) {
		return new SourceAdvertisement(root);
	}
}
