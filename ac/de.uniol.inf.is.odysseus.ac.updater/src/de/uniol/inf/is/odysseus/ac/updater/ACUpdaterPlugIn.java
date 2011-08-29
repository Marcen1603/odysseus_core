package de.uniol.inf.is.odysseus.ac.updater;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.ac.IAdmissionReaction;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

public class ACUpdaterPlugIn implements BundleActivator {

	private static IAdmissionControl admissionControl;
	private static IExecutor executor;
	private static IAdmissionReaction admissionReaction;
	
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
			updater.startRunning(executor);
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
			updater.startRunning(executor);
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
	
	public void bindAdmissionReaction( IAdmissionReaction ar ) {
		admissionReaction = ar;
	}
	
	public void unbindAdmissionReaction( IAdmissionReaction ar ) {
		if( ar == admissionReaction ) {
			admissionReaction = null;
		}
	}
	
	public static IAdmissionReaction getAdmissionReaction() {
		return admissionReaction;
	}
}
