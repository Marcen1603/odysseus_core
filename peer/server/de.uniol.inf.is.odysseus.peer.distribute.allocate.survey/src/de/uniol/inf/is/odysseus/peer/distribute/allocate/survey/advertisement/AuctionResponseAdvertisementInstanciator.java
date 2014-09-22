package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement;

import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;


public final class AuctionResponseAdvertisementInstanciator implements AdvertisementFactory.Instantiator {
	
	/**
	 * Returns the identifying type of this Advertisement.
	 * 
	 * @return String the type of advertisement
	 */
	@Override
	public String getAdvertisementType() {
		
		return AuctionResponseAdvertisement.getAdvertisementType();
		
	}

	/**
	 * Constructs an instance of <CODE>Advertisement</CODE> matching the type
	 * specified by the <CODE>advertisementType</CODE> parameter.
	 * 
	 * @return The instance of <CODE>Advertisement</CODE> or null if it could
	 *         not be created.
	 */
	@Override
	public AuctionResponseAdvertisement newInstance() {
		
		return new AuctionResponseAdvertisement();
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public AuctionResponseAdvertisement newInstance(Element root) {
		
		return new AuctionResponseAdvertisement(root);
		
	}
	
}