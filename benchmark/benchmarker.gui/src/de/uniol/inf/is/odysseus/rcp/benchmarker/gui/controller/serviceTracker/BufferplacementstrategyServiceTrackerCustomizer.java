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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;

/**
 * Diese Klasse holt sich die Bufferplacementstrategieservices aus Odysseus
 * 
 * @author Stefanie Witzke
 * 
 */
public class BufferplacementstrategyServiceTrackerCustomizer implements ServiceTrackerCustomizer {

	private final BundleContext context;
	private final List<String> bufferplacementstrategyNames;

	public BufferplacementstrategyServiceTrackerCustomizer(BundleContext context,
			final List<String> bufferplacementstrategyNames) {
		this.context = context;
		this.bufferplacementstrategyNames = bufferplacementstrategyNames;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		IBufferPlacementStrategy service = (IBufferPlacementStrategy) context.getService(reference);
		bufferplacementstrategyNames.add(service.getName());
		return service;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		removedService(reference, service);
		addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		bufferplacementstrategyNames.remove(reference.getProperty("component.Name").toString());
		context.ungetService(reference);
	}
}
