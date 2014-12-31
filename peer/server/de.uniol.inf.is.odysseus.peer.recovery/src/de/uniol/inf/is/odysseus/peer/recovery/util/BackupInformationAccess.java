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
import de.uniol.inf.is.odysseus.peer.ddc.distribute.advertisement.DistributedDataContainerAdvertisement;
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
	public void saveBackupInformation(int queryId, String pql, String state, String sharedQuery, boolean master, String strategy) {
		String localPeerId = p2pNetworkManager.getLocalPeerID().toString();
		this.saveBackupInformation(localPeerId, queryId, pql, state, sharedQuery, master, strategy);
	}

	@Override
	public void saveBackupInformation(String peerId, int queryId, String pql, String state, String sharedQuery, boolean master, String strategy) {
		LOG.debug("Save backup-info for query {}", queryId);

		DDCKey key = new DDCKey(peerId);
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();

		if (infoMap.containsKey(queryId)) {
			// Yes -> Update this information
			BackupInfo info = infoMap.get(queryId);
			info.pql = pql;
			info.state = state;
			info.sharedQuery = sharedQuery;
			info.master = master;
			if(strategy != null) {
				info.strategy = strategy;
			}
		} else {
			// No -> New information
			BackupInfo info = new BackupInfo();
			info.pql = pql;
			info.state = state;
			info.sharedQuery = sharedQuery;
			infoMap.put(queryId, info);
			info.master = master;
			if(strategy != null) {
				info.strategy = strategy;
			}
		}

		// Save the information in the DDC
		saveToDDC(infoMap, key);
	}

	@Override
	public void removeBackupInformation(int queryId) {
		String localPeerId = p2pNetworkManager.getLocalPeerID().toString();
		removeBackupInformation(localPeerId, queryId);
	}

	@Override
	public void removeBackupInformation(String peerId, int queryId) {
		LOG.debug("Remove backup-info for query {} for peer {}", queryId, peerId);

		DDCKey key = new DDCKey(peerId);
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();
		infoMap.remove(queryId);

		if (infoMap.isEmpty()) {
			// We don't have any information about this peer -> Remove the whole entry
			ddc.remove(key);

			// Distribute
			distributeDDC();

			LOG.debug("No more infos about this peer, delete peer from list.");
		}
	}

	@Override
	public ArrayList<String> getBackupPeerIds() {
		ImmutableCollection<PeerID> peerIds = peerDictionary.getRemotePeerIDs();
		ArrayList<String> peers = new ArrayList<String>(peerIds.size() + 1);

		for (PeerID peer : peerIds) {
			if (ddc.containsKey(new DDCKey(peer.toString()))) {
				peers.add(peer.toString());
			}
		}

		String localPeerId = p2pNetworkManager.getLocalPeerID().toString();
		if (ddc.containsKey(new DDCKey(localPeerId))) {
			peers.add(localPeerId);
		}

		return peers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Integer, BackupInfo> getBackupInformation(String peerId) {
		DDCKey key = new DDCKey(peerId);

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
		DistributedDataContainerAdvertisement ddcAdvertisement = DistributedDataContainerAdvertisementGenerator
				.getInstance().generateChanges();
		DistributedDataContainerAdvertisementSender.getInstance().publishDDCAdvertisement(ddcAdvertisement);
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

	@Override
	public String getBackupStrategy(int queryId) {
		HashMap<Integer, BackupInfo> infoMap = getBackupInformation();
		if (infoMap.containsKey(queryId)) {
			BackupInfo info = infoMap.get(queryId);
			return info.strategy;
		}
		return null;
	}

}
