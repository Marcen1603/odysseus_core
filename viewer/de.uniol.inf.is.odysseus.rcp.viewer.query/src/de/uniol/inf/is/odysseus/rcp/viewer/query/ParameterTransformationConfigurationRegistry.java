package de.uniol.inf.is.odysseus.rcp.viewer.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;

public class ParameterTransformationConfigurationRegistry {

	private final Logger logger = LoggerFactory.getLogger(ParameterTransformationConfigurationRegistry.class);
	
	private static ParameterTransformationConfigurationRegistry instance;
	
	private Map<String, ParameterTransformationConfiguration> transformationConfigs = new HashMap<String, ParameterTransformationConfiguration>();
	
	private ParameterTransformationConfigurationRegistry() {
		
	}
	
	public static ParameterTransformationConfigurationRegistry getInstance() {
		if( instance == null ) 
			instance = new ParameterTransformationConfigurationRegistry();
		return instance;
	}
	
	public void loadExtensionList() {
		transformationConfigs.clear();
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(IQueryConstants.TRANSFORMATION_CONFIGURATION_EXTENSION_ID);
		for( int i = 0; i < elements.length; i++ ) {
			IConfigurationElement element = elements[i];
			try {
				IParameterTransformationConfigurationExtension cfg = (IParameterTransformationConfigurationExtension)element.createExecutableExtension("class");
				String name = element.getAttribute("name");
				
				transformationConfigs.put(name, cfg.get());
			} catch( CoreException ex ) {
				logger.error(ex.getMessage());
			} catch( Exception ex ) {
				logger.error("Exception during loading extension", ex);
			}
		}
	}
	
	public Collection<String> getTransformationConfigurationNames() {
		return transformationConfigs.keySet();
	}
	
	public ParameterTransformationConfiguration getTransformationConfiguration( String name ) {
		return transformationConfigs.get(name);
	}
}
