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
package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class DashboardPartRegistry {

	private static class Registration {
		public final Class<? extends IDashboardPart> dashboardPartClass;
		public final Class<? extends IDashboardPartConfigurer<?>> dashboardPartConfigurerClass;
		
		public Registration(Class<? extends IDashboardPart> dashboardPartClass, Class<? extends IDashboardPartConfigurer<?>> configurer) {
			this.dashboardPartClass = dashboardPartClass;
			this.dashboardPartConfigurerClass = configurer;
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartRegistry.class);

	private static final Map<String, Registration> registeredParts = Maps.newHashMap();

	public static IDashboardPart createDashboardPart(String dashboardPartName) throws InstantiationException {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		// Preconditions.checkArgument(isRegistered(dashboardPartName), "DashboardPart %s not registered!", dashboardPartName);

		final Registration reg = registeredParts.get(dashboardPartName);
		final Optional<? extends IDashboardPart> optInstance = createDashboardPartInstance(reg.dashboardPartClass);
		if (optInstance.isPresent()) {
			return optInstance.get();
		}

		throw new InstantiationException("Could not create instance of DashboardPart " + dashboardPartName);
	}
	
	public static IDashboardPartConfigurer<?> createDashboardPartConfigurer( String dashboardPartName ) throws InstantiationException {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		// Preconditions.checkArgument(isRegistered(dashboardPartName), "DashboardPart %s not registered!", dashboardPartName);

		final Registration reg = registeredParts.get(dashboardPartName);
		Optional<? extends IDashboardPartConfigurer<?>> optConfigurer = createDashboardPartConfigurerInstance(reg.dashboardPartConfigurerClass);
		if( optConfigurer.isPresent() ) {
			return optConfigurer.get();
		}
		
		throw new InstantiationException("Could not create instance of DashboardPartConfigurer " + dashboardPartName);		
	}

	public static Optional<Class<? extends IDashboardPart>> getDashboardPartClass(String dashboardPartName) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");

		final Registration reg = registeredParts.get(dashboardPartName);
		if (reg != null) {
			final Class<? extends IDashboardPart> clazz = reg.dashboardPartClass;
			return Optional.<Class<? extends IDashboardPart>> of(clazz);
		}
		return Optional.absent();
	}

	public static ImmutableList<String> getDashboardPartNames() {
		return ImmutableList.copyOf(registeredParts.keySet());
	}

	public static boolean isRegistered(Class<? extends IDashboardPart> dashboardPartClass) {
		Objects.requireNonNull(dashboardPartClass, "Class of IDashboardPart must not be null!");

		return getRegistrationName(dashboardPartClass).isPresent();
	}

	public static boolean isRegistered(String dashboardPartName) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		return registeredParts.containsKey(dashboardPartName);
	}

	public static void register(Class<? extends IDashboardPart> dashboardPartClass, Class<? extends IDashboardPartConfigurer<?>> dashboardPartConfigurer, String dashboardPartName) {
		Objects.requireNonNull(dashboardPartClass, "Class of IDashboardPart must not be null!");
		Objects.requireNonNull(dashboardPartConfigurer, "Class of IDashboardPartConfigurer must not be null!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardPartName), "DashboardPartName must not be null or empty!");
		// Preconditions.checkArgument(!isRegistered(dashboardPartClass), "DashboardPartClass %s already registered!", dashboardPartClass);
		// Preconditions.checkArgument(!isRegistered(dashboardPartName), "DashboardPartName %s already registered!", dashboardPartName);

		registeredParts.put(dashboardPartName, new Registration(dashboardPartClass, dashboardPartConfigurer));
		LOG.debug("Registered DashboardPart {}.",  dashboardPartName);
	}

	public static void unregister(Class<? extends IDashboardPart> dashboardPartClass) {
		Objects.requireNonNull(dashboardPartClass, "Class of IDashboardPart must not be null!");

		final Optional<String> registrationName = getRegistrationName(dashboardPartClass);
		if (registrationName.isPresent()) {
			unregister(registrationName.get());
		} else {
			LOG.warn("Tried to unregister unknown DashboardPart " + dashboardPartClass);
		}
	}

	public static void unregister(String dashboardPartName) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");

		if (registeredParts.containsKey(dashboardPartName)) {
			registeredParts.remove(dashboardPartName);
			LOG.debug("Unregistered DashboardPart {}.", dashboardPartName);
		} else {
			LOG.warn("Tried to unregister unknown DashboardPart " + dashboardPartName);
		}

	}

	public static void unregisterAll() {
		registeredParts.clear();
	}

	private static Optional<? extends IDashboardPart> createDashboardPartInstance(Class<? extends IDashboardPart> dashboardPartClass) {
		try {
			return Optional.of(dashboardPartClass.newInstance());
		} catch (final Throwable t) {
			LOG.error("Could not create instance of type {}.", dashboardPartClass, t);
			return Optional.absent();
		}
	}
	
	private static Optional<? extends IDashboardPartConfigurer<?>> createDashboardPartConfigurerInstance(Class<? extends IDashboardPartConfigurer<?>> dashboardPartClass) {
		try {
			return Optional.of(dashboardPartClass.newInstance());
		} catch (final Throwable t) {
			LOG.error("Could not create instance of type {}.", dashboardPartClass, t);
			return Optional.absent();
		}
	}

	public static Optional<String> getRegistrationName(Class<? extends IDashboardPart> dashboardPartClass) {
		for (final String registrationName : registeredParts.keySet()) {
			if (registeredParts.get(registrationName).dashboardPartClass.equals(dashboardPartClass)) {
				return Optional.of(registrationName);
			}
		}

		return Optional.absent();
	}
}
