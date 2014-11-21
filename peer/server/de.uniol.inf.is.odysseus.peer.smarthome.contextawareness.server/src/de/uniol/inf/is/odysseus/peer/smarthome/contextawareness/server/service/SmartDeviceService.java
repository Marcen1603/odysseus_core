package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.FieldDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.IFieldDeviceListener;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceServerDictionaryDiscovery;

public class SmartDeviceService implements ISmartDeviceService {
		private static SmartDeviceService instance;
		private ArrayList<IFieldDeviceListener> smartDeviceListener;

		// //////
		// called by OSGi-DS
		public void activate() {
			instance = this;
		}

		// called by OSGi-DS
		public void deactivate() {
			instance = null;
		}

		// called by OSGi-DS
		public void bindListener(IFieldDeviceListener serv) {
			addSmartDeviceListener(serv);
		}

		// called by OSGi-DS
		public void unbindListener(IFieldDeviceListener serv) {
			removeSmartDeviceListener(serv);
		}

		@Override
		public void addSmartDeviceListener(IFieldDeviceListener listener) {
			getSmartDeviceListener().add(listener);
		}
		
		@Override
		public void removeSmartDeviceListener(IFieldDeviceListener listener) {
			getSmartDeviceListener().remove(listener);
		}
		private ArrayList<IFieldDeviceListener> getSmartDeviceListener() {
			if(smartDeviceListener==null){
				smartDeviceListener = new ArrayList<IFieldDeviceListener>();
			}
			return smartDeviceListener;
		}
		
		@SuppressWarnings("unused")
		private void fireFieldDeviceConnected(FieldDevice device) {
			/*
			for(IFieldDeviceListener listener : getSmartDeviceListener()){
				listener.fieldDeviceConnected(getLocalSmartDevice(), device);
			}
			*/
		}
		@SuppressWarnings("unused")
		private void fireFieldDeviceRemoved(FieldDevice device) {
			/*
			for(IFieldDeviceListener listener : getSmartDeviceListener()){
				listener.fieldDeviceRemoved(getLocalSmartDevice(), device);
			}
			*/
		}
		@SuppressWarnings("unused")
		private void fireReadyStateChanged(boolean state) {
			/*
			for(IFieldDeviceListener listener : getSmartDeviceListener()){
				listener.readyStateChanged(getLocalSmartDevice(), state);
			}
			*/
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
			return SmartDevicePublisher.getInstance().getLocalSmartDevice();
		}

		@Override
		public SmartDevicePublisher getSmartDeviceServer() {
			return SmartDevicePublisher.getInstance();
		}

		@Override
		public SmartDeviceServerDictionaryDiscovery getSmartDeviceServerDictionaryDiscovery() {
			return SmartDeviceServerDictionaryDiscovery.getInstance();
		}
}
