package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;

/**
 * Singleton class for the single, static referenced {@link IJxtaServicesProvider}. <br />
 * The referenced {@link IJxtaServicesProvider} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class JxtaServicesProviderService {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(JxtaServicesProviderService.class);
	
	/**
	 * The referenced {@link IJxtaServicesProvider}.
	 * @see #bindJxtaServicesProvider(IJxtaServicesProvider)
	 * @see #unbindJxtaServicesProvider(IJxtaServicesProvider)
	 */
	private static IJxtaServicesProvider provider;
	
	/**
	 * Binds the referenced {@link IJxtaServicesProvider}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindJxtaServicesProvider(IJxtaServicesProvider)
	 * @param prov An instance of an {@link IJxtaServicesProvider} implementation.
	 */
	public void bindJxtaServicesProvider(IJxtaServicesProvider prov) {
		
		provider = prov;		
		LOG.debug("JxtaServicesProvider bound {}", prov);
		
	}
	
	/**
	 * Unbinds an referenced {@link IJxtaServicesProvider}, if <code>prov</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindJxtaServicesProvider(IJxtaServicesProvider)
	 * @param prov An instance of an {@link IJxtaServicesProvider} implementation.
	 */
	public void unbindJxtaServicesProvider(IJxtaServicesProvider prov) {
		
		if(provider == prov) {
			
			provider = null;		
			LOG.debug("JxtaServicesProvider unbound {}", prov);
			
		}
		
	}
	
	/**
	 * Determines, if a referenced {@link IJxtaServicesProvider} is bound.
	 * @see #bindJxtaServicesProvider(IJxtaServicesProvider)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}
	

	/**
	 * Returns the referenced {@link IJxtaServicesProvider}.
	 */
	public static IJxtaServicesProvider get() {
		
		return provider;
		
	}
	
}