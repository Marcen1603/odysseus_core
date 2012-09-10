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
package de.uniol.inf.is.odysseus.rcp.viewer.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;

public class StreamEditorRegistry implements IRegistryEventListener {

	public static final String EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.viewer.StreamEditor";
	private static StreamEditorRegistry instance;

	private List<StreamExtensionDefinition> definitions = new ArrayList<StreamExtensionDefinition>();

	private StreamEditorRegistry() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
		for (IConfigurationElement e : config) {
			registerStreamEditorDefinition(e);
		}
		
		Platform.getExtensionRegistry().addListener(this, EXTENSION_ID);
	}

	public static StreamEditorRegistry getInstance() {
		if (instance == null)
			instance = new StreamEditorRegistry();
		return instance;
	}

	public List<StreamExtensionDefinition> getStreamExtensionDefinitions() {
		return definitions;
	}

	@Override
	public void added(IExtension[] extensions) {
		for( IExtension extension: extensions ) {
			for(IConfigurationElement element : extension.getConfigurationElements()) {
				registerStreamEditorDefinition(element);
			}
		}
	}

	@Override
	public void removed(IExtension[] extensions) {
		for( IExtension extension: extensions ) {
			for(IConfigurationElement element : extension.getConfigurationElements()) {
				unregisterStreamEditorDefinition(element);
			}
		}
	}
	
	@Override
	public void added(IExtensionPoint[] extensionPoints) {
	}

	@Override
	public void removed(IExtensionPoint[] extensionPoints) {
	}

	private void registerStreamEditorDefinition(IConfigurationElement e) {
		StreamExtensionDefinition def = new StreamExtensionDefinition();
		def.setID(e.getAttribute("id"));
		def.setLabel(e.getAttribute("label"));
		def.setConfigElement(e);
		
		synchronized(definitions) {
			definitions.add(def);
		}
	}

	private void unregisterStreamEditorDefinition(IConfigurationElement element) {
		String defId = element.getAttribute("id");
		
		synchronized(definitions) {
			for( StreamExtensionDefinition def : definitions.toArray(new StreamExtensionDefinition[0]) ) {
				if(def.getID().equals(defId)) {
					definitions.remove(def);
					break;
				}
			}
		}
	}

}
