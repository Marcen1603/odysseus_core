package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.SmartDeviceResponseMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;

public class SmartDeviceDictionaryDiscovery implements
		IPeerCommunicatorListener {

	private static SmartDeviceDictionaryDiscovery instance;
	private long GARBAGE_COLLECTOR_REPEAT_TIME_MS = 30000;
	private long SMART_DEVICE_AVAILABLE_AFTER_LAST_HEARTBEAT_MS = 30000;

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartDeviceDictionaryDiscovery.class);
	private Map<String, SmartDevice> smartDevices = Maps.newHashMap();
	private Map<String, Long> smartDevicesHeartBeat = Maps.newHashMap();
	private final List<ISmartDeviceDictionaryListener> listeners = Lists
			.newArrayList();

	private long lastGarbageCollectorTime = 0;
	private IPeerCommunicator peerCommunicator;

	public SmartDeviceDictionaryDiscovery() {
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

						// LOG.debug("cleanup() heartBeat:" + heartBeat +
						// " delta:" + delta);

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
		if (!getSmartDevices().containsKey(newSmartDevice.getPeerID())) {
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
					newSmartDevice.getPeerID());

			synchronized (getSmartDevices()) {
				existingSmartDevice.overwriteWith(newSmartDevice);
				refreshHeartBeat(existingSmartDevice);
			}
			fireSmartDeviceUpdatedEvent(existingSmartDevice);
		}
	}

	private synchronized void removeSmartDevice(String smartDevicePeerID) {
		// LOG.debug("removeSmartDevice peerID:" + smartDevicePeerID);

		SmartDevice smartDeviceToRemove = getSmartDevices().get(
				smartDevicePeerID);

		getSmartDevices().remove(smartDevicePeerID);
		getSmartDevicesHeartBeat().remove(smartDevicePeerID);

		fireSmartDeviceRemovedEvent(smartDeviceToRemove);
	}

	private synchronized void refreshHeartBeat(SmartDevice smartDevice) {
		String smartDevicePeerID = smartDevice.getPeerID();
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

	public static SmartDeviceDictionaryDiscovery getInstance() {
		if (instance == null) {
			instance = new SmartDeviceDictionaryDiscovery();
		}
		return instance;
	}

	// ///////////////////////////
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof SmartDeviceRequestMessage) {
			// if a peer request information for this smart device, then
			// send out this information
			try {
				SmartDeviceResponseMessage smartDeviceResponse = new SmartDeviceResponseMessage();
				smartDeviceResponse.setSmartDevice(SmartHomeServerPlugIn
						.getLocalSmartDevice());

				try {
					SmartHomeServerPlugIn.getPeerCommunicator().send(
							senderPeer, smartDeviceResponse);
				} catch (PeerCommunicationException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}
		} else if (message instanceof SmartDeviceResponseMessage) {
			SmartDeviceResponseMessage smartDeviceResponse = (SmartDeviceResponseMessage) message;

			if (!SmartHomeServerPlugIn.isLocalPeer(senderPeer)) {
				SmartDevice smartDevice = smartDeviceResponse.getSmartDevice();

				if (smartDevice != null) {
					SmartDeviceDictionaryDiscovery.getInstance()
							.addSmartDevice(smartDevice);
				} else {
					LOG.debug("The smart device response is null!!!");
				}
			}
		} else if (message instanceof SmartDeviceMessage) {
			LOG.debug("receivedMessage instanceof SmartDeviceMessage");
		}
	}

	public void bindPeerCommunicator(IPeerCommunicator _peerCommunicator) {
		peerCommunicator = _peerCommunicator;
		_peerCommunicator.registerMessageType(SmartDeviceRequestMessage.class);
		_peerCommunicator.registerMessageType(SmartDeviceResponseMessage.class);

		_peerCommunicator.addListener(this, SmartDeviceRequestMessage.class);
		_peerCommunicator.addListener(this, SmartDeviceResponseMessage.class);

	}

	public void unbindPeerCommunicator(IPeerCommunicator _peerCommunicator) {
		if (peerCommunicator == _peerCommunicator) {
			_peerCommunicator.removeListener(this,
					SmartDeviceRequestMessage.class);
			_peerCommunicator.removeListener(this,
					SmartDeviceResponseMessage.class);

			// peerCommunicator.unregisterMessageType(SmartDeviceRequestMessage.class);
			// peerCommunicator.unregisterMessageType(SmartDeviceResponseMessage.class);
			peerCommunicator = null;
		}
	}
}
