/** Copyright [2011] [The Odysseus Team]
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
import org.eclipse.core.runtime.Platform;

public class StreamEditorRegistry {

	public static final String EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.viewer.StreamEditor";
	private static StreamEditorRegistry instance;

	private List<StreamExtensionDefinition> definitions = new ArrayList<StreamExtensionDefinition>();

	private StreamEditorRegistry() {
		evaluateRegisteredExtensions();
	}

	public static StreamEditorRegistry getInstance() {
		if (instance == null)
			instance = new StreamEditorRegistry();
		return instance;
	}

	public List<StreamExtensionDefinition> getStreamExtensionDefinitions() {
		return definitions;
	}

	public void evaluateRegisteredExtensions() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
		for (IConfigurationElement e : config) {

			StreamExtensionDefinition def = new StreamExtensionDefinition();
			def.setID(e.getAttribute("id"));
			def.setLabel(e.getAttribute("label"));
			def.setConfigElement(e);
			definitions.add(def);
		}
	}
}
