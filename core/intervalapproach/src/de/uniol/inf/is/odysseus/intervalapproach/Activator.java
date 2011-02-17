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
package de.uniol.inf.is.odysseus.intervalapproach;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;

public class Activator implements BundleActivator {

	@Override
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(TimeInterval.class,
				ITimeInterval.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeMetadataType(ITimeInterval.class);
	}

}
