package de.uniol.inf.is.odysseus.rcp.login.extension;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;
import de.uniol.inf.is.odysseus.rcp.login.RCPLoginPlugIn;

public class LoginContributionEPManager implements IRegistryEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(LoginContributionEPManager.class);
	
	private final LoginContributionRegistry registry;
	
	public LoginContributionEPManager( LoginContributionRegistry registry ) {
		// Preconditions.checkNotNull(registry, "Login contribution registry must not be null!");
		
		this.registry = registry;
	}
	
	public void checkForExtensionPoints() {
		LOG.debug("Check for login contribution extension points");
		IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor(RCPLoginPlugIn.EXTENSION_POINT_ID);
		LOG.debug("Found {} login contribution extension points", configurationElementsFor.length);
		
		for (IConfigurationElement configElement : configurationElementsFor) {
			resolveConfigurationElement(configElement);
		}
	}

	@Override
	public void added(IExtension[] extensions) {
		LOG.debug("Got {} new login contribution extension points", extensions.length);
		
		for( IExtension extension : extensions ) {
			for( IConfigurationElement configElement : extension.getConfigurationElements()) {
				resolveConfigurationElement(configElement);
			}
		}
	}

	@Override
	public void removed(IExtension[] extensions) {
		LOG.debug("Removed {} new login contribution extension points", extensions.length);
		
		for( IExtension extension : extensions ) {
			for( IConfigurationElement configElement : extension.getConfigurationElements()) {
				unresolveConfigurationElement(configElement);
			}
		}
	}

	@Override
	public void added(IExtensionPoint[] extensionPoints) {
		// do nothing
	}

	@Override
	public void removed(IExtensionPoint[] extensionPoints) {
		// do nothing
	}
	
	private void resolveConfigurationElement(IConfigurationElement element) {
		Optional<Class<? extends ILoginContribution>> optContribClass = getLoginContributionClass(element);
		if( optContribClass.isPresent() ) {
			Class<? extends ILoginContribution> contribClass = optContribClass.get();
			
			if( !registry.contains(contribClass)) {
				registry.add(contribClass);
			} else {
				LOG.error("Login contribution {} already added to registry", contribClass.getName());
			}
		}
	}

	private void unresolveConfigurationElement(IConfigurationElement element) {
		Optional<Class<? extends ILoginContribution>> optContribClass = getLoginContributionClass(element);
		if( optContribClass.isPresent() ) {
			registry.remove(optContribClass.get());
		}
	}

	private static Optional<Class<? extends ILoginContribution>> getLoginContributionClass(IConfigurationElement configElement) {
		try {
			Object execExtension = configElement.createExecutableExtension("class");

			if (execExtension instanceof ILoginContribution) {
				ILoginContribution contrib = (ILoginContribution) execExtension;
				return Optional.<Class<? extends ILoginContribution>>of(contrib.getClass());
			} 
			
			LOG.error("Class {} does not implement {}", execExtension.getClass(), ILoginContribution.class);
			
		} catch (CoreException ex) {
			LOG.error("Could not resolve login contribution extension", ex);
		}
		
		return Optional.absent();
	}
}
