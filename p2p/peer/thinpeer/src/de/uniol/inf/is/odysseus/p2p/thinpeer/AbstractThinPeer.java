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
package de.uniol.inf.is.odysseus.p2p.thinpeer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p.IExtendedPeerAdvertisement;
import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IGuiUpdater;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerDiscoverer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISourceDiscoverer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IIdGenerator;

public abstract class AbstractThinPeer extends AbstractOdysseusPeer implements
		IThinPeer {

	private Thread socketListenerThread;
	private MainWindow window;
	protected IGuiUpdater guiUpdater;
	protected IAdministrationPeerDiscoverer administrationPeerDiscoverer;
	private Thread administrationPeerListenerThread;
	protected ISourceDiscoverer sourceListener;
	private Thread sourceListenerThread;
	protected IIdGenerator idGenerator;
	protected IQueryPublisher queryPublisher;

	private List<IThinPeerListener> listener = new CopyOnWriteArrayList<IThinPeerListener>();

	private Map<String, Object> adminPeers;
	private Set<ISourceAdvertisement> sources = new HashSet<ISourceAdvertisement>();

	public AbstractThinPeer(ISocketServerListener serverListener,
			ILogListener log) {
		super(serverListener, log);
	}

	public Map<String, Object> getAdminPeers() {
		return adminPeers;
	}

	private void startGui() {
		window = new MainWindow(this, this.getName());
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		((Log) getLog()).setWindow(window);
		Thread t = new Thread(guiUpdater);
		t.start();
	}

	protected void startServerSocketListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		this.socketListenerThread = new Thread(getSocketServerListener());
		socketListenerThread.start();
	}

	protected void startAdministrationPeerListener() {
		if (administrationPeerListenerThread != null
				&& administrationPeerListenerThread.isAlive()) {
			administrationPeerListenerThread.interrupt();
		}
		this.administrationPeerListenerThread = new Thread(
				administrationPeerDiscoverer);
		administrationPeerListenerThread.start();
	}

	protected void startSourceListener() {
		if (sourceListenerThread != null && sourceListenerThread.isAlive()) {
			sourceListenerThread.interrupt();
		}
		this.sourceListenerThread = new Thread(sourceListener);
		sourceListenerThread.start();
	}

	@Override
	public void startPeer() {
		startNetwork();
		init();
		startServerSocketListener();
		startGui();
		startAdministrationPeerListener();
		startSourceListener();
	}

	@Override
	public void stopPeer() {
		stopNetwork();
	}

	protected void stopServerSocketListener() {
		this.socketListenerThread.interrupt();
	}

	protected void stopGuiUpdater() {
		this.guiUpdater = null;
	}

	private void init() {
		initServerResponseConnection();
		initMessageSender();
		initLocalMessageHandler();
		initQueryPublisher();
		initGuiUpdater();
		initAdministrationPeerListener();
		initSourceListener();
		initIdGenerator();
		initAdminPeerList();

	}

	protected abstract void initServerResponseConnection();

	protected void initAdminPeerList() {
		this.adminPeers = new HashMap<String, Object>();
	}

	protected abstract void initIdGenerator();

	protected abstract void initAdministrationPeerListener();

	protected abstract void initGuiUpdater();

	protected abstract void initQueryPublisher();

	protected abstract void initSourceListener();

	protected abstract void startNetwork();

	protected abstract void stopNetwork();

	// ---------------------------------------------------------
	// Temporary delegates to remove GUI-Dependencies
	// ---------------------------------------------------------

	public void adminPeerFound(String queryId, String peerId) {
		window.addAdminPeer(queryId, peerId);
	}

	public void log(String queryId, String status) {
		window.addStatus(queryId, status);
	}

	@Override
	protected void afterQueryRemoval(String queryId) {
		window.removeTab(queryId);
	}

	public boolean isEnabled() {
		return window.isEnabled();
	}

	public void setEnabled(boolean b) {
		window.setEnabled(b);
	}

	// ----------------------------------------------------------------
	// Listener-Dinge
	// ----------------------------------------------------------------

	@Override
	public void registerListener(IThinPeerListener thinPeerListener) {
		listener.add(thinPeerListener);
	}

	@Override
	public void removeListener(IThinPeerListener thinPeerListener) {
		listener.remove(thinPeerListener);
	}

	public void addOrUpdateAdminPeer(IExtendedPeerAdvertisement adminPeer) {
		if (!adminPeers.containsKey(adminPeer.getPeerId())) {
			this.adminPeers.put(adminPeer.getPeerId(), adminPeer);
			for (IThinPeerListener l : listener) {
				l.foundAdminPeer(adminPeer);
			}
		}
	}

	public void addOrUpdateSources(ISourceAdvertisement adv, ISession caller) {
		if (!sources.contains(adv)) {
			this.sources.add(adv);
			for (IThinPeerListener l : listener) {
				l.foundSource(adv, caller);
			}
		}
	}

}
