package de.uniol.inf.is.odysseus.rcp.editor.activator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.ILogicalPlanEditorConstants;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.ParameterEditorRegistry;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor"; //$NON-NLS-1$
	
	private static Activator plugin;
	private final Logger logger = LoggerFactory.getLogger(Activator.class);
	private static IExecutor executor = null;
	
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		resolveExtensions();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public void bindExecutor( IExecutor ex ) {
		executor = ex;
	}
	
	public void unbindExecutor( IExecutor ex ) {
		executor = null;
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
	
	// Löst die Extensions für die Parametereditoren auf
	private void resolveExtensions() {
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(ILogicalPlanEditorConstants.PARAMETER_EDITOR_EXTENSION_ID);
		
		// operatoren
		for( int i = 0; i < elements.length; i++ ) {
			IConfigurationElement element = elements[i];
			try {
				final String operatorName = element.getAttribute("name");
				
				// editoren
				for( int j = 0; j < element.getChildren().length; j++ ) {
					IConfigurationElement editorElement = element.getChildren()[j];
					
					IParameterEditor editor = (IParameterEditor)editorElement.createExecutableExtension("class");
					String attrName = editorElement.getAttribute("name");
					String finalName = operatorName + ParameterEditorRegistry.NAME_SEPARATOR + attrName;
					ParameterEditorRegistry.getInstance().register(finalName, editor.getClass());
				}
			} catch( CoreException ex ) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}
}
