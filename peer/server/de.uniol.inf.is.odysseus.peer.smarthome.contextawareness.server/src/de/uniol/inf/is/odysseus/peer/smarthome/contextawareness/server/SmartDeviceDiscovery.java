package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.SmartHomeServerPlugIn;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.server.advertisement.SmartDeviceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceRequestMessage;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.message.SmartDeviceResponseMessage;

public class SmartDeviceDiscovery implements
		IPeerCommunicatorListener, IAdvertisementDiscovererListener {

	private static SmartDeviceDiscovery instance;
	private long GARBAGE_COLLECTOR_REPEAT_TIME_MS = 30000;
	private long SMART_DEVICE_AVAILABLE_AFTER_LAST_HEARTBEAT_MS = 30000;

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private Map<String, ASmartDevice> foundSmartDevices = Maps.newHashMap();
	private Map<String, Long> smartDevicesHeartBeat = Maps.newHashMap();
	private final List<ISmartDeviceDictionaryListener> listeners = Lists
			.newArrayList();

	private long lastGarbageCollectorTime = 0;

	private static ArrayList<PeerID> foundPeerIDs = new ArrayList<PeerID>();
	private static Collection<PeerID> refreshing = Lists.newLinkedList();

	public SmartDeviceDiscovery() {
		cleanupAsync();
		refreshFoundPeerIDsAsync();
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

	/************************************
	 * IAdvertisementDiscovererListener
	 */
	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if (advertisement != null
				&& advertisement instanceof SmartDeviceAdvertisement
				&& advertisement.getAdvType().equals(
						SmartDeviceAdvertisement.getAdvertisementType())) {
			// TODO:
			SmartDeviceAdvertisement adv = (SmartDeviceAdvertisement) advertisement;
			getSmartDeviceInformations(adv);
		}
	}

	@Override
	public void updateAdvertisements() {
	}

	void getSmartDeviceInformations(SmartDeviceAdvertisement adv) {
		//LOG.debug("getSmartDeviceInformations peer:"+adv.getPeerName());
		if (adv != null && isPeerIdAvailable(adv.getPeerID())) {
			try {
				SmartDeviceRequestMessage smartDevRequest = new SmartDeviceRequestMessage(
						"request");

				SmartDevicePublisher.getInstance().getPeerCommunicator()
						.send(adv.getPeerID(), smartDevRequest);
			} catch (PeerCommunicationException e) {
				LOG.error(e.getMessage() + " PeerID:"
						+ adv.getPeerID().intern().toString(), e);
			}
		}
	}

	/****************************
	 * IPeerCommunicatorListener
	 */
	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof SmartDeviceRequestMessage) {
			// if a peer request information for this smart device, then
			// send out this information
			try {
				SmartDeviceResponseMessage smartDeviceResponse = new SmartDeviceResponseMessage();
				smartDeviceResponse.setSmartDevice(SmartDevicePublisher
						.getInstance().getLocalSmartDevice());

				try {
					SmartDevicePublisher.getInstance().getPeerCommunicator()
							.send(senderPeer, smartDeviceResponse);
				} catch (PeerCommunicationException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}
		} else if (message instanceof SmartDeviceResponseMessage) {
			SmartDeviceResponseMessage smartDeviceResponse = (SmartDeviceResponseMessage) message;
			ASmartDevice smartDevice = smartDeviceResponse.getSmartDevice();

			if (!SmartDevicePublisher.getInstance().isLocalPeer(senderPeer) && !isLocalSmartDevice(smartDevice)) {

				if (smartDevice != null) {
					SmartDeviceDiscovery.getInstance()
							.addSmartDevice(smartDevice);
				} else {
					LOG.debug("The smart device response is null!!! Are all classes in SmartDevice serializable?");
				}
			}else if(!SmartDevicePublisher.getInstance().isLocalPeer(senderPeer)){
				//TODO: update local smart device:
				LOG.debug("TODO: update local smart device: "+ smartDevice.getPeerName());
				
				SmartDevicePublisher.getInstance().getLocalSmartDevice().overwriteWith(smartDevice);
			}
		} else if (message instanceof SmartDeviceMessage) {
			LOG.debug("receivedMessage instanceof SmartDeviceMessage");
		}
	}

	private static boolean isLocalSmartDevice(ASmartDevice smartDevice) {
		if(smartDevice.getPeerID().equals(SmartDevicePublisher.getInstance().getLocalSmartDevice().getPeerID())){
			return true;
		}
		return false;
	}

	public void addSmartDevice(ASmartDevice newSmartDevice) {
		if (!getFoundSmartDevices().containsKey(newSmartDevice.getPeerID())) {
			// LOG.debug("Add new SmartDevice");
			// Add new SmartDevice
			try {
				String smartDevicePeerID = newSmartDevice.getPeerID().intern()
						.toString();
				if (smartDevicePeerID != null && !smartDevicePeerID.equals("")) {
					synchronized (getFoundSmartDevices()) {
						getFoundSmartDevices()
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
			//LOG.debug("Update existing SmartDevice");
			// Update existing SmartDevice
			ASmartDevice existingSmartDevice = getFoundSmartDevices().get(
					newSmartDevice.getPeerID());

			synchronized (getFoundSmartDevices()) {
				existingSmartDevice.overwriteWith(newSmartDevice);
				refreshHeartBeat(existingSmartDevice);
			}
			fireSmartDeviceUpdatedEvent(existingSmartDevice);
		}
	}

	private synchronized void removeSmartDevice(String smartDevicePeerID) {
		// LOG.debug("removeSmartDevice peerID:" + smartDevicePeerID);

		ASmartDevice smartDeviceToRemove = getFoundSmartDevices().get(
				smartDevicePeerID);

		getFoundSmartDevices().remove(smartDevicePeerID);
		getSmartDevicesHeartBeat().remove(smartDevicePeerID);

		fireSmartDeviceRemovedEvent(smartDeviceToRemove);
	}

	private synchronized void refreshHeartBeat(ASmartDevice smartDevice) {
		String smartDevicePeerID = smartDevice.getPeerID();
		synchronized (this.smartDevicesHeartBeat) {
			Long currentTime = new Long(System.currentTimeMillis());
			getSmartDevicesHeartBeat().put(smartDevicePeerID, currentTime);
		}
	}

	public void addListener(ISmartDeviceDictionaryListener listener) {
		LOG.debug(" addListener");
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(ISmartDeviceDictionaryListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected final void fireSmartDeviceAddEvent(ASmartDevice smartDevice) {
		synchronized (listeners) {
			for (ISmartDeviceDictionaryListener listener : listeners) {
				try {
					listener.smartDeviceAdded(SmartDevicePublisher.getInstance(),
							smartDevice);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invokinf smart device dictionary listener",
							t);
				}
			}
		}
	}

	protected final void fireSmartDeviceRemovedEvent(ASmartDevice smartDevice) {
		synchronized (listeners) {
			for (ISmartDeviceDictionaryListener listener : listeners) {
				try {
					LOG.debug("fireSmartDeviceRemovedEvent smartDevice:"
							+ smartDevice.getPeerName() + " listener:"
							+ listener.getClass());
					listener.smartDeviceRemoved(
							SmartDevicePublisher.getInstance(), smartDevice);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invokinf smart device dictionary listener",
							t);
				}
			}
		}
	}

	protected final void fireSmartDeviceUpdatedEvent(ASmartDevice smartDevice) {
		synchronized (listeners) {
			for (ISmartDeviceDictionaryListener listener : listeners) {
				try {
					listener.smartDeviceUpdated(
							SmartDevicePublisher.getInstance(), smartDevice);
				} catch (Throwable t) {
					LOG.error(
							"Exception during invokinf smart device dictionary listener",
							t);
				}
			}
		}
	}

	public Map<String, ASmartDevice> getFoundSmartDevices() {
		// cleanup();
		return foundSmartDevices;
	}
	
	public ArrayList<ASmartDevice> getFoundSmartDeviceList() {
		ArrayList<ASmartDevice> list = new ArrayList<ASmartDevice>();
		if(foundSmartDevices!=null)
			list.addAll(foundSmartDevices.values());
		return list;
	}

	public synchronized void setSmartDevices(
			Map<String, ASmartDevice> smartDevices) {
		this.foundSmartDevices = smartDevices;
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

	public static SmartDeviceDiscovery getInstance() {
		if (instance == null) {
			instance = new SmartDeviceDiscovery();
		}
		return instance;
	}

	private static boolean isPeerIdAvailable(PeerID peerID) {
		if (peerID == null) {
			// LOG.debug("peerID==null");
			return false;
		} else if (SmartDevicePublisher.isLocalPeer(peerID.intern().toString())) {
			// LOG.debug("peerID is LocalPeer: " + peerID.intern().toString());
			return false;
		} else if (foundPeerIDs == null 
				|| peerID.intern() == null
				|| peerID.intern().toString().isEmpty()) {
			// LOG.debug("peerID is null or empty");
			return false;
		} else if (!isFoundInFoundPeers(peerID)) {
			@SuppressWarnings("unused")
			String debugMessage = "is not in the foundPeerID's peerID:";

			try {
				debugMessage += "peerID: " + peerID.intern().toString();
			} catch (Exception ex) {
				debugMessage += "peerID: null";
			}

			// LOG.debug(debugMessage);
			return false;
		} else {
			return true;
		}
	}

	private static boolean isFoundInFoundPeers(PeerID peerID) {
		ArrayList<PeerID> foundPeerIDArray = getFoundPeerIDs();
		for (PeerID foundPeerID : foundPeerIDArray) {
			if (foundPeerID.intern().toString()
					.equals(peerID.intern().toString())) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<PeerID> getFoundPeerIDs() {
		ArrayList<PeerID> foundPeerIDArray = Lists.newArrayList();
		foundPeerIDArray.addAll(SmartHomeServerPlugIn.getPeerDictionary()
				.getRemotePeerIDs());
		return foundPeerIDArray;
	}

	private static void refreshFoundPeerIDsAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					waitForP2PDictionary();

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
					try {
						refreshFoundPeerIDs();
					} catch (Exception ex) {
						LOG.error(ex.getMessage(), ex);
					}
				}
			}
		});
		t.setName("SmartHomeServerPlugIn refresh foundPeerIDs Thread");
		t.setDaemon(true);
		t.start();
	}

	public static void refreshFoundPeerIDs() {
		Collection<PeerID> foundPeerIDsCopy = null;
		if (foundPeerIDs != null
				&& SmartHomeServerPlugIn.getPeerDictionary() != null
				&& SmartHomeServerPlugIn.getPeerDictionary().getRemotePeerIDs() != null) {
			synchronized (foundPeerIDs) {
				foundPeerIDs.clear();
				foundPeerIDs.addAll(SmartHomeServerPlugIn.getPeerDictionary()
						.getRemotePeerIDs());
				foundPeerIDsCopy = Lists.newArrayList(foundPeerIDs);
			}

			if (foundPeerIDsCopy != null) {
				for (final PeerID remotePeerID : foundPeerIDsCopy) {
					synchronized (refreshing) {
						if (refreshing.contains(remotePeerID)) {
							continue;
						}

						refreshing.add(remotePeerID);
					}
				}
			}
		}

		// printFoundPeerIDs(foundPeerIDsCopy);
	}

	@SuppressWarnings("unused")
	private static void printFoundPeerIDs(Collection<PeerID> foundPeerIDsCopy) {
		if (foundPeerIDsCopy != null) {
			LOG.debug("foundPeerIDs size:" + foundPeerIDsCopy.size());
			for (PeerID peerID : foundPeerIDsCopy) {
				LOG.debug("peerID: " + peerID.intern().toString());
			}
		} else {
			LOG.debug("foundPeerIDsCopy==null");
		}
	}

	@SuppressWarnings("unused")
	private static void showActualImportedSourcesAsync() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				waitForP2PNetworkManager();
				waitForP2PDictionary();
				waitForPQLGenerator();

				UnmodifiableIterator<SourceAdvertisement> iteratorSources = SmartHomeServerPlugIn
						.getP2PDictionary().getImportedSources().iterator();
				while (iteratorSources.hasNext()) {
					SourceAdvertisement sourceAdv = iteratorSources.next();

					LOG.debug("SourceAdv actual imported: "
							+ sourceAdv.getName());
				}
			}
		});
		thread.setName("SmartHome showActualImportedSourcesAsync Thread");
		thread.setDaemon(true);
		thread.start();
	}

	private static void waitForP2PDictionary() {
		while (SmartHomeServerPlugIn.getP2PDictionary() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void waitForP2PNetworkManager() {
		while (SmartHomeServerPlugIn.getP2PNetworkManager() == null
				|| !SmartHomeServerPlugIn.getP2PNetworkManager().isStarted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private static void waitForPQLGenerator() {
		while (SmartHomeServerPlugIn.getPQLGenerator() == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public static PeerID getPeerIDOfString(String peerID) {
		for(PeerID peer : getFoundPeerIDs()){
			if(peer.intern().toString().equals(peerID)){
				return peer;
			}
		}

		return null;
	}
}