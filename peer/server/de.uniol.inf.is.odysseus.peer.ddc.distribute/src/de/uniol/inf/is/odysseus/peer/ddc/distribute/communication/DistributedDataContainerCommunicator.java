package de.uniol.inf.is.odysseus.peer.ddc.distribute.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerChangeAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerRequestAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementGenerator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerChange;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.message.DDCMessage;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.message.DDCRequest;

/**
 * Listener for DDCAdvertisements. The Changes from Advertisements will be
 * written to DDC
 * 
 * @author ChrisToenjesDeye, Michael Brand
 * 
 */
public class DistributedDataContainerCommunicator implements
		IAdvertisementDiscovererListener, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerCommunicator.class);

	private static IDistributedDataContainer ddc;

	private IP2PNetworkManager p2pNetworkManager;
	private List<UUID> receivedDDCAdvertisements = new ArrayList<UUID>();
	private List<UUID> receivedRequests = new ArrayList<UUID>();
	private List<UUID> receivedMessages = new ArrayList<UUID>();
	private static IPeerCommunicator peerCommunicator;

	// called by OSGi-DS
	public static void bindDDC(IDistributedDataContainer ddc) {
		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		DistributedDataContainerCommunicator.ddc = ddc;
		DistributedDataContainerCommunicator.LOG.debug("Bound {} as a DDC", ddc
				.getClass().getSimpleName());

	}

	// called by OSGi-DS
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (DistributedDataContainerCommunicator.ddc == ddc) {
			DistributedDataContainerCommunicator.ddc = null;
			DistributedDataContainerCommunicator.LOG.debug(
					"Unbound {} as a DDC", ddc.getClass().getSimpleName());
		}
	}

	// called by OSGi-DS
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addAdvertisementListener(this);
	}

	// called by OSGi-DS
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager.removeAdvertisementListener(this);
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Bound Peer Communicator.");
		peerCommunicator = serv;
		peerCommunicator.registerMessageType(DDCRequest.class);
		peerCommunicator.registerMessageType(DDCMessage.class);

		peerCommunicator.addListener(this, DDCRequest.class);
		peerCommunicator.addListener(this, DDCMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, DDCRequest.class);
			peerCommunicator.removeListener(this, DDCRequest.class);

			peerCommunicator.unregisterMessageType(DDCRequest.class);
			peerCommunicator.unregisterMessageType(DDCMessage.class);

			peerCommunicator = null;
		}
	}

	private static IPeerDictionary peerDictionary;

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	/**
	 * Processes DDCAdvertisements and writes changes to DDC. If
	 * DDCAdvertisement is already processed it will be ignored
	 */
	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		if (advertisement instanceof DistributedDataContainerAdvertisement) {
			DistributedDataContainerAdvertisement ddcAdvertisement = (DistributedDataContainerAdvertisement) advertisement;
			// check if listener already received this ddc advertisement
			UUID advId = ddcAdvertisement.getAdvertisementUid();
			PeerID peerId = ddcAdvertisement.getInitiatingPeerId();
			if (!receivedDDCAdvertisements.contains(advId)
					&& !peerId.equals(p2pNetworkManager.getLocalPeerID())) {
				LOG.debug("Got new DDC advertisment.");
				receivedDDCAdvertisements.add(advId);
				DDCRequest request = new DDCRequest();
				request.setAdvertisementId(advId);
				try {
					peerCommunicator.send(peerId, request);
				} catch (PeerCommunicationException e) {
					LOG.error("Could not send DDC request!", e);
				}
			}
		} else if (advertisement instanceof DistributedDataContainerChangeAdvertisement) {
			DistributedDataContainerChangeAdvertisement ddcAdvertisement = (DistributedDataContainerChangeAdvertisement) advertisement;
			// check if listener already received this ddc advertisement
			UUID advId = ddcAdvertisement.getAdvertisementUid();
			PeerID peerId = ddcAdvertisement.getInitiatingPeerId();
			if (!receivedDDCAdvertisements.contains(advId)
					&& !peerId.equals(p2pNetworkManager.getLocalPeerID())) {
				LOG.debug("Got new DDC change advertisment.");
				receivedDDCAdvertisements.add(advId);
				DDCRequest request = new DDCRequest();
				request.setAdvertisementId(advId);
				request.setChangeRequest(true);
				try {
					peerCommunicator.send(peerId, request);
				} catch (PeerCommunicationException e) {
					LOG.error("Could not send DDC change request!", e);
				}
			}
		} else if (advertisement instanceof DistributedDataContainerRequestAdvertisement) {
			DistributedDataContainerRequestAdvertisement ddcAdvertisement = (DistributedDataContainerRequestAdvertisement) advertisement;
			// check if listener already received this ddc advertisement
			UUID advId = ddcAdvertisement.getAdvertisementUid();
			PeerID peerId = ddcAdvertisement.getInitiatingPeerId();
			if (!receivedDDCAdvertisements.contains(advId)
					&& !peerId.equals(p2pNetworkManager.getLocalPeerID())) {
				LOG.debug("Got new DDC request advertisment.");
				receivedDDCAdvertisements.add(advId);
				DistributedDataContainerAdvertisementGenerator.getInstance()
						.disableListeningForChanges();
				DDCMessage response = new DDCMessage();
				response.setAdvertisementId(advId);
				List<DDCEntry> entries_added = Lists.newArrayList();
				for (DDCKey key : ddc.getKeys()) {
					try {
						entries_added.add(ddc.get(key));
					} catch (MissingDDCEntryException e) {
						LOG.error("Unknown DDC key: {}", key);
					}
				}
				response.setEntriesAdded(entries_added);
				try {
					peerCommunicator.send(peerId, response);
				} catch (PeerCommunicationException e) {
					LOG.error("Could not send DDC message!", e);
				}
				// enable listening for changes after changes are written to ddc
				DistributedDataContainerAdvertisementGenerator.getInstance()
						.enableListeningForChanges();
			}
		}
	}

	@Override
	public void updateAdvertisements() {
		// no operation
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator,
			PeerID senderPeer, IMessage message) {
		if (message instanceof DDCRequest) {
			DDCRequest ddcRequest = (DDCRequest) message;
			UUID advId = ddcRequest.getAdvertisementId();
			if (!receivedRequests.contains(advId)) {
				LOG.debug("Got new DDC request.");
				receivedRequests.add(advId);
				DistributedDataContainerAdvertisementGenerator.getInstance()
						.disableListeningForChanges();
				DDCMessage response = new DDCMessage();
				response.setAdvertisementId(advId);
				List<DDCEntry> entries_added = Lists.newArrayList();
				if (ddcRequest.isChangeRequest()) {
					List<DDCEntry> entries_removed = Lists.newArrayList();
					Set<DistributedDataContainerChange> changes = DistributedDataContainerAdvertisementGenerator
							.getInstance().getDDCChanges(advId);
					for (DistributedDataContainerChange change : changes) {
						switch (change.getDdcChangeType()) {
						case ddcEntryAdded:
							entries_added.add(change.getDdcEntry());
							break;
						case ddcEntryRemoved:
							entries_removed.add(change.getDdcEntry());
							break;
						default:
							LOG.error("Unknown DDC change type: {}",
									change.getDdcChangeType());
						}
					}
					response.setEntriesRemoved(entries_removed);
				} else {
					for (DDCKey key : ddc.getKeys()) {
						try {
							entries_added.add(ddc.get(key));
						} catch (MissingDDCEntryException e) {
							LOG.error("Unknown DDC key: {}", key);
						}
					}
				}
				response.setEntriesAdded(entries_added);
				try {
					peerCommunicator.send(senderPeer, response);
				} catch (PeerCommunicationException e) {
					LOG.error("Could not send DDC message!", e);
				}
				// enable listening for changes after changes are written to ddc
				DistributedDataContainerAdvertisementGenerator.getInstance()
						.enableListeningForChanges();
			}
		} else if (message instanceof DDCMessage) {
			DDCMessage ddcMessage = (DDCMessage) message;
			UUID advId = ddcMessage.getAdvertisementId();
			if (!receivedMessages.contains(advId)) {
				LOG.debug("Got new DDC message.");
				receivedMessages.add(advId);
				DistributedDataContainerAdvertisementGenerator.getInstance()
						.disableListeningForChanges();
				for (DDCEntry entry : ddcMessage.getEntriesAdded()) {
					ddc.add(entry);
				}
				for (DDCEntry entry : ddcMessage.getEntriesRemoved()) {
					ddc.remove(entry.getKey());
				}
				// enable listening for changes after changes are written to ddc
				DistributedDataContainerAdvertisementGenerator.getInstance()
						.enableListeningForChanges();
			}
		}
	}

}