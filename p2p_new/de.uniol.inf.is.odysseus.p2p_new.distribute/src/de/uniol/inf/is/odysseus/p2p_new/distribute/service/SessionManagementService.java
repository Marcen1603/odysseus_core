package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Singleton class for the single, static referenced {@link ISessionManagement}. <br />
 * The referenced {@link ISessionManagement} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class SessionManagementService {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SessionManagementService.class);
	
	/**
	 * The referenced {@link ISessionManagement}.
	 * @see #bindSessionManagement(ISessionManagement)
	 * @see #unbindSessionManagement(ISessionManagement)
	 */
	private static ISessionManagement sessionManagement;
	
	/**
	 * The active {@link ISession}.
	 */
	private static ISession activeSession;

	/**
	 * Binds the referenced {@link ISessionManagement}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindSessionManagement(ISessionManagement)
	 * @param sm An instance of an {@link ISessionManagement} implementation.
	 */
	public final void bindSessionManagement(ISessionManagement sm) {
		
		sessionManagement = sm;
		activeSession = sessionManagement.loginSuperUser(null, "");
		LOG.debug("Bound Session Management {}", sm);
		
	}

	/**
	 * Unbinds an referenced {@link ISessionManagement}, if <code>sm</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindSessionManagement(ISessionManagement)
	 * @param sm An instance of an {@link ISessionManagement} implementation.
	 */
	public final void unbindSessionManagement(ISessionManagement sm) {
		
		if(sm == sessionManagement) {
			
			sessionManagement = null;
			activeSession = null;
			LOG.debug("Unbound Session Management");
			
		}
		
	}

	/**
	 * Returns the active {@link ISession}.
	 */
	public static ISession getActiveSession() {
		
		return activeSession;
		
	}
	
	/**
	 * Determines, if a referenced {@link ISessionManagement} is bound.
	 * @see #bindJxtaServicesProvider(ISessionManagement)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}

	/**
	 * Returns the referenced {@link ISessionManagement}.
	 */
	public static ISessionManagement get() {
		
		return sessionManagement;
		
	}
	
}