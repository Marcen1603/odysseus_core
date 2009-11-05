package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener;

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterPriority;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.MessageTool;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;

public class SourceListenerJxtaImpl implements ISourceListener, DiscoveryListener {

	
	private int WAIT_TIME=10000;

	public SourceListenerJxtaImpl(){
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			AdministrationPeerJxtaImpl.getInstance().getDiscoveryService().getRemoteAdvertisements(null,
					DiscoveryService.ADV, "sourceName", "*", 20,
					this);
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
					Object source = en.nextElement();
					if (source instanceof SourceAdvertisement ){
						adv = (SourceAdvertisement) source;
//						PipeAdvertisement pipeAdv = MessageTool.createPipeAdvertisementFromXml(adv.getSourceSocket());
						AdministrationPeerJxtaImpl.getInstance().getSources().put(adv.getSourceName(), adv);
						// Hier wird noch das Schema der Quelle zum DataDictionary hinzugefügt, damit der Compiler mit den Informationen arbeiten kann
						if(DataDictionary.getInstance().sourceTypeMap.isEmpty() || !DataDictionary.getInstance().sourceTypeMap.containsKey(adv.getSourceName())) {
							AdministrationPeerJxtaImpl.getInstance().getExecutor().addQuery(adv.getSourceScheme(), "CQL", new ParameterPriority(2));
						}
						
					}
					else{
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

}
