package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.util.Enumeration;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterPriority;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.base.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.transformation.helper.relational.RelationalTransformationHelper;

/**
 * Operator-Peers mit Quellen werden separat ausgeschrieben, so dass diese gesondert gesucht und eingetragen werden
 * 
 * @author Mart Köhler
 *
 */
public class SourceListenerJxtaImpl implements ISourceListener, DiscoveryListener {

	
	private int WAIT_TIME=10000;
	
	private IExecutor executor;
	
	private ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration(new RelationalTransformationHelper(), "relational", ITimeInterval.class));

	public IExecutor getExecutor() {
		return executor;
	}

	public SourceListenerJxtaImpl(){
	}
	
	public SourceListenerJxtaImpl(IExecutor executor) {
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
						if(DataDictionary.getInstance().getSourceType(adv.getSourceName()) != null) {
							// TODO: User einfuegen, der diese Query ausf�hrt
							User user = UserManagement.getInstance().getP2PUser();
							getExecutor().addQuery(adv.getSourceScheme(), "CQL", user, this.trafoConfigParam, new ParameterPriority(2));
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
