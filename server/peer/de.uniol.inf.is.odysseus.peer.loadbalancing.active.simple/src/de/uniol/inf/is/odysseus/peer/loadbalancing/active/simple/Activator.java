package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

/**
 * The {@link BundleActivator} for the bundle
 * <code>de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple</code>.
 * 
 * @author Michael Brand
 */
public class Activator implements BundleActivator {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

	/**
	 * The {@link IExecutor}.
	 */
	private static IExecutor cExecutor;

	/**
	 * The {@link ISession}.
	 */
	private static ISession cSession;

	/**
	 * The {@link IPeerResourceUsageManager}.
	 */
	private static IPeerResourceUsageManager cResourceManager;

	/**
	 * The {@link IP2PDictionary}.
	 */
	private static IP2PDictionary cPeerDictionary;

	/**
	 * The {@link IP2PNetworkManager}.
	 */
	private static IP2PNetworkManager cNetworkmanager;

	/**
	 * Gets the executor.
	 * 
	 * @return The {@link IExecutor}, if one is bound.
	 */
	public static Optional<IExecutor> getExecutor() {

		return Optional.fromNullable(Activator.cExecutor);

	}

	/**
	 * Binds an executor. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param executor
	 *            The implementation of {@link IExecutor} to be bound.
	 */
	public static void bindExecutor(IExecutor executor) {

		Preconditions
				.checkNotNull("The executor to be bound must be not null!");

		Activator.cExecutor = executor;
		LOG.debug("Bound {} as an implementation of {}", executor.getClass()
				.getSimpleName(), IExecutor.class.getSimpleName());

	}

	/**
	 * Removes the binding for an executor. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param executor
	 *            The implementation of {@link IExecutor} to be unbound.
	 */
	public static void unbindExecutor(IExecutor executor) {

		if (executor != null && executor.equals(Activator.cExecutor)) {

			Activator.cExecutor = null;
			LOG.debug("Unbound {} as an implementation of {}", executor
					.getClass().getSimpleName(), IExecutor.class
					.getSimpleName());

		}

	}

	/**
	 * Gets the session.
	 * 
	 * @return The {@link ISession}.
	 */
	public static ISession getSession() {

		if (Activator.cSession == null || !Activator.cSession.isValid()) {

			Activator.cSession = UserManagementProvider
					.getSessionmanagement()
					.loginSuperUser(null,
							UserManagementProvider.getDefaultTenant().getName());

		}

		return Activator.cSession;

	}

	/**
	 * Gets the resource manager.
	 * 
	 * @return The {@link IPeerResourceUsageManager}, if one is bound.
	 */
	public static Optional<IPeerResourceUsageManager> getResourceManager() {

		return Optional.fromNullable(Activator.cResourceManager);

	}

	/**
	 * Binds a resource manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The implementation of {@link IPeerResourceUsageManager} to be
	 *            bound.
	 */
	public static void bindResourceManager(IPeerResourceUsageManager manager) {

		Preconditions
				.checkNotNull("The resource manager to be bound must be not null!");

		Activator.cResourceManager = manager;
		LOG.debug("Bound {} as an implementation of {}", manager.getClass()
				.getSimpleName(), IPeerResourceUsageManager.class
				.getSimpleName());

	}

	/**
	 * Removes the binding for a resource manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The implementation of {@link IPeerResourceUsageManager} to be
	 *            unbound.
	 */
	public static void unbindResourceManager(IPeerResourceUsageManager manager) {

		if (manager != null && manager.equals(Activator.cResourceManager)) {

			Activator.cResourceManager = null;
			LOG.debug("Unbound {} as an implementation of {}", manager
					.getClass().getSimpleName(),
					IPeerResourceUsageManager.class.getSimpleName());

		}

	}

	/**
	 * Gets the peer dictionary.
	 * 
	 * @return The {@link IP2PDictionary}, if one is bound.
	 */
	public static Optional<IP2PDictionary> getPeerDictionary() {

		return Optional.fromNullable(Activator.cPeerDictionary);

	}

	/**
	 * Binds a peer dictionary. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param dictionary
	 *            The implementation of {@link IP2PDictionary} to be bound.
	 */
	public static void bindPeerDictionary(IP2PDictionary dictionary) {

		Preconditions
				.checkNotNull("The peer dictionary to be bound must be not null!");

		Activator.cPeerDictionary = dictionary;
		LOG.debug("Bound {} as an implementation of {}", dictionary.getClass()
				.getSimpleName(), IP2PDictionary.class.getSimpleName());

	}

	/**
	 * Removes the binding for a peer dictionary. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param dictionary
	 *            The implementation of {@link IP2PDictionary} to be unbound.
	 */
	public static void unbindPeerDictionary(IP2PDictionary dictionary) {

		if (dictionary != null && dictionary.equals(Activator.cPeerDictionary)) {

			Activator.cPeerDictionary = null;
			LOG.debug("Unbound {} as an implementation of {}", dictionary
					.getClass().getSimpleName(), IP2PDictionary.class
					.getSimpleName());

		}

	}

	/**
	 * Gets the network manager.
	 * 
	 * @return The {@link IP2PNetworkManager}, if one is bound.
	 */
	public static Optional<IP2PNetworkManager> getNetworkManager() {

		return Optional.fromNullable(Activator.cNetworkmanager);

	}

	/**
	 * Binds a network manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The implementation of {@link IP2PNetworkManager} to be bound.
	 */
	public static void bindNetworkManager(IP2PNetworkManager manager) {

		Preconditions
				.checkNotNull("The network manager to be bound must be not null!");

		Activator.cNetworkmanager = manager;
		LOG.debug("Bound {} as an implementation of {}", manager.getClass()
				.getSimpleName(), IP2PNetworkManager.class.getSimpleName());

	}

	/**
	 * Removes the binding for a network manager. <br />
	 * Called by OSGI-DS.
	 * 
	 * @param manager
	 *            The implementation of {@link IP2PNetworkManager} to be
	 *            unbound.
	 */
	public static void unbindNetworkManager(IP2PNetworkManager manager) {

		if (manager != null && manager.equals(Activator.cNetworkmanager)) {

			Activator.cNetworkmanager = null;
			LOG.debug("Unbound {} as an implementation of {}", manager
					.getClass().getSimpleName(), IP2PNetworkManager.class
					.getSimpleName());

		}

	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {

		// Nothing to do.

	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		// Nothing to do.

	}

}