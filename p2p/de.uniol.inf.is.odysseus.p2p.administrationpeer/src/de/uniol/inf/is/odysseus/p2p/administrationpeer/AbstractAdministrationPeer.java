package de.uniol.inf.is.odysseus.p2p.administrationpeer;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.p2p.IPeer;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IBiddingHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IEventListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IHotPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IOperatorPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IBiddingHandlerStrategy;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IHotPeerStrategy;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.bidding.IBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.splitting.ISplittingStrategy;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

public abstract class AbstractAdministrationPeer implements IPeer {

	protected IQuerySpezificationListener querySpezificationListener;

	private Thread querySpezificationListenerThread;

	private Thread socketListenerThread;
	
	private Thread biddingHandlerThread;
	
	private Thread operatorPeerListenerThread;
	
	private Thread eventListenerThread;
	
	private Thread aliveHandlerThread;
	
	private Thread hotPeerFinderThread;
	
	protected IAliveHandler aliveHandler;
	
	protected IOperatorPeerListener operatorPeerListener;
	
	protected IBiddingHandler biddingHandler;
	
	protected IHotPeerListener hotPeerFinder;

	protected ISocketServerListener socketServerListener;
	
	protected ISplittingStrategy splitter;
	
	protected ISourceListener sourceListener;
	
	private Thread sourceListenerThread;
	
	protected IBiddingStrategy biddingStrategy;
	
	protected IQueryResultHandler queryResultHandler;
	
	protected IHotPeerStrategy hotPeerStrategy;
	
	protected IBiddingHandlerStrategy biddingHandlerStrategy;
	
	protected IEventListener eventListener;
	
	protected boolean peerStarted = false;
	
	private IAdvancedExecutor executor;
	
	public IAdvancedExecutor getExecutor() {
		return executor;
	}

	private ICompiler compiler;
	
	public ICompiler getCompiler() {
		return compiler;
	}
	
	
	
	public void bindCompiler(ICompiler compiler) {
		this.compiler = compiler;

	}
	
	public void unbindCompiler(ICompiler compiler) {
		if(this.compiler == compiler){
			this.compiler = null;
		}
	}

	public void bindExecutor(IAdvancedExecutor executor) {
		System.out.println("Binde Executor: "+ executor.getCurrentScheduler() +" "+ executor.getCurrentSchedulingStrategy());
//		if(getExecutor()==null) {
//			//synchronisieren von executor
				this.executor = executor;
//			if(getTrafo()!=null && !this.peerStarted) {
//				this.peerStarted  = true;
//				startPeer();
//			}
//		}
				if(!peerStarted) {
					startPeer();
					peerStarted = true;
				}
	}
	
	public void unbindExecutor(IAdvancedExecutor executor) {
		if(this.executor == executor) {
			this.executor = null;
		}
	}
	


	public IEventListener getEventListener() {
		return eventListener;
	}

	public IBiddingHandlerStrategy getBiddingHandlerStrategy() {
		return biddingHandlerStrategy;
	}

	public IHotPeerStrategy getHotPeerStrategy() {
		return hotPeerStrategy;
	}

	private boolean guiEnabled = true;
	
	public boolean isGuiEnabled() {
		return guiEnabled;
	}

	public ISocketServerListener getSocketServerListener() {
		return socketServerListener;
	}

	public ISplittingStrategy getSplitter() {
		return splitter;
	}
	
	String events =  "OpenInit,OpenDone,ProcessInit,ProcessDone,ProcessInitNeg,ProcessDoneNeg,PushInit,PushDone,PushInitNeg,PushDoneNeg,Done" ;

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	private void init() {
		initWrapperPlanFactory();
		initSocketServerListener();
		initOperatorPeerListener();
		initBiddingStrategy();
		initEventListener();
		initQuerySpezificationListener();
		initSourceListener();
		initSplitter();
		initBiddingHandler();
		initAliveHandler();
		initHotPeerFinder();
		initBiddingHandlerStrategy();
		initQueryResultHandler(getCompiler());
		initHotPeerStrategy();
	}
	


	protected abstract void initSourceListener();

	protected abstract void initQuerySpezificationListener();

	protected abstract void initSocketServerListener();
	
	protected abstract void initSplitter();
	
	protected abstract void initBiddingHandler();
	
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
		if (isGuiEnabled())
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

	public void setSplitter(ISplittingStrategy splitter) {
		this.splitter = splitter;
	}

	final public ArrayList<AbstractLogicalOperator> splitPlan(AbstractLogicalOperator plan){
		return splitter.splitPlan(plan);
	}

	protected abstract void startNetwork();

	public void startPeer() {
		
		startNetwork();
		init();
		startGui();
		startQuerySpezificationListener();
		startServerSocketListener();
		startBiddingHandler();
		startSourceListener();
		startOperatorPeerListener();
		startAliveHandler();
		startHotPeerFinder();
		startEventListener();
		System.out.println("Alle Dienste gestartet");
	}

	private void startEventListener() {
		if (eventListenerThread != null
				&& eventListenerThread.isAlive()) {
			eventListenerThread.interrupt();
		}
		eventListenerThread = new Thread(eventListener);
		eventListenerThread.start();
		
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
	
	

	public IQueryResultHandler getQueryResultHandler() {
		return queryResultHandler;
	}

	protected void startServerSocketListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		this.socketListenerThread = new Thread(socketServerListener);
		socketListenerThread.start();
	}
	
	protected void startBiddingHandler() {
		if (biddingHandlerThread != null && biddingHandlerThread.isAlive()) {
			biddingHandlerThread.interrupt();
		}
		this.biddingHandlerThread = new Thread(biddingHandler);
		biddingHandlerThread.start();
	}
	
	protected void startSourceListener(){
		if (sourceListenerThread!=null && sourceListenerThread.isAlive()){
			sourceListenerThread.interrupt();
		}
		this.sourceListenerThread = new Thread(sourceListener);
		sourceListenerThread.start();
	}

	protected abstract void stopNetwork();
	
	protected abstract void initBiddingStrategy();
	
	protected abstract void initHotPeerFinder();
	
	protected abstract void initQueryResultHandler(ICompiler compiler);
	
	protected abstract void initHotPeerStrategy();
	
	protected abstract void initBiddingHandlerStrategy();
	
	protected abstract void initEventListener();
	
	public IBiddingStrategy getBiddingStrategy() {
		return biddingStrategy;
	}

	public void stopPeer() {
		stopNetwork();
	}
	
	protected void stopServerSocketListener() {
		this.socketListenerThread.interrupt();
	}
}
