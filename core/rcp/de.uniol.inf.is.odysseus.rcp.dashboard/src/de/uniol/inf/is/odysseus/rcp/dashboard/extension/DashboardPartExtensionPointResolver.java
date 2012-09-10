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

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class DashboardPartExtensionPointResolver implements IRegistryEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartExtensionPointResolver.class);

	public DashboardPartExtensionPointResolver() {
		// Extensions, welche vor dem Listener bereits registriert wurden
		for(IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(DashboardPlugIn.EXTENSION_POINT_ID)) {
			resolveConfigurationElement(element);
		}
	}
	
	@Override
	public void added(IExtension[] extensions) {
		for( IExtension extension : extensions ) {
			for( IConfigurationElement element : extension.getConfigurationElements()) {
				resolveConfigurationElement(element);
			}
		}
	}

	@Override
	public void removed(IExtension[] extensions) {
		for( IExtension extension : extensions ) {
			for( IConfigurationElement element : extension.getConfigurationElements()) {
				String name = element.getAttribute("name");
				DashboardPartRegistry.unregister(name);
			}
		}
	}

	@Override
	public void added(IExtensionPoint[] extensionPoints) {
		// do nothing
	}

	@Override
	public void removed(IExtensionPoint[] extensionPoints) {
		// do nothing
	}
	
	private static void resolveConfigurationElement(IConfigurationElement element) {
		try {
			Class<? extends IDashboardPart> clazz = checkAndGetDashboardPartClass(element.createExecutableExtension("class"));					
			DashboardPartDescriptor desc = getDashboardPartDescriptorFromExtension(element);
			DashboardPartRegistry.register(clazz, desc);
		} catch( Throwable t ) {
			LOG.error("Could not evaluate extension", t);
		}
	}
	
	private static DashboardPartDescriptor getDashboardPartDescriptorFromExtension(IConfigurationElement e) throws Exception {
		String name = e.getAttribute("name");
		String description = e.getAttribute("description");

		List<SettingDescriptor<?>> settingDescriptors = Lists.newArrayList();
		for (IConfigurationElement child : e.getChildren()) {
			settingDescriptors.add(evaluateSetting(child));
		}

		return new DashboardPartDescriptor(name, description, settingDescriptors);
	}

	private static SettingDescriptor<?> evaluateSetting(IConfigurationElement e) throws Exception {
		String name = e.getAttribute("name");
		String type = e.getAttribute("type");
		String defaultValue = e.getAttribute("defaultValue");
		String description = e.getAttribute("description");
		String isOptional = e.getAttribute("isOptional");
		String isEditable = e.getAttribute("isEditable");

		return new SettingDescriptor<Object>(name, description, type, convertValue(defaultValue, type), Boolean.valueOf(isOptional), Boolean.valueOf(isEditable));
	}

	private static Class<? extends IDashboardPart> checkAndGetDashboardPartClass(Object obj) throws Exception {
		if (!(obj instanceof IDashboardPart)) {
			throw new Exception("Class " + obj.getClass() + " does not implement the interface " + IDashboardPart.class);
		}
		IDashboardPart part = (IDashboardPart) obj;
		return part.getClass();
	}

	private static Object convertValue(String value, String type) throws Exception {
		if (value == null) {
			return null;
		}

		if ("Integer".equalsIgnoreCase(type)) {
			return Integer.valueOf(value);
		}

		if ("Long".equalsIgnoreCase(type)) {
			return Long.valueOf(value);
		}

		if ("Double".equalsIgnoreCase(type)) {
			return Double.valueOf(value);
		}

		if ("Boolean".equalsIgnoreCase(type)) {
			return Boolean.valueOf(value);
		}

		if ("Float".equalsIgnoreCase(type)) {
			return Float.valueOf(value);
		}

		if ("String".equalsIgnoreCase(type)) {
			return value;
		}

		throw new Exception("Setting type " + type + " not supported!");
	}
}
