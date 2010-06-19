package de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManagerListener;

public class ExtensibleModelManager<C> extends ModelManager<C> {

	private static final Logger logger = LoggerFactory.getLogger( ModelManager.class );

	public ExtensibleModelManager() {
		checkForExtensions();
	}

	@SuppressWarnings("unchecked")
	private void checkForExtensions() {
//		System.out.println("Check extensions");
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("de.uniol.inf.is.odysseus.rcp.viewer.model.ModelManagerExtensionPoint");
		for( int i = 0; i < elements.length; i++ ) {
//			System.out.println("Extension found!");
			IConfigurationElement element = elements[i];
//			System.out.println(i + " : " + element.getName());
			try {
				IModelManagerListener<C> listener = (IModelManagerListener<C>)element.createExecutableExtension("class");
				addListener(listener);
//				System.out.println("Listener added");
			} catch( CoreException ex ) {
				logger.error(ex.getMessage());
			}
		}
//		System.out.println("Check finished");
	}
}
