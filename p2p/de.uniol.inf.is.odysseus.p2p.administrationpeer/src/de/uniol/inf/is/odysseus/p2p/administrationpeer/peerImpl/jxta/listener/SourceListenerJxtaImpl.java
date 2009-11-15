package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.listener;

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterPriority;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

/**
 * Operator-Peers mit Quellen werden separat ausgeschrieben, so dass diese gesondert gesucht und eingetragen werden
 * 
 * @author Mart Köhler
 *
 */
public class SourceListenerJxtaImpl implements ISourceListener, DiscoveryListener {

	
	private int WAIT_TIME=10000;
	
	private IAdvancedExecutor executor;

	public IAdvancedExecutor getExecutor() {
		return executor;
	}

	public SourceListenerJxtaImpl(){
	}
	
	public SourceListenerJxtaImpl(IAdvancedExecutor executor) {
		this.executor = executor;
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
						AdministrationPeerJxtaImpl.getInstance().getSources().put(adv.getSourceName(), adv);
						// Hier wird noch das Schema der Quelle zum DataDictionary hinzugefügt, damit der Compiler mit den Informationen arbeiten kann
						if(DataDictionary.getInstance().sourceTypeMap.isEmpty() || !DataDictionary.getInstance().sourceTypeMap.containsKey(adv.getSourceName())) {
							getExecutor().addQuery(adv.getSourceScheme(), "CQL", new ParameterPriority(2));
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
