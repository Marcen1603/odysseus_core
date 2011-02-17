/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
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

	public SourceListenerJxtaImpl(IExecutor executor,
			AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
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
			administrationPeerJxtaImpl.getDiscoveryService()
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
						administrationPeerJxtaImpl.getSources().put(
								adv.getSourceName(), adv);
						IDataDictionary datadictionary = GlobalState
								.getActiveDatadictionary();
						User caller = GlobalState.getActiveUser();
						// Login des users?
						String viewname = adv.getSourceName();

						// Nur eintragen, wenn nicht eh schon vorhanden
						if (!datadictionary.containsViewOrStream(
								adv.getSourceName(), caller)) {

							String sourceType = adv.getSourceType();


							ILogicalOperator topOperator = (ILogicalOperator) AdvertisementTools
									.fromBase64String(adv.getLogicalPlan());
							SDFEntity entity = (SDFEntity) AdvertisementTools
									.fromBase64String(adv.getEntity());

							logger.debug("Adding to DD " + adv.getSourceName()
									+ " " + sourceType + " as "+entity);
							
							if (adv.isView()) {
								datadictionary.setView(viewname, topOperator,
										caller);
							} else {
								datadictionary.setStream(viewname, topOperator,
										caller);
							}
							datadictionary.addSourceType(viewname, sourceType);
							datadictionary.addEntity(viewname, entity, caller);
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
