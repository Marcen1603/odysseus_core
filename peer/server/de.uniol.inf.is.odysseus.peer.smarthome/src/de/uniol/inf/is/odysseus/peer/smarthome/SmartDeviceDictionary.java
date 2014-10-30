package de.uniol.inf.is.odysseus.peer.smarthome;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;

public class SmartDeviceDictionary {

	private long GARBAGE_COLLECTOR_REPEAT_TIME_MS = 30000;
	private long SMART_DEVICE_AVAILABLE_AFTER_LAST_HEARTBEAT_MS = 30000;

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartDeviceDictionary.class);
	private Map<String, SmartDevice> smartDevices = Maps.newHashMap();
	private Map<String, Long> smartDevicesHeartBeat = Maps.newHashMap();
	private final List<ISmartDeviceDictionaryListener> listeners = Lists
			.newArrayList();

	private long lastGarbageCollectorTime = 0;

	public SmartDeviceDictionary() {
		cleanupAsync();
	}

	protected void cleanupAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(GARBAGE_COLLECTOR_REPEAT_TIME_MS);
					} catch (InterruptedException e) {
					}

					cleanup();
				}
			}
		});
		t.setName("SmartDeviceDictionary cleanup thread");
		t.setDaemon(true);
		t.start();
	}

	private void cleanup() {
		try {
			long deltaGarbage = System.currentTimeMillis()
					- lastGarbageCollectorTime;

			if (deltaGarbage > GARBAGE_COLLECTOR_REPEAT_TIME_MS) {
				lastGarbageCollectorTime = System.currentTimeMillis();

				synchronized (getSmartDevicesHeartBeat()) {
					for (Entry<String, Long> smartDeviceEntry : getSmartDevicesHeartBeat()
							.entrySet()) {
						String peerId = smartDeviceEntry.getKey();
						Long heartBeat = smartDeviceEntry.getValue();

						long delta = System.currentTimeMillis()
								- SMART_DEVICE_AVAILABLE_AFTER_LAST_HEARTBEAT_MS;

						//LOG.debug("cleanup() heartBeat:" + heartBeat		+ " delta:" + delta);

						if (heartBeat < delta) {
							removeSmartDevice(peerId);
						}
					}
				}
			} else if (lastGarbageCollectorTime == 0) {
				lastGarbageCollectorTime = System.currentTimeMillis();
			}
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	public void addSmartDevice(SmartDevice newSmartDevice) {
		if (!getSmartDevices().containsKey(newSmartDevice.getPeerIDString())) {
			// Add new SmartDevice
			try {
				String smartDevicePeerID = newSmartDevice.getPeerID().intern()
						.toString();
				if (smartDevicePeerID != null && !smartDevicePeerID.equals("")) {
					synchronized (getSmartDevices()) {
						getSmartDevices()
								.put(smartDevicePeerID, newSmartDevice);
						refreshHeartBeat(newSmartDevice);
					}
					fireSmartDeviceAddEvent(newSmartDevice);
				} else {

				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		} else {
			// Update existing SmartDevice
			SmartDevice existingSmartDevice = getSmartDevices().get(
					newSmartDevice.getPeerIDString());

			synchronized (getSmartDevices()) {
				existingSmartDevice.overwriteWith(newSmartDevice);
				refreshHeartBeat(existingSmartDevice);
			}
			fireSmartDeviceUpdatedEvent(existingSmartDevice);
		}
	}

	private synchronized void removeSmartDevice(String smartDevicePeerID) {
		//LOG.debug("removeSmartDevice peerID:" + smartDevicePeerID);

		SmartDevice smartDeviceToRemove = getSmartDevices().get(
				smartDevicePeerID);

		getSmartDevices().remove(smartDevicePeerID);
		getSmartDevicesHeartBeat().remove(smartDevicePeerID);

		fireSmartDeviceRemovedEvent(smartDeviceToRemove);
	}

	private synchronized void refreshHeartBeat(SmartDevice smartDevice) {
		String smartDevicePeerID = smartDevice.getPeerIDString();
		synchronized (this.smartDevicesHeartBeat) {
			Long currentTime = new Long(System.currentTimeMillis());
			getSmartDevicesHeartBeat().put(smartDevicePeerID, currentTime);
		}
	}

	public void addListener(ISmartDeviceDictionaryListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(ISmartDeviceDictionaryListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected final void fireSmartDeviceAddEvent(SmartDevice smartDevice) {
		synchronized (listeners) {
			for (ISmartDeviceDictionaryListener listener : listeners) {
				try {
					listener.smartDeviceAdded(this, smartDevice);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invokinf smart device dictionary listener",
							t);
				}
			}
		}
	}

	protected final void fireSmartDeviceRemovedEvent(SmartDevice smartDevice) {
		synchronized (listeners) {
			for (ISmartDeviceDictionaryListener listener : listeners) {
				try {
					LOG.debug("fireSmartDeviceRemovedEvent");
					listener.smartDeviceRemoved(this, smartDevice);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invokinf smart device dictionary listener",
							t);
				}
			}
		}
	}

	protected final void fireSmartDeviceUpdatedEvent(SmartDevice smartDevice) {
		synchronized (listeners) {
			for (ISmartDeviceDictionaryListener listener : listeners) {
				try {
					listener.smartDeviceUpdated(this, smartDevice);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invokinf smart device dictionary listener",
							t);
				}
			}
		}
	}

	public Map<String, SmartDevice> getSmartDevices() {
		// cleanup();
		return smartDevices;
	}

	public synchronized void setSmartDevices(
			Map<String, SmartDevice> smartDevices) {
		this.smartDevices = smartDevices;
	}

	public synchronized Map<String, Long> getSmartDevicesHeartBeat() {
		return smartDevicesHeartBeat;
	}

	public synchronized void setSmartDevicesHeartBeat(
			Map<String, Long> smartDevicesHeartBeat) {
		synchronized (this.smartDevicesHeartBeat) {
			this.smartDevicesHeartBeat = smartDevicesHeartBeat;
		}
	}
}
