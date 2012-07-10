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
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;

public class DashboardPartRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartRegistry.class);
	
	private static class Registration {
		public final Class<? extends IDashboardPart> dashboardPartClass;
		public final DashboardPartDescriptor descriptor;
		
		public Registration( Class<? extends IDashboardPart> dashboardPartClass, DashboardPartDescriptor descriptor ) {
			this.dashboardPartClass = dashboardPartClass;
			this.descriptor = descriptor;
		}
	}
	
	private static final Map<String, Registration> registeredParts = Maps.newHashMap();
	
	public DashboardPartRegistry() {
		// probably automatically invoked by declarative 
		// services or extension point resolvers
	}
	
	public static void register( Class<? extends IDashboardPart> dashboardPartClass, DashboardPartDescriptor descriptor ) {
		Preconditions.checkNotNull(dashboardPartClass, "Class of IDashboardPart must not be null!");
		Preconditions.checkNotNull(descriptor, "Descriptor of DashboardPart must not be null!");
		Preconditions.checkArgument(!isRegistered(dashboardPartClass), "DashboardPartClass %s already registered!", dashboardPartClass);
		
		registeredParts.put(descriptor.getName(), new Registration(dashboardPartClass, descriptor));
		LOG.debug("Registered DashboardPart {}.", descriptor.getName());
	}
	
	public static void unregister( Class<? extends IDashboardPart> dashboardPartClass ) {
		Preconditions.checkNotNull(dashboardPartClass, "Class of IDashboardPart must not be null!");
		
		Optional<String> registrationName = getRegistrationName(dashboardPartClass);
		if( registrationName.isPresent() ) {
			unregister(registrationName.get());
		} else {
			LOG.warn("Tried to unregister unknown DashboardPart " + dashboardPartClass);
		}
	}
	
	public static void unregister( String dashboardPartName ) {
		Preconditions.checkArgument( !Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		
		if( registeredParts.containsKey(dashboardPartName) ) {
			registeredParts.remove(dashboardPartName);
			LOG.debug("Unregistered DashboardPart {}.", dashboardPartName);
		} else {
			LOG.warn("Tried to unregister unknown DashboardPart " + dashboardPartName);
		}
		
	}
	
	public static void unregisterAll() {
		registeredParts.clear();
	}
	
	public static boolean isRegistered( String dashboardPartName ) {
		Preconditions.checkArgument( !Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		return registeredParts.containsKey(dashboardPartName);
	}
	
	public static boolean isRegistered( Class<? extends IDashboardPart> dashboardPartClass) {
		Preconditions.checkNotNull(dashboardPartClass, "Class of IDashboardPart must not be null!");

		return getRegistrationName(dashboardPartClass).isPresent();
	}
	
	public static ImmutableList<String> getDashboardPartNames() {
		return ImmutableList.copyOf(registeredParts.keySet());
	}
	
	public static Optional<Class<? extends IDashboardPart>> getDashboardPartClass( String dashboardPartName ) {
		Preconditions.checkArgument( !Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		
		Registration reg = registeredParts.get(dashboardPartName);
		if( reg != null ) {
			Class<? extends IDashboardPart> clazz = reg.dashboardPartClass;
			return Optional.<Class<? extends IDashboardPart>>of(clazz);
		} else {
			return Optional.absent();
		}
	}
	
	public static Optional<DashboardPartDescriptor> getDashboardPartDescriptor( String dashboardPartName ) {
		Preconditions.checkArgument( !Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		
		Registration reg = registeredParts.get(dashboardPartName);
		if( reg != null ) {
			return Optional.of(reg.descriptor);
		} else {
			return Optional.absent();
		}
	}
	
	public static IDashboardPart createDashboardPart( String dashboardPartName ) throws InstantiationException {
		Preconditions.checkArgument( !Strings.isNullOrEmpty(dashboardPartName), "Name of DashboardPart must not be null or empty!");
		Preconditions.checkArgument(isRegistered(dashboardPartName), "DashboardPart %s not registered!", dashboardPartName);
		
		Registration reg = registeredParts.get(dashboardPartName);
		Optional<? extends IDashboardPart> optInstance = createDashboardPartInstance(reg.dashboardPartClass);
		if( optInstance.isPresent() ) {
			IDashboardPart instance = optInstance.get();
			DashboardPartDescriptor descriptor = reg.descriptor;
			if( instance.init(descriptor.createDefaultConfiguration()) ) {
				LOG.debug("Created DashboardPart-instance of {}.", dashboardPartName);
				return instance;
			}
			
			throw new InstantiationException("Could not create instance of DashboardPart " + dashboardPartName + ", because init failed.");
		}
		
		throw new InstantiationException("Could not create instance of DashboardPart " + dashboardPartName );
	}

	private static Optional<? extends IDashboardPart> createDashboardPartInstance(Class<? extends IDashboardPart> dashboardPartClass) {
		try {
			return Optional.of(dashboardPartClass.newInstance());
		} catch (Throwable t ) {
			LOG.error("Could not create instance of type {}.", dashboardPartClass, t);
			return Optional.absent();
		}
	}

	private static Optional<String> getRegistrationName( Class<? extends IDashboardPart> dashboardPartClass) {
		for( String registrationName : registeredParts.keySet() ) {
			if( registeredParts.get(registrationName).dashboardPartClass.equals(dashboardPartClass)) {
				return Optional.of(registrationName);
			}
		}
		
		return Optional.absent();
	}
}
