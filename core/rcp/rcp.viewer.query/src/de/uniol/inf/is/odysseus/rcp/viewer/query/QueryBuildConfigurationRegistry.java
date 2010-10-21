package de.uniol.inf.is.odysseus.rcp.viewer.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class QueryBuildConfigurationRegistry {

	private final Logger logger = LoggerFactory.getLogger(QueryBuildConfigurationRegistry.class);
	
	private static QueryBuildConfigurationRegistry instance;
	
	private Map<String, List<IQueryBuildSetting>> queryBuildConfigs = new HashMap<String, List<IQueryBuildSetting>>();
	
	private QueryBuildConfigurationRegistry() {
		
	}
	
	public static QueryBuildConfigurationRegistry getInstance() {
		if( instance == null ) 
			instance = new QueryBuildConfigurationRegistry();
		return instance;
	}
	
	public void loadExtensionList() {
		queryBuildConfigs.clear();
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(IQueryConstants.QUERY_BUILD_CONFIGURATION_EXTENSION_ID);
		for( int i = 0; i < elements.length; i++ ) {
			IConfigurationElement element = elements[i];
			try {
				IQueryBuildConfiguration cfg = (IQueryBuildConfiguration)element.createExecutableExtension("class");
				String name = element.getAttribute("name");
				
				queryBuildConfigs.put(name, cfg.get());
			} catch( CoreException ex ) {
				logger.error(ex.getMessage());
			} catch( Exception ex ) {
				logger.error("Exception during loading extension", ex);
			}
		}
	}
	
	public Collection<String> getQueryBuildConfigurationNames() {
		return queryBuildConfigs.keySet();
	}
	
	public List<IQueryBuildSetting> getQueryBuildConfiguration( String name ) {
		return queryBuildConfigs.get(name);
	}
}
