/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.user.P2PUserContext;

/**
 * Operator-Peers mit Quellen werden separat ausgeschrieben, so dass diese
 * gesondert gesucht und eingetragen werden
 * 
 * @author Mart Koehler, Marco Grawunder
 * 
 */
public class SourceListenerJxtaImpl implements ISourceListener,
		DiscoveryListener {

	static Logger logger = LoggerFactory
			.getLogger(SourceListenerJxtaImpl.class);

	private int WAIT_TIME = 10000;

	private IExecutor executor;
	private IDataDictionary dd;

	private AdministrationPeerJxtaImpl administrationPeerJxtaImpl;

	public IExecutor getExecutor() {
		return executor;
	}

	public SourceListenerJxtaImpl() {
	}

	public SourceListenerJxtaImpl(IExecutor executor,
			AdministrationPeerJxtaImpl administrationPeerJxtaImpl) {
		this.executor = executor;
		this.dd = ((IServerExecutor)executor).getDataDictionary();
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

						ISession caller = P2PUserContext.getActiveSession("");
						
						// Login des users?
						String viewname = adv.getSourceName();
						
						// Nur eintragen, wenn nicht eh schon vorhanden
						if (dd.containsViewOrStream(
								adv.getSourceName(), caller)) {

							String sourceType = adv.getSourceType();


							ILogicalOperator topOperator = (ILogicalOperator) AdvertisementTools
									.fromBase64String(adv.getLogicalPlan());

							logger.debug("Adding to DD " + adv.getSourceName()
									+ " " + sourceType);
							
							if (adv.isView()) {
								dd.setView(viewname, topOperator,
										caller);
							} else {
								dd.setStream(viewname, topOperator,
										caller);
							}
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
