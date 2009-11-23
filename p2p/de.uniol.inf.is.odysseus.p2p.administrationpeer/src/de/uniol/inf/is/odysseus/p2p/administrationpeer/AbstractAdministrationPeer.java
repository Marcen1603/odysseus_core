package de.uniol.inf.is.odysseus.p2p.administrationpeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
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


public abstract class AbstractAdministrationPeer implements IPeer {

	protected IQuerySpezificationListener querySpezificationListener;

	private Thread querySpezificationListenerThread;

	private Thread socketListenerThread;
	
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
	
	protected ISplittingStrategy splitting;
	
	protected IDistributionProvider distributionProvider;
	
	private IAdvancedExecutor executor;
	
	private HashMap<String, Query> queries = new HashMap<String, Query>();
	
	private Map<String, IMessageHandler> messageHandler = new HashMap<String, IMessageHandler>();
//	
//	public void bindMessageHandler(IMessageHandler messageHandler) {
//		System.out.println("binde Namespace "+messageHandler.getInterestedNamespace());
//		this.messageHandler.put(messageHandler.getInterestedNamespace(), messageHandler);
//	}
//	
//	public void unbindMessageHandler(IMessageHandler messageHandler) {
//		if(this.messageHandler.containsKey(messageHandler.getInterestedNamespace())) {
//			this.messageHandler.remove(messageHandler.getInterestedNamespace());
//		}
//	}
	
	public IDistributionProvider getDistributionProvider() {
		return distributionProvider;
	}

	public void bindDistributionProvider(IDistributionProvider dp) {
		System.out.println("Binde Distribution Provider");
		this.distributionProvider = dp;
		this.distributionProvider.setManagedQueries(getQueries());
		this.distributionProvider.initializeService();
		//Handler des Distribution Providers registrieren
		getMessageHandler().put(this.distributionProvider.getMessageHandler().getInterestedNamespace(), this.distributionProvider.getMessageHandler());
		
	}
	
	public void unbindDistributionProvider(IDistributionProvider dp) {
		System.out.println("unbind distribution");
		if(this.distributionProvider == dp) {
			this.distributionProvider = null;
		}
	}
	
	
	public IAdvancedExecutor getExecutor() {
		return executor;
	}

	private ICompiler compiler;

	
	public ICompiler getCompiler() {
		return compiler;
	}
	
	
	
	public void bindCompiler(ICompiler compiler) {
		System.out.println("Binde Compiler");
		this.compiler = compiler;

	}
	
	public void unbindCompiler(ICompiler compiler) {
		System.out.println("unbind compiler");
		if(this.compiler == compiler){
			this.compiler = null;
		}
	}

	public void bindExecutor(IAdvancedExecutor executor) {
		System.out.println("Binde Executor: "+ executor.getCurrentScheduler() +" "+ executor.getCurrentSchedulingStrategy());
		this.executor = executor;
	}
	
	public void unbindExecutor(IAdvancedExecutor executor) {
		System.out.println("unbind executor");
		if(this.executor == executor) {
			this.executor = null;
		}
	}
	
	public void bindSplitting(ISplittingStrategy splitting) {
		try {
			System.out.println("Binde Splitting");
			this.splitting = splitting;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unbindSplitting(ISplittingStrategy splitting) {
		System.out.println("unbind splitting");
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
		initSocketServerListener(this);
		initOperatorPeerListener();
		initQuerySpezificationListener();
		initSourceListener();
		initAliveHandler();
		initHotPeerFinder();
		initQueryResultHandler();
		initHotPeerStrategy();
	}
	
	protected abstract void initServerResponseConnection();

	protected abstract void initSourceListener();

	protected abstract void initQuerySpezificationListener();

	protected abstract void initSocketServerListener(AbstractAdministrationPeer aPeer);
	
	protected abstract void initOperatorPeerListener();
	
	protected abstract void initAliveHandler();
	
	
	
	protected MainWindow gui;
	
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

	final public ArrayList<AbstractLogicalOperator> splitPlan(AbstractLogicalOperator plan){
		return this.splitting.splitPlan(plan);
	}

	protected abstract void startNetwork();

	public void startPeer() {
		System.out.println("startPeer()");
		startNetwork();

		init();
		getDistributionProvider().startService();
		startGui();
		startQuerySpezificationListener();
		startServerSocketListener();
		startSourceListener();
		startOperatorPeerListener();
		startAliveHandler();
		startHotPeerFinder();
		Log.setWindow(getGui());

		System.out.println("Alle Dienste gestartet");
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

	protected void startServerSocketListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		
		this.socketListenerThread = new Thread(socketServerListener);
		socketListenerThread.start();
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

	public void setQueries(HashMap<String, Query> queries) {
		this.queries = queries;
	}

	public HashMap<String, Query> getQueries() {
		return queries;
	}

	public void setMessageHandler(Map<String, IMessageHandler> messageHandler) {
		this.messageHandler = messageHandler;
	}

	public Map<String, IMessageHandler> getMessageHandler() {
		return messageHandler;
	}

	protected void initSocketServerListener() {
		// TODO Auto-generated method stub
		
	}
	
//	protected void stopServerSocketListener() {
//		this.socketListenerThread.interrupt();
//	}
}
