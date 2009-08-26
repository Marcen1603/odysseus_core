package de.uniol.inf.is.odysseus.p2p.thinpeer;

import de.uniol.inf.is.odysseus.p2p.IPeer;
import de.uniol.inf.is.odysseus.p2p.thinpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IGuiUpdater;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.thinpeer.handler.IQueryPublisher;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.IAdministrationPeerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.strategy.RandomIdGenerator;
import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IBiddingHandlerStrategy;
import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IIdGenerator;

public abstract class AbstractThinPeer implements IPeer {

	protected ISocketServerListener socketServerListener;

	private Thread socketListenerThread;

	private MainWindow gui;

	protected IGuiUpdater guiUpdater;

	private Thread guiUpdaterThread;

	protected IBiddingHandler queryBiddingHandler;

	private Thread queryBiddingHandlerThread;

	protected IAdministrationPeerListener administrationPeerListener;

	private Thread administrationPeerListenerThread;

	protected ISourceListener sourceListener;

	private Thread sourceListenerThread;

	protected IIdGenerator idGenerator;

	protected IQueryPublisher queryPublisher;

	protected IBiddingHandlerStrategy biddingHandlerStrategy;

	public MainWindow getGui() {
		return gui;
	}

	public IBiddingHandlerStrategy getBiddingHandlerStrategy() {
		return biddingHandlerStrategy;
	}

	private void startGui() {
		gui = new MainWindow(this);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
		Thread t = new Thread(guiUpdater);
		t.start();
	}

	protected void startServerSocketListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		this.socketListenerThread = new Thread(socketServerListener);
		socketListenerThread.start();
	}

	protected void startQueryBiddingHandler() {
		if (queryBiddingHandlerThread != null
				&& queryBiddingHandlerThread.isAlive()) {
			queryBiddingHandlerThread.interrupt();
		}
		this.queryBiddingHandlerThread = new Thread(queryBiddingHandler);
		queryBiddingHandlerThread.start();
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

	public void startPeer() {
		startNetwork();
		init();
		startServerSocketListener();
		startGui();
		startQueryBiddingHandler();
		startAdministrationPeerListener();
		startSourceListener();
	}

	public void stopPeer() {
		stopNetwork();
	}

	protected void stopServerSocketListener() {
		this.socketListenerThread.interrupt();
		socketServerListener = null;
	}

	protected void stopGuiUpdater() {
		this.guiUpdaterThread.interrupt();
		this.guiUpdater = null;
	}

	private void init() {
		initSocketServerListener();
		initQueryPublisher();
		initGuiUpdater();
		initQueryBiddingHandler();
		initAdministrationPeerListener();
		initSourceListener();
		initIdGenerator();
		initBiddingHandlerStrategy();
	}

	public ISocketServerListener getSocketServerListener() {
		return socketServerListener;
	}

	public void setSocketServerListener(
			ISocketServerListener socketServerListener) {
		this.socketServerListener = socketServerListener;
	}

	protected void initIdGenerator() {
		this.idGenerator = new RandomIdGenerator();
	}

	protected abstract void initAdministrationPeerListener();

	protected abstract void initQueryBiddingHandler();

	protected abstract void initGuiUpdater();

	protected abstract void initQueryPublisher();

	protected abstract void initSocketServerListener();

	protected abstract void initSourceListener();

	protected abstract void startNetwork();

	protected abstract void stopNetwork();

	protected abstract void initBiddingHandlerStrategy();

	public abstract void publishQuerySpezification(String query, String language);

	public abstract void sendQuerySpezificationToAdminPeer(String query,
			String language, String adminPeer, String adminPeerName);
}
