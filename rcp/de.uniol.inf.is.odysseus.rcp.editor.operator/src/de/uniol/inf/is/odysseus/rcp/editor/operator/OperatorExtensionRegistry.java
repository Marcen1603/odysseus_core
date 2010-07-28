package de.uniol.inf.is.odysseus.rcp.editor.operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.editor.operator.impl.OperatorExtensionDescriptor;
import de.uniol.inf.is.odysseus.rcp.editor.operator.impl.OperatorExtensionDescriptorList;

public class OperatorExtensionRegistry {

	private static OperatorExtensionRegistry instance = null;
	private final Map<String, IOperatorExtensionDescriptor> operatorExtensions = new HashMap<String, IOperatorExtensionDescriptor>();
	private final Collection<String> operatorGroups = new ArrayList<String>();
	
	private final Logger logger = LoggerFactory.getLogger(OperatorExtensionRegistry.class);
	
	private OperatorExtensionRegistry() {
		
	}
	
	public static OperatorExtensionRegistry getInstance() {
		if( instance == null ) 
			instance = new OperatorExtensionRegistry();
		return instance;
	}
	
	public void loadOperatorExtensions() {
		operatorExtensions.clear();
		
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(IOperatorConstants.OPERATOR_EXTENSION_ID);
		for( int i = 0; i < elements.length; i++ ) {
			IConfigurationElement element = elements[i];
			try {
				String id = element.getAttribute("id");
				String label = element.getAttribute("label");
				String group = element.getAttribute("group");
				ILogicalOperatorExtension extension = (ILogicalOperatorExtension)element.createExecutableExtension("class");
				
				OperatorExtensionDescriptor desc = new OperatorExtensionDescriptor(id);
				desc.setLabel(label);
				desc.setGroup(group);
				desc.setExtensionClass(extension);
				
				operatorExtensions.put(id, desc);
				if( !operatorGroups.contains(group))
					operatorGroups.add(group);
				
			} catch( CoreException ex ) {
				logger.error(ex.getMessage());
			} catch( Exception ex ) {
				logger.error("Exception during loading extension", ex);
			}
		}
	}
	
	public IOperatorExtensionDescriptor getExtension( String id ) {
		return operatorExtensions.get(id);
	}
	
	public IOperatorExtensionDescriptorList getExtensions() {
		OperatorExtensionDescriptorList list = new OperatorExtensionDescriptorList();
		list.addAll(operatorExtensions.values());
		return list;
	}
	
	public Collection<String> getExtensionIDs() {
		return Collections.unmodifiableCollection(operatorExtensions.keySet());
	}
	
	public Collection<String> getGroups() {
		return Collections.unmodifiableCollection(operatorGroups);
	}
}
