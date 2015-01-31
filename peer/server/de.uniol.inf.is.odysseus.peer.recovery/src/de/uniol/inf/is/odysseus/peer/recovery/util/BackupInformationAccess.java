package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.ddc.DDCEntry;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerChangeAdvertisement;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementGenerator;
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.sender.DistributedDataContainerAdvertisementSender;
import de.uniol.inf.is.odysseus.peer.recovery.IBackupInformationAccess;
import de.uniol.inf.is.odysseus.peer.recovery.internal.BackupInfo;

/**
 * This class gives you access to the backup-information which is saved in the DDC.
 * 
 * @author Tobias Brandt
 *
 */
public class BackupInformationAccess implements IBackupInformationAccess {

	private static final Logger LOG = LoggerFactory.getLogger(BackupInformationAccess.class);

	private static IDistributedDataContainer ddc;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerDictionary peerDictionary;

	// The prefix which is before every key (peerId) to identify, that it's recovery backup information
	public static String KEY_PREFIX = "rbi_";

	public static void bindDDC(IDistributedDataContainer ddcService) {
		ddc = ddcService;
	}

	public static void unbindDDC(IDistributedDataContainer ddcService) {
		if (ddc == ddcService) {
			ddc = null;
		}
	}

	public static void bindP2PNetworkManager(IP2PNetworkManager manager) {
		p2pNetworkManager = manager;
	}

	public static void unbindP2PNetworkManager(IP2PNetworkManager manager) {
		if (p2pNetworkManager == manager)
			p2pNetworkManager = null;
	}

	public static void bindPeerDictionary(IPeerDictionary dict) {
		peerDictionary = dict;
	}

	public static void unbindPeerDictionary(IPeerDictionary dict) {
		if (peerDictionary == dict)
			peerDictionary = null;
	}

	@Override
	public void saveBackupInformation(int queryId, String pql, String state, String sharedQuery, boolean master,
			String masterId) {
		String localPeerId = p2pNetworkManager.getLocalPeerID().toString();
		this.saveBackupInformation(localPeerId, queryId, pql, state, sharedQuery, master, masterId);
	}

	@Override
	public void saveBackupInformation(String peerId, int queryId, String pql, String state, String sharedQuery,
			boolean master, String masterId) {
		LOG.debug("Save backup-info for query {}", queryId);

		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();

		if (infoMap.containsKey(queryId)) {
			// Yes -> Update this information
			BackupInfo info = infoMap.get(queryId);
			info.pql = pql;
			info.state = state;
			info.sharedQuery = sharedQuery;
			info.master = master;
			info.masterID = masterId;
		} else {
			// No -> New information
			BackupInfo info = new BackupInfo();
			info.pql = pql;
			info.state = state;
			info.sharedQuery = sharedQuery;
			info.master = master;
			info.masterID = masterId;
			infoMap.put(queryId, info);
		}

		// Save the information in the DDC
		DDCKey key = getKeyForPeerId(peerId);
		saveToDDC(infoMap, key);
	}

	@Override
	public void removeBackupInformation(int queryId) {
		String localPeerId = p2pNetworkManager.getLocalPeerID().toString();
		removeBackupInformation(localPeerId, queryId);
	}

	@Override
	public void removeBackupInformation(String peerId, int queryId) {
		String peerName = peerDictionary.getRemotePeerName(peerId);
		LOG.debug("Remove backup-info for query {} for peer {}", queryId, peerName);

		DDCKey key = getKeyForPeerId(peerId);
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation(peerId);
		infoMap.remove(queryId);

		if (infoMap.isEmpty()) {
			// We don't have any information about this peer -> Remove the whole
			// entry
			ddc.remove(key);

			// Distribute
			distributeDDC();

			LOG.debug("No more infos about this peer, delete peer from list.");
		} else {
			// We still have information about this peer -> keep it, save it
			saveToDDC(infoMap, key);
		}
	}

	@Override
	public void updateBackupInfoForPipe(String failedPeer, String pipeId) {
		String localPeerId = p2pNetworkManager.getLocalPeerID().toString();

		HashMap<Integer, BackupInfo> infoMap = getBackupInformation(failedPeer);

		for (int key : infoMap.keySet()) {
			BackupInfo info = infoMap.get(key);
			// Check, if the pipeId is in the PQL. If it's the case, replace the old peerId (from the failed peer) with
			// our peerId
			if (info.pql.contains(pipeId) && info.pql.contains(failedPeer)) {
				LOG.debug("Replaced peerId {} in Backup-Store.", failedPeer);
				info.pql = info.pql.replaceAll(failedPeer, localPeerId);
				
				// Save it to the DDC
				DDCKey ddcKey = getKeyForPeerId(failedPeer);
				saveToDDC(infoMap, ddcKey);
			}
		}
	}

	@Override
	public ArrayList<String> getBackupPeerIds() {
		ImmutableCollection<PeerID> peerIds = peerDictionary.getRemotePeerIDs();
		ArrayList<String> peers = new ArrayList<String>(peerIds.size() + 1);

		for (PeerID peer : peerIds) {
			if (ddc.containsKey(getKeyForPeerId(peer.toString()))) {
				peers.add(peer.toString());
			}
		}

		String localPeerId = p2pNetworkManager.getLocalPeerID().toString();
		if (ddc.containsKey(getKeyForPeerId(localPeerId))) {
			peers.add(localPeerId);
		}

		return peers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, BackupInfo> getBackupInformation(String peerId) {
		DDCKey key = getKeyForPeerId(peerId);

		try {
			if (ddc.containsKey(key)) {
				DDCEntry entry = ddc.get(key);
				String value = entry.getValue();
				Object hashTestObject = fromString(value);

				HashMap<Integer, BackupInfo> infoMap = (HashMap<Integer, BackupInfo>) hashTestObject;
				return infoMap;
			}
		} catch (Exception e) {
			LOG.error("Can't read backup-information.");
		}

		return new HashMap<Integer, BackupInfo>();
	}

	@Override
	public String getBackupPQL(String peerId, int queryId) {
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation(peerId);
		if (infoMap.containsKey(queryId)) {
			BackupInfo info = infoMap.get(queryId);
			return info.pql;
		}
		return null;
	}

	@Override
	public String getBackupPQL(int queryId) {
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();
		if (infoMap.containsKey(queryId)) {
			BackupInfo info = infoMap.get(queryId);
			return info.pql;
		}
		return null;
	}

	@Override
	public String getBackupSharedQuery(int queryId) {
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();
		if (infoMap.containsKey(queryId)) {
			BackupInfo info = infoMap.get(queryId);
			return info.sharedQuery;
		}
		return null;
	}

	@Override
	public boolean isBackupMaster(int queryId) {
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();
		if (infoMap.containsKey(queryId)) {
			BackupInfo info = infoMap.get(queryId);
			return info.master;
		}
		return true;
	}

	@Override
	public String getBackupMasterId(int queryId) {
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();
		if (infoMap.containsKey(queryId)) {
			BackupInfo info = infoMap.get(queryId);
			return info.masterID;
		}
		return null;
	}

	@Override
	public HashMap<Integer, BackupInfo> getBackupInformation() {
		return getBackupInformation(p2pNetworkManager.getLocalPeerID().toString());
	}

	/**
	 * Read the object from Base64 string.
	 * 
	 * @param s
	 *            The string you want to convert to an object
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static Object fromString(String s) throws IOException, ClassNotFoundException {
		byte[] data = Base64Coder.decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	/** Write the object to a Base64 string. */
	private static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}

	private static void distributeDDC() {
		DistributedDataContainerChangeAdvertisement ddcAdvertisement = DistributedDataContainerAdvertisementGenerator
				.getInstance().generateChanges();
		DistributedDataContainerAdvertisementSender.getInstance().publishDDCChangeAdvertisement(ddcAdvertisement);
	}

	private static void saveToDDC(Serializable o, DDCKey key) {
		try {
			String serializedList = toString(o);
			DDCEntry newListEntry = new DDCEntry(key, serializedList, System.currentTimeMillis(), false);
			ddc.add(newListEntry);
			distributeDDC();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param peerId
	 *            The peerId you want to have the backup-information-key for
	 * @return the key for this peer
	 */
	private static DDCKey getKeyForPeerId(String peerId) {
		return new DDCKey(KEY_PREFIX + peerId);
	}

	@Override
	public BackupInfo getBackupInformation(int queryId) {
		return getBackupInformation().get(queryId);
	}

}
