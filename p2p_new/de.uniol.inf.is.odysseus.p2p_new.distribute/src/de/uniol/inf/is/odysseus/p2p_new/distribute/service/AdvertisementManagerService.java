package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;

/**
 * Singleton class for the single, static referenced {@link IAdvertisementManager}. <br />
 * The referenced {@link IAdvertisementManager} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class AdvertisementManagerService {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AdvertisementManagerService.class);
	
	/**
	 * The referenced {@link IAdvertisementManager}.
	 * @see #bindAdvertisementManager(IAdvertisementManager)
	 * @see #unbindAdvertisementManager(IAdvertisementManager)
	 */
	private static IAdvertisementManager manager;
	
	/**
	 * Binds the referenced {@link IAdvertisementManager}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindAdvertisementManager(IAdvertisementManager)
	 * @param mng An instance of an {@link IAdvertisementManager} implementation.
	 */
	public void bindAdvertisementManager(IAdvertisementManager mng) {
		
		manager = mng;		
		LOG.debug("AdvertisementManager bound {}", mng);
		
	}
	
	/**
	 * Unbinds an referenced {@link IAdvertisementManager}, if <code>mng</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindAdvertisementManager(IAdvertisementManager)
	 * @param mng An instance of an {@link IAdvertisementManager} implementation.
	 */
	public void unbindAdvertisementManager(IAdvertisementManager mng) {
		
		if(manager == mng) {
			
			manager = null;			
			LOG.debug("AdvertisementManager unbound {}", mng);
			
		}
		
	}
	
	/**
	 * Returns the referenced {@link IAdvertisementManager}.
	 */
	public static IAdvertisementManager get() {
		
		return manager;
		
	}
	
	/**
	 * Determines, if a referenced {@link IAdvertisementManager} is bound.
	 * @see #bindAdvertisementManager(IAdvertisementManager)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}
	
}