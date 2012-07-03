package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.SettingDescriptor;

public class DashboardPartExtensionPointResolver {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartExtensionPointResolver.class);

	public static void execute(IExtensionRegistry registry) {
		Preconditions.checkNotNull(registry, "ExtensionRegistry must not be null!");

		evaluateExtensions(registry);
	}

	private static void evaluateExtensions(IExtensionRegistry registry) {
		IConfigurationElement[] config = registry.getConfigurationElementsFor(DashboardPlugIn.EXTENSION_POINT_ID);
		for (IConfigurationElement e : config) {
			try {
				evaluateDashboardPartExtension(e);
			} catch (Throwable t) {
				LOG.error("Could not resolve extension for dashboard part", t);
			}
		}
	}

	private static void evaluateDashboardPartExtension(IConfigurationElement e) throws Exception {
		String name = e.getAttribute("name");
		String description = e.getAttribute("description");

		Class<? extends IDashboardPart> clazz = checkAndGetDashboardPartClass(e.createExecutableExtension("class"));

		List<SettingDescriptor<?>> settingDescriptors = Lists.newArrayList();
		for (IConfigurationElement child : e.getChildren()) {
			settingDescriptors.add(evaluateSetting(child));
		}

		// TODO: Image
		DashboardPartRegistry.register(clazz, new DashboardPartDescriptor(name, description, settingDescriptors));
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
