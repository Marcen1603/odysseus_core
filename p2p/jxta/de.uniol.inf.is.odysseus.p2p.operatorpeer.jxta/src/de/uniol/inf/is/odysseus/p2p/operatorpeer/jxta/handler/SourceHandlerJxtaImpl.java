package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * Alle Quellen werden hier fuer die Verwendung im P2P Netzwerk vorbereitet und
 * in regelmaessigen Abstaenden ausgeschrieben.
 * 
 * @author Mart Köhler
 * 
 */
public class SourceHandlerJxtaImpl implements ISourceHandler {

	private int LIFETIME = 30000;
	private OperatorPeerJxtaImpl oPeer = null;

	public SourceHandlerJxtaImpl(OperatorPeerJxtaImpl aPeer) {
		this.setoPeer(aPeer);
	}

	@Override
	public void run() {
		ArrayList<SourceAdvertisement> advList = new ArrayList<SourceAdvertisement>();

		User user = GlobalState.getActiveUser(); // Wollen jede View bzw. Quelle
													// ausschreiben
		for (Entry<String, ILogicalOperator> v : GlobalState
				.getActiveDatadictionary().getStreamsAndViews(user)) {
			// Create source advertisement and add to publish list
			SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory
					.newAdvertisement(SourceAdvertisement
							.getAdvertisementType());

			adv.setSourceName(v.getKey());
			adv.setPeer(getoPeer().getNetPeerGroup()
					.getPeerAdvertisement().toString());
			adv.setSourceId(v.getKey());
			adv.setSourceScheme(getoPeer().getSources()
					.get(v.getKey()).getE1());
			adv.setLanguage(getoPeer().getSources()
					.get(v.getKey()).getE2());
			advList.add(adv);

		}

		// Publish all sources
		while (true) {
			for (SourceAdvertisement adv : advList) {
				try {
					getoPeer().getDiscoveryService()
							.publish(adv, LIFETIME, LIFETIME);
				} catch (IOException e) {
					e.printStackTrace();
				}
				getoPeer().getDiscoveryService()
						.remotePublish(adv, 31000);
			}
			try {
				Thread.sleep(LIFETIME - 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public PipeAdvertisement createSocketAdvertisement() {
		PipeID socketID = null;
		socketID = (PipeID) IDFactory.newPipeID(getoPeer().getNetPeerGroup().getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("Source Distribution Socket");

		return advertisement;
	}

	public void setoPeer(OperatorPeerJxtaImpl oPeer) {
		this.oPeer = oPeer;
	}

	public OperatorPeerJxtaImpl getoPeer() {
		return oPeer;
	}

}
