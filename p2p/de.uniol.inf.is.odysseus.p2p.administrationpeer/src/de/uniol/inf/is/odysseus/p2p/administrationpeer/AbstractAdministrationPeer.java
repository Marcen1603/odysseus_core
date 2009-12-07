package de.uniol.inf.is.odysseus.p2p.administrationpeer;



import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IHotPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IOperatorPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IHotPeerStrategy;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.IDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.splitting.base.ISplittingStrategy;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

public abstract class AbstractAdministrationPeer extends AbstractPeer {

	protected IQuerySpezificationListener querySpezificationListener;

	private Thread querySpezificationListenerThread;

	private Thread operatorPeerListenerThread;
	
	private Thread aliveHandlerThread;
	
	private Thread hotPeerFinderThread;
	
	protected IAliveHandler aliveHandler;
	
	protected IOperatorPeerListener operatorPeerListener;
	
	protected IHotPeerListener hotPeerFinder;

	protected ISocketServerListener socketServerListener;
	
	protected ISourceListener sourceListener;
	
	private Thread sourceListenerThread;
	
	protected IMessageHandler queryResultHandler;
	
	protected IHotPeerStrategy hotPeerStrategy;
	
	protected boolean peerStarted = false;
	
	private IAdvancedExecutor executor;
	protected ISplittingStrategy splitting;
	protected IDistributionProvider<AbstractPeer> distributionProvider;
	
	private ICompiler compiler;

	
	public ICompiler getCompiler() {
		return compiler;
	}
	
	
	
	public void bindCompiler(ICompiler compiler) {
		getLogger().info("Binding Compiler" , compiler);
		this.compiler = compiler;

	}
	
	public void unbindCompiler(ICompiler compiler) {
		getLogger().info("Unbinding Compiler" , compiler);
		if(this.compiler == compiler){
			this.compiler = null;
		}
	}
	public IAdvancedExecutor getExecutor() {
		return executor;
	}

	public void bindExecutor(IAdvancedExecutor executor) {
		getLogger().info("Binding Executor" , executor);
		this.executor = executor;
	}
	
	public void unbindExecutor(IAdvancedExecutor executor) {
		getLogger().info("Unbinding Executor" , executor);
		if(this.executor == executor) {
			this.executor = null;
		}
	}
	
	public IDistributionProvider<AbstractPeer> getDistributionProvider() {
		return distributionProvider;
	}

	public void bindDistributionProvider(IDistributionProvider<AbstractPeer> dp) {
		getLogger().info("Binding Distribution Provider" , dp);
		this.distributionProvider = dp;
//		this.distributionProvider.setManagedQueries(getQueries());
		this.distributionProvider.setPeer(this);
		this.distributionProvider.initializeService();
		//Handler des Distribution Providers registrieren
//		try {
//			registerMessageHandler(this.distributionProvider
//					.getMessageHandler());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void unbindDistributionProvider(IDistributionProvider<AbstractPeer> dp) {
		System.out.println("unbind distribution");
		if(this.distributionProvider == dp) {
			getLogger().info("Unbinding Distribution Provider" , dp);
			this.distributionProvider = null;
//			deregisterMessageHandler(this.distributionProvider.getMessageHandler());
		}
	}
	
	
	public AbstractAdministrationPeer() {
		super();
	}
	
	
	
	

	
	public void bindSplitting(ISplittingStrategy splitting) {
		getLogger().info("Binding Splitting Service" , splitting);
			this.splitting = splitting;
	}
	
	public void unbindSplitting(ISplittingStrategy splitting) {
		getLogger().info("Unbinding Splitting Service" , splitting);
		if(this.splitting == splitting) {
			this.splitting = null;
		}
	}
	
	public IHotPeerStrategy getHotPeerStrategy() {
		return hotPeerStrategy;
	}

	public ISocketServerListener getSocketServerListener() {
		return socketServerListener;
	}

	public ISplittingStrategy getSplitting() {
		return splitting;
	}


	private void init() {
		initServerResponseConnection();
		initWrapperPlanFactory();
		initOperatorPeerListener();
		initQuerySpezificationListener();
		initSourceListener();
		initAliveHandler();
		initHotPeerFinder();
		initQueryResultHandler();
		initHotPeerStrategy();
		initSocketServerListener();
	}
	
	protected abstract void initServerResponseConnection();

	protected abstract void initSourceListener();

	protected abstract void initQuerySpezificationListener();

	protected abstract void initOperatorPeerListener();
	
	protected abstract void initAliveHandler();
	
	protected abstract void initSocketServerListener();
	
	protected MainWindow gui;

	private Thread socketListenerThread;
	
	public MainWindow getGui() {
		return gui;
	}

	public void setGui(MainWindow gui) {
		this.gui = gui;
	}
	
	private void startGui(){
			gui = new MainWindow();
	}
	
	protected void initWrapperPlanFactory() {
		try {
			WrapperPlanFactory.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSocketServerListener(ISocketServerListener socketServerListener) {
		this.socketServerListener = socketServerListener;
	}

	protected abstract void startNetwork();

	public void startPeer() {
		startNetwork();
		init();
		getDistributionProvider().startService();
		startGui();
		startQuerySpezificationListener();
		startSourceListener();
		startOperatorPeerListener();
		startAliveHandler();
		startHotPeerFinder();
		startSocketServerListener();
		Log.setWindow(getGui());
		getLogger().info("Peer Services started");
	}

	

	private void startSocketServerListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		
		this.socketListenerThread = new Thread(socketServerListener);
		socketListenerThread.start();		
	}

	protected void startQuerySpezificationListener() {
		if (querySpezificationListenerThread != null
				&& querySpezificationListenerThread.isAlive()) {
			querySpezificationListenerThread.interrupt();
		}
		querySpezificationListenerThread = new Thread(querySpezificationListener);
		querySpezificationListenerThread.start();
	}
	
	protected void startAliveHandler() {
		if (aliveHandlerThread != null
				&& aliveHandlerThread.isAlive()) {
			aliveHandlerThread.interrupt();
		}
		aliveHandlerThread = new Thread(aliveHandler);
		aliveHandlerThread.start();
	}
	
	protected void startHotPeerFinder() {
		if (hotPeerFinderThread != null
				&& hotPeerFinderThread.isAlive()) {
			hotPeerFinderThread.interrupt();
		}
		hotPeerFinderThread = new Thread(hotPeerFinder);
		hotPeerFinderThread.start();
	}
	
	protected void startOperatorPeerListener() {
		if (operatorPeerListenerThread != null
				&& operatorPeerListenerThread.isAlive()) {
			operatorPeerListenerThread.interrupt();
		}
		operatorPeerListenerThread = new Thread(operatorPeerListener);
		operatorPeerListenerThread.start();
	}
	
	

	public IMessageHandler getQueryResultHandler() {
		return queryResultHandler;
	}


	
	protected void startSourceListener(){
		if (sourceListenerThread!=null && sourceListenerThread.isAlive()){
			sourceListenerThread.interrupt();
		}
		this.sourceListenerThread = new Thread(sourceListener);
		sourceListenerThread.start();
	}

	protected abstract void stopNetwork();
	
	protected abstract void initHotPeerFinder();
	
	protected abstract void initQueryResultHandler();
	
	protected abstract void initHotPeerStrategy();
	
	public void stopPeer() {
		stopNetwork();
	}
}
