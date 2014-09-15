package de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementGenerator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.message.DistributedDataContainerCurrentStateMessage;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.message.DistributedDataContainerCurrentStateResponseMessage;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.message.RepeatingMessageSend;

/**
 * Listener for DDCAdvertisements. The Changes from Advertisements will be
 * written to DDC
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class DistributedDataContainerAdvertisementListener implements
		IDistributedDataContainerAdvertisementListener,
		IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(DistributedDataContainerAdvertisementListener.class);

	private static IDistributedDataContainer ddc;

	private IP2PNetworkManager p2pNetworkManager;
	private List<UUID> receivedDDCAdvertisements = new ArrayList<UUID>();
	private List<UUID> receivedInitiatingMessages = new ArrayList<UUID>();
	private RepeatingMessageSend currentJob;
	private static IPeerCommunicator peerCommunicator;

	// called by OSGi-DS
	public static void bindDDC(IDistributedDataContainer ddc) {
		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		DistributedDataContainerAdvertisementListener.ddc = ddc;
		DistributedDataContainerAdvertisementListener.LOG.debug(
				"Bound {} as a DDC", ddc.getClass().getSimpleName());

	}

	// called by OSGi-DS
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (DistributedDataContainerAdvertisementListener.ddc == ddc) {
			DistributedDataContainerAdvertisementListener.ddc = null;
			DistributedDataContainerAdvertisementListener.LOG.debug(
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
		peerCommunicator
				.registerMessageType(DistributedDataContainerCurrentStateMessage.class);
		peerCommunicator
				.registerMessageType(DistributedDataContainerCurrentStateResponseMessage.class);

		peerCommunicator.addListener(this,
				DistributedDataContainerCurrentStateMessage.class);
		peerCommunicator.addListener(this,
				DistributedDataContainerCurrentStateResponseMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		LOG.debug("Unbound Peer Communicator.");
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this,
					DistributedDataContainerCurrentStateMessage.class);
			peerCommunicator.removeListener(this,
					DistributedDataContainerCurrentStateResponseMessage.class);

			peerCommunicator
					.unregisterMessageType(DistributedDataContainerCurrentStateMessage.class);
			peerCommunicator
					.unregisterMessageType(DistributedDataContainerCurrentStateResponseMessage.class);

			peerCommunicator = null;
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
			if (!receivedDDCAdvertisements.contains(ddcAdvertisement
					.getAdvertisementUid())) {
				receivedDDCAdvertisements.add(ddcAdvertisement
						.getAdvertisementUid());

				// disable listening in AdvertisementGenerator. Otherwise same
				// changes will detected multiple times on multiple peers.
				DistributedDataContainerAdvertisementGenerator.getInstance()
						.disableListeningForChanges();

				switch (ddcAdvertisement.getType()) {
				case initialDistribution:
					// write only added DDC entries to DDC on initial
					// distribution
					if (ddcAdvertisement.getAddedDDCEntires() != null) {
						for (DDCEntry addedDdcEntry : ddcAdvertisement
								.getAddedDDCEntires()) {
							DistributedDataContainerAdvertisementListener.ddc
									.add(addedDdcEntry);
						}
					}

					// send current state of the ddc to initiating peer
					List<DDCEntry> ddcEntries = new ArrayList<DDCEntry>();
					for (DDCKey key : DistributedDataContainerAdvertisementListener.ddc
							.getKeys()) {
						try {
							ddcAdvertisement
									.addAddedEntry(DistributedDataContainerAdvertisementListener.ddc
											.get(key));
						} catch (MissingDDCEntryException e) {
							// do nothing
						}
					}
					PeerID volunteerPeerID = p2pNetworkManager.getLocalPeerID();

					DistributedDataContainerCurrentStateMessage currentStateMessage = DistributedDataContainerCurrentStateMessage
							.createMessage(volunteerPeerID,
									ddcAdvertisement.getAdvertisementUid(),
									ddcEntries);
					this.currentJob = new RepeatingMessageSend(
							peerCommunicator, currentStateMessage,
							ddcAdvertisement.getInitiatingPeerId());
					currentJob.start();

					break;
				case changeDistribution:
					// write added entries in DDC
					if (ddcAdvertisement.getAddedDDCEntires() != null) {
						for (DDCEntry addedDdcEntry : ddcAdvertisement
								.getAddedDDCEntires()) {
							DistributedDataContainerAdvertisementListener.ddc
									.add(addedDdcEntry);
						}
					}
					// remove entries from DDC
					if (ddcAdvertisement.getRemovedDDCEntires() != null) {
						for (String[] deletedDdcEntryKey : ddcAdvertisement
								.getRemovedDDCEntires()) {
							DistributedDataContainerAdvertisementListener.ddc
									.remove(new DDCKey(deletedDdcEntryKey));
						}
					}
					break;
				default:
					LOG.debug("Could not detect DDCAdvertisement type. Changes not processed to DDC");
					break;
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

		if (message instanceof DistributedDataContainerCurrentStateMessage) {
			// processing on initiating peer

			DistributedDataContainerCurrentStateMessage currentStateMessage = (DistributedDataContainerCurrentStateMessage) message;

			if (!receivedInitiatingMessages.contains(currentStateMessage
					.getDdcAdvertisementUUID())) {

				DistributedDataContainerAdvertisementGenerator.getInstance()
						.disableListeningForChanges();

				// install current state from volunteer peer DDC on own DDC
				if (currentStateMessage.getDdcEntries() != null) {
					for (DDCEntry addedDdcEntry : currentStateMessage
							.getDdcEntries()) {
						DistributedDataContainerAdvertisementListener.ddc
								.add(addedDdcEntry);
					}
				}

				DistributedDataContainerAdvertisementGenerator.getInstance()
						.enableListeningForChanges();

				receivedInitiatingMessages.add(currentStateMessage
						.getDdcAdvertisementUUID());
			}

			// send response
			DistributedDataContainerCurrentStateResponseMessage responseMessage = DistributedDataContainerCurrentStateResponseMessage
					.createMessage(true);
			try {
				LOG.debug("Send Response");
				peerCommunicator.send(currentStateMessage.getVolunteerPeerID(),
						responseMessage);
			} catch (PeerCommunicationException e) {
				LOG.error("Error while sending Response:");
				LOG.error(e.getMessage());
			}

		}

		if (message instanceof DistributedDataContainerCurrentStateResponseMessage) {
			// processing on volunteer peer

			if (this.currentJob != null) {
				currentJob.stopRunning();
				currentJob = null;

			}
		}
	}

}
