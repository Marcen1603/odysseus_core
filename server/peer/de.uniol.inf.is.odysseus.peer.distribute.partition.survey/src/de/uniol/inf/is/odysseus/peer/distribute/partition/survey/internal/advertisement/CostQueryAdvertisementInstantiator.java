package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.internal.advertisement;

import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Element;


public final class CostQueryAdvertisementInstantiator implements AdvertisementFactory.Instantiator {
	
	/**
	 * Returns the identifying type of this Advertisement.
	 * 
	 * @return String the type of advertisement
	 */
	@Override
	public String getAdvertisementType() {
		
		return CostQueryAdvertisement.getAdvertisementType();
		
	}

	/**
	 * Constructs an instance of <CODE>Advertisement</CODE> matching the type
	 * specified by the <CODE>advertisementType</CODE> parameter.
	 * 
	 * @return The instance of <CODE>Advertisement</CODE> or null if it could
	 *         not be created.
	 */
	@Override
	public CostQueryAdvertisement newInstance() {
		
		return new CostQueryAdvertisement();
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CostQueryAdvertisement newInstance(Element root) {
		
		return new CostQueryAdvertisement(root);
		
	}
	
}