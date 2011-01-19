package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * Operator-Peers mit Quellen werden separat ausgeschrieben, so dass diese
 * gesondert gesucht und eingetragen werden
 * 
 * @author Mart Köhler
 * 
 */
public class SourceListenerJxtaImpl implements ISourceListener,
		DiscoveryListener {

	static Logger logger = LoggerFactory
			.getLogger(SourceListenerJxtaImpl.class);

	private int WAIT_TIME = 10000;

	private IExecutor executor;

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public IExecutor getExecutor() {
		return executor;
	}

	public SourceListenerJxtaImpl() {
	}

	public SourceListenerJxtaImpl(IExecutor executor, AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		this.executor = executor;
		this.administrationPeerJxtaImpl = administrationPeerJxtaImpl;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
			}
			administrationPeerJxtaImpl
					.getDiscoveryService()
					.getRemoteAdvertisements(null, DiscoveryService.ADV,
							"sourceName", "*", 20, this);
		}
	}

	@Override
	public synchronized void discoveryEvent(DiscoveryEvent ev) {

		DiscoveryResponseMsg res = ev.getResponse();
		SourceAdvertisement adv = null;
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				try {
					Object source = en.nextElement();
					if (source instanceof SourceAdvertisement) {
						adv = (SourceAdvertisement) source;
						//logger.debug("Found Source " + adv.getSourceName());

						administrationPeerJxtaImpl.getSources()
								.put(adv.getSourceName(), adv);
						User user = GlobalState.getActiveUser();
						IDataDictionary datadictionary = GlobalState
								.getActiveDatadictionary();
						// Nur eintragen, wenn nicht eh schon vorhanden
						if (!datadictionary.containsViewOrStream(adv.getSourceName(),
								user)){
							logger.debug("Adding to DD " + adv.getSourceName());
							List<IQueryBuildSetting<?>> cfg = executor.getQueryBuildConfiguration("Standard");
							getExecutor().addQuery(adv.getSourceScheme(),
									adv.getLanguage(), user, datadictionary,
									cfg.toArray(new IQueryBuildSetting[0]));
						}
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

}
