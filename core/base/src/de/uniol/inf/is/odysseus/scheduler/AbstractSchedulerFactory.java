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
package de.uniol.inf.is.odysseus.scheduler;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

/**
 * Base class for scheduler factories. Sets the name of the factory based on
 * OSGi properties.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractSchedulerFactory implements ISchedulerFactory {
	/**
	 * Name for this factory. Should be unique.
	 */
	private String name = null;

	/**
	 * OSGi-Method: Is called when this object will be activated by OSGi (after
	 * constructor and bind-methods). This method can be used to configure this
	 * object.
	 * 
	 * @param context
	 *            OSGi {@link ComponentContext} provides informations about the
	 *            OSGi environment.
	 */
	protected void activate(ComponentContext context) {
		// Set the name by properties
		setName(context.getProperties());
	}

	/**
	 * Set the name of this factory by properties.
	 * 
	 * @param properties
	 *            Properties containing "component.readableName" or
	 *            "component.name" as name for this factory.
	 */
	protected void setName(Dictionary<?, ?> properties) {
		name = ((String) properties.get("component.readableName"));
		if (name == null) {
			name = ((String) properties.get("component.name"));
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

}
