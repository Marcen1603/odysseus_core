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

import org.eclipse.core.runtime.IConfigurationElement;

public class StreamExtensionDefinition {

	private String id;
	private IConfigurationElement configElement;
	private String label;
	
	public StreamExtensionDefinition() {}
	
	public void setID( String id ) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}

	public IConfigurationElement getConfigElement() {
		return configElement;
	}

	public void setConfigElement(IConfigurationElement configElement) {
		this.configElement = configElement;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
