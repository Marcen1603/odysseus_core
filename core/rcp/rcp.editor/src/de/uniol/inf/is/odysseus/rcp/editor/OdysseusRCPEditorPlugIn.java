/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;

public class OdysseusRCPEditorPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor"; 
	
	public static final String LOGICAL_PLAN_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.LogicalPlanEditor";
	public static final String NEW_LOGICAL_PLAN_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.editor.NewLogicalPlanCommand";
	
	public static final String FILENAME_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.editor.FilenameParameter";
	
	public static final String PARAMETERS_VIEW_ID = "de.uniol.inf.is.odysseus.rcp.editor.ParameterView";
	
	public static final String PARAMETER_EDITOR_EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.editor.ParameterEditor";
	public static final String OPERATOR_GROUP_EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.editor.OperatorGroup";
	
	public static final String LOGICAL_PLAN_FILE_EXTENSION = "pln";
	
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusRCPEditorPlugIn.class);
	
	private static IExecutor executor;
	private static ImageManager imageManager;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		imageManager = new ImageManager(context.getBundle());
		imageManager.register("operatorIcon", "icons/operatorIcon.png");
		imageManager.register("operatorGroupIcon", "icons/operatorGroupIcon.png");
		imageManager.register("connectionIcon", "icons/connection.gif");
		
		resolveExtensions();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		imageManager.disposeAll();
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static ImageManager getImageManager() {
		return imageManager;
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
	
	private void resolveExtensions() {
		// Löst die Extensions für die Parametereditoren auf
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(PARAMETER_EDITOR_EXTENSION_ID);
		
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
				LOG.error(ex.getMessage(), ex);
			}
		}
		
		// Extensions für Operatorgruppen
		elements = Platform.getExtensionRegistry().getConfigurationElementsFor(OPERATOR_GROUP_EXTENSION_ID);
		
		// Gruppen
		for( int i = 0; i < elements.length; i++ ) {
			IConfigurationElement element = elements[i];
			String groupName = element.getAttribute("label");
			OperatorGroupRegistry.getInstance().registerOperatorGroup(groupName);
			
			// Operatoren
			for( int j = 0; j < element.getChildren().length; j++ ) {
				IConfigurationElement operatorElement = element.getChildren()[j];
				String opName = operatorElement.getAttribute("name");
				OperatorGroupRegistry.getInstance().registerOperator(opName, groupName);
			}
		}
	}
}
