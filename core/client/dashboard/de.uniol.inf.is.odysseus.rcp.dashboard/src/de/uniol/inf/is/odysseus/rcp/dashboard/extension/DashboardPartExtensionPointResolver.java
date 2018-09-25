/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.extension;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartConfigurer;

public class DashboardPartExtensionPointResolver implements IRegistryEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartExtensionPointResolver.class);

	public DashboardPartExtensionPointResolver() {
		// Extensions, welche vor dem Listener bereits registriert wurden
		for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(DashboardPlugIn.EXTENSION_POINT_ID)) {
			resolveConfigurationElement(element);
		}
	}

	@Override
	public void added(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			for (IConfigurationElement element : extension.getConfigurationElements()) {
				resolveConfigurationElement(element);
			}
		}
	}

	@Override
	public void added(IExtensionPoint[] extensionPoints) {
		// do nothing
	}

	@Override
	public void removed(IExtension[] extensions) {
		for (IExtension extension : extensions) {
			for (IConfigurationElement element : extension.getConfigurationElements()) {
				String name = element.getAttribute("name");
				DashboardPartRegistry.unregister(name);
			}
		}
	}

	@Override
	public void removed(IExtensionPoint[] extensionPoints) {
		// do nothing
	}

	@SuppressWarnings("unchecked")
	private static Class<IDashboardPart> checkAndGetDashboardPartClass(Object obj) throws Exception {
		if (!(obj instanceof IDashboardPart)) {
			throw new Exception("Class " + obj.getClass() + " does not implement the interface " + IDashboardPart.class);
		}
		IDashboardPart part = (IDashboardPart) obj;
		return (Class<IDashboardPart>) part.getClass();
	}

	@SuppressWarnings("unchecked")
	private static Class<IDashboardPartConfigurer<? extends IDashboardPart>> checkAndGetDashboardPartConfigurerClass(Object obj) throws Exception {
		if (!(obj instanceof IDashboardPartConfigurer)) {
			throw new Exception("Class " + obj.getClass() + " does not implement the interface " + IDashboardPartConfigurer.class);
		}
		IDashboardPartConfigurer<?> part = (IDashboardPartConfigurer<?>) obj;
		return (Class<IDashboardPartConfigurer<? extends IDashboardPart>>) part.getClass();
	}


	private static void resolveConfigurationElement(IConfigurationElement element) {
		try {
			Class<? extends IDashboardPart> dashboardPartClass = checkAndGetDashboardPartClass(element.createExecutableExtension("class"));
			Class<? extends IDashboardPartConfigurer<? extends IDashboardPart>> dashboardPartConfigurerClass = checkAndGetDashboardPartConfigurerClass(element.createExecutableExtension("configClass"));
			String name = element.getAttribute("name");
			DashboardPartRegistry.register(dashboardPartClass, dashboardPartConfigurerClass, name);
		} catch (Throwable t) {
			LOG.error("Could not evaluate extension", t);
		}
	}

}
