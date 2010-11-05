package de.uniol.inf.is.odysseus.broker.benchmarker;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class BenchmarkConsole implements CommandProvider, BenchmarkService {

	private CommandInterpreter lastCI;
	
	private int runs = 3;
	
	@Override
	public String getHelp() {
		return "";
	}

	public void _runtest(CommandInterpreter ci) {
		//ci.execute("runfile eva");		
		this.lastCI = ci;
		startAll(ci);				
		ci.println("wait...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ci.execute("runfile eva");
		//stopAll(ci);
		//ci.println("ALL STOPPED!!");
	}	
	

	private void stopAll(CommandInterpreter ci) {		
		int i = 1;
		BundleContext context = Activator.getContext();
		Bundle thisBundle = context.getBundle();
		ci.println(thisBundle);
		for (Bundle bundle : Activator.getContext().getBundles()) {
			String name = bundle.getSymbolicName();
			if ((!name.startsWith("org.eclipse")) 
					&& (!name.startsWith("com.")) 
					&& (!bundle.equals(thisBundle))
					&& (!name.endsWith("newconsole"))) {

				// ci.println(i + " stopping: " + bundle.getSymbolicName());
				try {
					if (!isFragment(bundle)) {												
						bundle.stop();
						bundle.uninstall();
					}
				} catch (BundleException e) {
					e.printStackTrace();
				}
				i++;
			}
		}
	}

	private void startAll(CommandInterpreter ci){	
		for(Bundle bundle : Activator.getContext().getBundles()){
			if(!isFragment(bundle) && bundle.getState()!=Bundle.ACTIVE){
				try {				
					bundle.start();
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
		}
	}
	
	private void installAll(CommandInterpreter ci){
		ci.println("loading bundles...");
		ci.execute("runfile bundles");
		ci.println("loading bundles done.");
	}
	
	public void _installbundle(CommandInterpreter ci){
		String location = ci.nextArgument();
		System.out.println("Installing: "+location);
		try {		
			Activator.getContext().installBundle(location);								
		} catch (BundleException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean isFragment(Bundle bundle) {
		return !(bundle.getHeaders().get(org.osgi.framework.Constants.FRAGMENT_HOST) == null);
	}

	@Override
	public void stop() {
		System.err.println("STOPPING INVOKED!!");
		this.lastCI.execute("stopschedule");
		stopAll(this.lastCI);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		installAll(this.lastCI);
		if(runs>0){
			runs--;
			_runtest(lastCI);
		}
		
	}

}
