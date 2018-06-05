package de.uniol.inf.is.odysseus.iql.basic.ui.service;

import java.util.Collection;
import java.util.HashSet;

public class IQLUiServiceBinding {

	private Collection<IIQLUiService> services = new HashSet<>();
	private Collection<Listener> listeners = new HashSet<>();

	private static IQLUiServiceBinding instance = new IQLUiServiceBinding();
	
	public static IQLUiServiceBinding getInstance() {
		return instance;
	}
	
	public static void bindIQLUiService(IIQLUiService service) {
		getInstance().onIQLUiServiceAdded(service);
	}
	
	public void onIQLUiServiceAdded(IIQLUiService iqlService) {
		services.add(iqlService);
		for (Listener listener : listeners) {
			listener.onServiceAdded(iqlService);
		}
	}
	
	public static void unbindIQLUiService(IIQLUiService service) {
		getInstance().onIQLUiServiceRemoved(service);
	}
	
	public void onIQLUiServiceRemoved(IIQLUiService iqlService) {
		services.remove(iqlService);
		for (Listener listener : listeners) {
			listener.onServiceRemoved(iqlService);
		}
		
	}
	
	public Collection<IIQLUiService> getServices() {
		return services;
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	
	public interface Listener {
		void onServiceAdded(IIQLUiService service);
		void onServiceRemoved(IIQLUiService service);
	}
	
}
