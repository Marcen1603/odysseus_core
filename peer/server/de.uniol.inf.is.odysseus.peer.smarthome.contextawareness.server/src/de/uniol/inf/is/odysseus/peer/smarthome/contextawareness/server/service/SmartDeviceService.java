package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.service;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDevicePublisher;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.SmartDeviceDictionaryDiscovery;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ISmartDeviceListener;

public class SmartDeviceService implements ISmartDeviceService {
		private static SmartDeviceService instance;
		private ArrayList<ISmartDeviceListener> smartDeviceListener;

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

		public static boolean isActivated() {
			return instance != null;
		}
		
		public static SmartDeviceService getInstance() {
			if(instance==null){
				instance = new SmartDeviceService();
			}
			return instance;
		}

		@Override
		public ASmartDevice getLocalSmartDevice() {
			return SmartDevicePublisher.getInstance().getLocalSmartDevice();
		}

		@Override
		public SmartDevicePublisher getSmartDevicePublisher() {
			return SmartDevicePublisher.getInstance();
		}

		@Override
		public SmartDeviceDictionaryDiscovery getSmartDeviceDictionaryDiscovery() {
			return SmartDeviceDictionaryDiscovery.getInstance();
		}
}
