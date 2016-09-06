package de.uniol.inf.is.odysseus.iql.basic.service;

import java.util.Collection;
import java.util.HashSet;

public class IQLServiceBinding {

	private Collection<IIQLService> services = new HashSet<>();
	private Collection<Listener> listeners = new HashSet<>();

	private static IQLServiceBinding instance = new IQLServiceBinding();
	
	public static IQLServiceBinding getInstance() {
		return instance;
	}
	
	public static void bindIQLService(IIQLService service) {
		getInstance().onIQLServiceAdded(service);
	}
	
	public void onIQLServiceAdded(IIQLService iqlService) {
		services.add(iqlService);
		for (Listener listener : listeners) {
			listener.onServiceAdded(iqlService);
		}
	}
	
	public static void unbindIQLService(IIQLService service) {
		getInstance().onIQLServiceRemoved(service);
	}
	
	public void onIQLServiceRemoved(IIQLService iqlService) {
		services.remove(iqlService);
		for (Listener listener : listeners) {
			listener.onServiceRemoved(iqlService);
		}
		
	}
	
	public Collection<IIQLService> getServices() {
		return services;
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	
	public interface Listener {
		void onServiceAdded(IIQLService service);
		void onServiceRemoved(IIQLService service);
	}
	
}
