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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.Activator;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.StringUtils;

public class BenchmarkMetadata {
	private String version;
	private String name;

	public void getMetadataInformations() {
		BundleContext context = Activator.getDefault().getBundle().getBundleContext();
		Bundle[] bundles = context.getBundles();
		if (bundles == null) {
			System.out.println("BUNDLES IST NULL!!!!");
		} else {
			for (int i = 0; i < bundles.length; i++) {
				Bundle bundle = bundles[i];
				// bundleId = Long.toString(bundle.getBundleId());
				version = bundle.getVersion().getQualifier();
				name = StringUtils.splitString(bundle.getSymbolicName());
				 System.out.println("BUNDLEVERSION = " + version +
				 "BUNDLENAME = " + name);
			}
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
