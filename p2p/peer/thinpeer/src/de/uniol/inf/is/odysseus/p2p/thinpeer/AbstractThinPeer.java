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

import javax.swing.DefaultListModel;

import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IGuiUpdater;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IIdGenerator;
import de.uniol.inf.is.odysseus.usermanagement.User;

public abstract class AbstractThinPeer extends AbstractOdysseusPeer {

	private Thread socketListenerThread;
	private MainWindow window;
	protected IGuiUpdater guiUpdater;
	protected IAdministrationPeerListener administrationPeerListener;
	private Thread administrationPeerListenerThread;
	protected ISourceListener sourceListener;
	private Thread sourceListenerThread;
	protected IIdGenerator idGenerator;
	protected IQueryPublisher queryPublisher;
	
	public HashMap<String,Object> adminPeers;
	
	public AbstractThinPeer(ISocketServerListener serverListener, ILogListener log) {
		super(serverListener, log);
	}
	
	public HashMap<String,Object> getAdminPeers() {
		return adminPeers;
	}
	
	protected void setAdminPeers(HashMap<String,Object> adminPeers) {
		this.adminPeers = adminPeers;
	}

//	public MainWindow getGui() {
//		return gui;
//	}

	private void startGui() {
		
		// TODO: Change Handling of GUI --> GUI starts Peer!
		
		window = new MainWindow(this, this.getName());
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		((Log)getLog()).setWindow(window);
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
				administrationPeerListener);
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
	
	protected abstract void initAdminPeerList();
	
	protected abstract void initIdGenerator();
	
	protected abstract void initAdministrationPeerListener();

	protected abstract void initGuiUpdater();

	protected abstract void initQueryPublisher();

	protected abstract void initSourceListener();

	protected abstract void startNetwork();

	protected abstract void stopNetwork();

	public abstract void publishQuerySpezification(String query, String language, User user);

	public abstract void sendQuerySpezificationToAdminPeer(String query,
			String language, String adminPeer, String adminPeerName);

	
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

	public void setAdminPeersModel(DefaultListModel model) {
		window.getAdminPeers().setModel(model);
	}

	public void setSourcesModel(DefaultListModel model) {
		window.getSources().setModel(model);
	}


}
