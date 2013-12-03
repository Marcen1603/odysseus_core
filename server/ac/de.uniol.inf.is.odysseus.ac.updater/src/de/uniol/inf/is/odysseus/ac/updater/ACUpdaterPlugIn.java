/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.ac.updater;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.ac.IAdmissionControl;

/**
 * Activator-Class for ac.updater-Plugin.
 * 
 * @author Timo Michelsen
 *
 */
public class ACUpdaterPlugIn implements BundleActivator {

	private static IAdmissionControl admissionControl;
	private static IExecutor executor;
	
	private static ACUpdater updater;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	public void bindAdmissionControl( IAdmissionControl adc ) {
		admissionControl = adc;
		
		if( updater != null ) {
			updater.stopRunning();
		}
		
		updater = new ACUpdater( adc );
		
		if( executor != null )
			updater.startRunning();
	}
	
	public void unbindAdmissionControl( IAdmissionControl adc ) {
		if( adc == admissionControl ) {
			admissionControl = null;
			
			updater.stopRunning();
			updater = null;
		}
	}
	
	public static IAdmissionControl getAdmissionControl() {
		return admissionControl;
	}
	
	public void bindExecutor( IExecutor e ) {
		executor = e;
		
		if( admissionControl != null ) {
			updater.startRunning();
		}
	}
	
	public void unbindExecutor( IExecutor e ) {
		if( executor == e ) {
			executor = null;
			
			if( admissionControl != null ) 
				updater.stopRunning();
		}
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
}
