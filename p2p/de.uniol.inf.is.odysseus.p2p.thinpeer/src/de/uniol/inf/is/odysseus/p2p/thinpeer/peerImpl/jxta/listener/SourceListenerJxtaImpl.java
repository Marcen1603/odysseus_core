package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.listener;

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;

public class SourceListenerJxtaImpl implements ISourceListener,
		DiscoveryListener {

	//Wie oft soll nach neuen Quellen gesucht werden
	private int WAIT_TIME=10000;
	
	//Wieviele Advertisements pro Peer
	private int ADVS_PER_PEER=20;
	
	public SourceListenerJxtaImpl() {
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			ThinPeerJxtaImpl.getInstance().getDiscoveryService().getRemoteAdvertisements(null,
					DiscoveryService.ADV, "sourceName", "*", ADVS_PER_PEER, this);
		}
	}

	@Override
	public void discoveryEvent(DiscoveryEvent ev) {
		DiscoveryResponseMsg res = ev.getResponse();
		SourceAdvertisement adv = null;
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				try {
					Object temp2 = en.nextElement();
					if (temp2 instanceof SourceAdvertisement) {
						adv = (SourceAdvertisement) temp2;
						PipeAdvertisement pipeAdv = MessageTool
								.createPipeAdvertisementFromXml(adv
										.getSourceSocket());
						ThinPeerJxtaImpl.getInstance().getSources().put(adv.getSourceName(),
								pipeAdv);
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}
		}
	}

}
