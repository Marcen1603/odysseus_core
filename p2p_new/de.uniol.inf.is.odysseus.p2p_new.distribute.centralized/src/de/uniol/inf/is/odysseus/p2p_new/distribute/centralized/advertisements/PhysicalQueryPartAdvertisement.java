package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;

@SuppressWarnings("unused")
public class PhysicalQueryPartAdvertisement extends Advertisement {
	private static final Logger LOG = LoggerFactory.getLogger(PhysicalQueryPartAdvertisement.class);
	private static final String ID_TAG = "id";
	private static final String PEER_ID_TAG = "peerid";
	private static final String MASTER_PEER_ID_TAG = "masterpeerid";
	private static final String ADVERTISEMENT_TYPE = "jxta:PhysicalQueryPlanAdvertisement";
	private ID id;
	private PeerID masterPeerID;
	private PeerID peerID;

	
	public PhysicalQueryPartAdvertisement(Element<?> root) {
		// TODO Auto-generated constructor stub
	}

	public PhysicalQueryPartAdvertisement() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ID getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String[] getIndexFields() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public PeerID getMasterPeerID() {
		return masterPeerID;
	}

	public void setMasterPeerID(PeerID masterPeerID) {
		this.masterPeerID = masterPeerID;
	}

	public PeerID getPeerID() {
		return peerID;
	}

	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}

	public void setID(ID id) {
		this.id = id;
	}
}
