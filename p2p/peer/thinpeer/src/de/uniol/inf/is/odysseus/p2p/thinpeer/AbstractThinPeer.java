package de.uniol.inf.is.odysseus.p2p.thinpeer;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IGuiUpdater;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IIdGenerator;

public abstract class AbstractThinPeer extends AbstractOdysseusPeer {

	private Thread socketListenerThread;
	private MainWindow gui;
	protected IGuiUpdater guiUpdater;
	protected IAdministrationPeerListener administrationPeerListener;
	private Thread administrationPeerListenerThread;
	protected ISourceListener sourceListener;
	private Thread sourceListenerThread;
	protected IIdGenerator idGenerator;
	protected IQueryPublisher queryPublisher;
	
	public HashMap<String,Object> adminPeers;
	
	public AbstractThinPeer(ISocketServerListener serverListener) {
		super(serverListener);
	}
	
	public HashMap<String,Object> getAdminPeers() {
		return adminPeers;
	}
	
	protected void setAdminPeers(HashMap<String,Object> adminPeers) {
		this.adminPeers = adminPeers;
	}

	public MainWindow getGui() {
		return gui;
	}

	private void startGui() {
		gui = new MainWindow(this);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
		Log.setWindow(getGui());
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

	public abstract void publishQuerySpezification(String query, String language);

	public abstract void sendQuerySpezificationToAdminPeer(String query,
			String language, String adminPeer, String adminPeerName);
}
