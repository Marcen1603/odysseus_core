package de.uniol.inf.is.odysseus.rcp.viewer.views;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.IOperatorDetailProvider;
import de.uniol.inf.is.odysseus.rcp.IOperatorGeneralDetailProvider;

public final class OperatorDetailProviderRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(OperatorDetailProviderRegistry.class);
	
	private final Map<Class<? extends IPhysicalOperator>, List<Class<? extends IOperatorDetailProvider<?>>>> providerMap = Maps.newHashMap();
	private final List<Class<? extends IOperatorGeneralDetailProvider>> generalProviders = Lists.newArrayList(); 
	
	private static OperatorDetailProviderRegistry instance;
	
	// called by OSGi-DS
	public void activate() {
		instance = this;
	}
	
	// called by OSGi-DS
	public void deactivate() {
		instance = null;
	}
	
	// called by OSGi-DS
	@SuppressWarnings("unchecked")
	public void bindOperatorDetailProvider( IOperatorDetailProvider<? extends IPhysicalOperator> provider ) {
		if( !containsProvider(provider)) {
			
			Collection<? extends Class<? extends IPhysicalOperator>> operatorClasses = provider.getOperatorTypes();
			
			synchronized( providerMap ) {
				for( Class<? extends IPhysicalOperator> operatorClass : operatorClasses ) {
					List<Class<? extends IOperatorDetailProvider<?>>> providers = getProviderClassListFromMap(operatorClass);
					providers.add((Class<? extends IOperatorDetailProvider<?>>) provider.getClass());
				}
			}
			
			LOG.debug("OperatorDetailProvider bound {}", provider);
		} else {
			throw new IllegalArgumentException("OperatorDetailProvider " + provider + " already bound!");
		}
	}
	
	// called by OSGi-DS
	public void unbindOperatorDetailProvider( IOperatorDetailProvider<?> provider ) {
		synchronized( providerMap ) {
			for( List<Class<? extends IOperatorDetailProvider<?>>> providers : providerMap.values()) {
				if( providers.remove(provider) ) {
					LOG.debug("OperatorDetailProvider unbound {}" , provider);
				}
			}
		}
	}
	
	// called by OSGi-DS
	public void bindOperatorGeneralDetailProvider( IOperatorGeneralDetailProvider generalProvider ) {
		if( !generalProviders.contains(generalProvider)) {
			generalProviders.add(generalProvider.getClass());
			
			LOG.debug("OperatorGeneralDetailProvider bound {}", generalProvider);
			return;
		} 
		
		throw new IllegalArgumentException("OperatorGeneralDetailProvider " + generalProvider + " already bound");
	}
	
	// called by OSGi-DS
	public void unbindOperatorGeneralDetailProvider( IOperatorGeneralDetailProvider generalProvider ) {
		if( generalProviders.contains(generalProvider)) {
			generalProviders.remove(generalProvider);
			
			LOG.debug("OperatorGeneralDetailProvider unbound {}", generalProvider);
		}
	}
	
	public ImmutableList<Class<? extends IPhysicalOperator>> getOperators() {
		return ImmutableList.copyOf(providerMap.keySet());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IPhysicalOperator> List<Class<? extends IOperatorDetailProvider<T>>> getProviders(Class<T> operatorClass) {
		List<?> providers = null;
		synchronized( providerMap ) {
			providers = providerMap.get(operatorClass);
		}
		
		if (providers == null) {
			return Lists.newArrayList();
		}
		
		return (List<Class<? extends IOperatorDetailProvider<T>>>) providers;
	}
	
	public ImmutableList<Class<? extends IOperatorGeneralDetailProvider>> getGeneralProviders() {
		return ImmutableList.copyOf(generalProviders);
	}

	public static OperatorDetailProviderRegistry getInstance() {
		return instance;
	}
	
	public static boolean isActivated() {
		return instance != null;
	}

	private boolean containsProvider(IOperatorDetailProvider<?> provider) {
		for (Class<? extends IPhysicalOperator> operator : providerMap.keySet()) {
			List<Class<? extends IOperatorDetailProvider<?>>> providers = providerMap.get(operator);
			if (providers.contains(provider.getClass())) {
				return true;
			}
		}

		return false;
	}
	
	private List<Class<? extends IOperatorDetailProvider<?>>> getProviderClassListFromMap(Class<? extends IPhysicalOperator> operatorClass) {
		if (providerMap.containsKey(operatorClass)) {
			return providerMap.get(operatorClass);
		}

		List<Class<? extends IOperatorDetailProvider<?>>> providers = Lists.newArrayList();
		providerMap.put(operatorClass, providers);
		return providers;
	}
}
