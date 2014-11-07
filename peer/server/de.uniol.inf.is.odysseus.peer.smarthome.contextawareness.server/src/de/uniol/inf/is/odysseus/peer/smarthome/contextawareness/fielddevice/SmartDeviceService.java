package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServer;

public class SmartDeviceService implements ISmartDeviceService {
		private static SmartDeviceService instance;
		private ArrayList<ISmartDeviceListener> smartDeviceListener;

		// //////
		// called by OSGi-DS
		public void activate() {
			instance = this;
			System.out.println("SmartDeviceService activate");
		}

		// called by OSGi-DS
		public void deactivate() {
			instance = null;
			System.out.println("SmartDeviceService activate");
		}

		// called by OSGi-DS
		public void bindListener(ISmartDeviceListener serv) {
			addSmartDeviceListener(serv);
		}

		// called by OSGi-DS
		public void unbindListener(ISmartDeviceListener serv) {
			removeSmartDeviceListener(serv);
		}

		@Override
		public void addSmartDeviceListener(ISmartDeviceListener listener) {
			getSmartDeviceListener().add(listener);
		}
		
		@Override
		public void removeSmartDeviceListener(ISmartDeviceListener listener) {
			getSmartDeviceListener().remove(listener);
		}
		private ArrayList<ISmartDeviceListener> getSmartDeviceListener() {
			if(smartDeviceListener==null){
				smartDeviceListener = new ArrayList<ISmartDeviceListener>();
			}
			return smartDeviceListener;
		}
		
		@SuppressWarnings("unused")
		private void fireFieldDeviceConnected(FieldDevice device) {
			for(ISmartDeviceListener listener : getSmartDeviceListener()){
				listener.fieldDeviceConnected(getLocalSmartDevice(), device);
			}
		}
		@SuppressWarnings("unused")
		private void fireFieldDeviceRemoved(FieldDevice device) {
			for(ISmartDeviceListener listener : getSmartDeviceListener()){
				listener.fieldDeviceRemoved(getLocalSmartDevice(), device);
			}
		}
		@SuppressWarnings("unused")
		private void fireReadyStateChanged(boolean state) {
			for(ISmartDeviceListener listener : getSmartDeviceListener()){
				listener.readyStateChanged(getLocalSmartDevice(), state);
			}
		}

		public static boolean isActivated() {
			return instance != null;
		}
		
		public static SmartDeviceService getInstance() {
			if(instance==null){
				instance = new SmartDeviceService();
			}
			return instance;
		}

		public ASmartDevice getLocalSmartDevice() {
			return SmartDeviceServer.getInstance().getLocalSmartDevice();
		}
}
