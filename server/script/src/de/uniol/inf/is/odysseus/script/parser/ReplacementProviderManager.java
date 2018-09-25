package de.uniol.inf.is.odysseus.script.parser;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;

public class ReplacementProviderManager {
	
	private static final InfoService INFO_SERVICE = InfoServiceFactory.getInfoService(ReplacementProviderManager.class);
	private static final Logger LOG = LoggerFactory.getLogger(ReplacementProviderManager.class);
	
	private static final Collection<IReplacementProvider> PROVIDERS = Lists.newArrayList();

	// called by OSGi-DS
	public static void bindReplacementProvider( IReplacementProvider provider ) {
		PROVIDERS.add(provider);
	}

	// called by OSGi-DS
	public static void unbindReplacementProvider( IReplacementProvider provider ) {
		PROVIDERS.remove(provider);
	}
	
	public static Map<String, IReplacementProvider> generateProviderMap() {
		Map<String, IReplacementProvider> replacementProviderMap = Maps.newHashMap();
		
		for( IReplacementProvider provider : PROVIDERS ) {
			Collection<String> replacementKeys = provider.getReplacementKeys();
			if( replacementKeys != null ) {
				for( String replacementKey : replacementKeys ) {
					String replacementKeyUpperCase = replacementKey.toUpperCase();
					if( replacementProviderMap.containsKey(replacementKeyUpperCase)) {
						IReplacementProvider prevProvider = replacementProviderMap.get(replacementKeyUpperCase);
						
						String warningMessage = "Replacementkey '" + replacementKeyUpperCase + "' is defined by multiple replacement providers: " + prevProvider + " and " + provider;
						INFO_SERVICE.warning(warningMessage);
						LOG.warn(warningMessage);
					}
					replacementProviderMap.put(replacementKeyUpperCase, provider);
				}
			}
		}
		
		return replacementProviderMap;
	}
}
