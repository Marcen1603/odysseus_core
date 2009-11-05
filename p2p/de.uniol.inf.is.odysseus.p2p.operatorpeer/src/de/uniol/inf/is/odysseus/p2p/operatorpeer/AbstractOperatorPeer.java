package de.uniol.inf.is.odysseus.p2p.operatorpeer;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.p2p.IPeer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IQueryResultHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.listener.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.strategy.bidding.IBiddingStrategy;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.Priority;

public abstract class AbstractOperatorPeer implements IPeer {

	private IAdvancedExecutor executor;

	protected IAliveHandler aliveHandler;

	private Thread aliveHandlerThread;

	protected IBiddingStrategy biddingStrategy;

	protected MainWindow gui;
	
	private boolean guiEnabled = true;

	protected IPriority priority;

	protected IQueryResultHandler queryResultHandler;

	protected IQuerySpezificationListener querySpezificationFinder;

	private Thread querySpezificationFinderThread;

//	protected IScheduler scheduler;
	
//	private ISchedulingStrategyFactory schedulerStrategy;

	private Thread socketListenerThread;

	protected ISocketServerListener socketServerListener;

	protected ISourceHandler sourceHandler;

	private Thread sourceHandlerThread;
	
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

//	protected ArrayList<String> sources = new ArrayList<String>();
	protected HashMap<String, String> sources = new HashMap<String, String>();

	public HashMap<String, String> getSources() {
		return sources;
	}

	public void setSources(HashMap<String, String> sources) {
		this.sources = sources;
	}

	protected ITransformation trafo;

	public IBiddingStrategy getBiddingStrategy() {
		return biddingStrategy;
	}

	public MainWindow getGui() {
		return gui;
	}

	public IPriority getPriority() {
		return priority;
	}

	public IQueryResultHandler getQueryResultHandler() {
		return queryResultHandler;
	}

//	public IScheduler getScheduler() {
//		return scheduler;
//	}

//	public ISchedulingStrategyFactory getSchedulerStrategy() {
//		return schedulerStrategy;
//	}

	public ISocketServerListener getSocketServerListener() {
		return socketServerListener;
	}

//	public ArrayList<String> getSources() {
//		return sources;
//	}

	public Map<String, ISource<?>> getSourcesFromWrapperPlanFactory() {
		return WrapperPlanFactory.getSources();
	}

	public ITransformation getTrafo() {
		return trafo;
	}
	
	
	public IAdvancedExecutor getExecutor() {
		return executor;
	}


	private void init() {
		initSources(this);
//		initWrapperPlanFactory();
//		initTransformation();
		initPriorityMode();
		//TODO:Init Source, wenn wir tatsächlich Sources besitzen. Da die WrapperPlanFactory initialisiert wurde, ist diese folglich leer
		initSourceHandler(this);
//		initAliveHandler();
		initBiddingStrategy();
		initSocketServerListener(this);
//		initSchedulerStrategy();
//		initScheduler();
		initExecutor();
		initDistributor();
		initPartitioner();
		initMonitoring();
		initQuerySpezificationFinder();
		initQueryResultHandler();
	}

	private void initMonitoring() {
		
	}

	private void initPartitioner() {
		
	}

	private void initDistributor() {
		
	}

	private void initExecutor() {
		if(this.executor!=null) {
			try {
				this.executor.initialize();
			} catch (ExecutorInitializeException e) {
				e.printStackTrace();
			}
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
	}
	
	public void unbindExecutor(IAdvancedExecutor executor) {
		if(this.executor == executor) {
			this.executor = null;
		}
	}
	
	public void bindTransformation(ITransformation transformation) {
		System.out.println("Binde Transformation: "+ transformation.toString());
//		if(getTrafo()==null) {
				this.trafo = transformation;
//			if(getExecutor()!=null && !this.peerStarted) {
//				this.peerStarted  = true;
				startPeer();
//			}
//		}
	}
	
	public void unbindTransformation(ITransformation transformation) {
		if(this.trafo == transformation) {
			this.trafo = null;
		}
	}
	
	
	protected abstract void initAliveHandler();

	protected abstract void initBiddingStrategy();

	protected void initPriorityMode() {
		this.priority = new Priority();
	}

	protected abstract void initQueryResultHandler();

	protected abstract void initQuerySpezificationFinder();

	
	
	
//	private void initScheduler() {
//		// TODO: Hier jetzt direkt ein Executor verwenden
//		//scheduler = new SingleThreadScheduler();
//		
//		
////		IPartitioningStrategy partitionStrategy = new SplittBeforeIIteratable();
////		scheduler = new SimpleThreadedScheduler(schedulerStrategy,
////				new FullBufferPlacementStrategy(prioMode),
////				partitionStrategy);
//	}

//	private void initSchedulerStrategy() {
//		//this.schedulerStrategy = new RoundRobinFactory();
//	}

	protected abstract void initSocketServerListener(AbstractOperatorPeer aPeer);

	protected abstract void initSourceHandler(AbstractOperatorPeer aPeer);

	protected abstract void initSources(AbstractOperatorPeer aPeer);

	
//	//TODO: Als Dienst einfügen
//	protected void initTransformation() {
//		this.trafo = new DroolsTransformation();
//	}

//	protected void initWrapperPlanFactory() {
//		try {
////			WrapperPlanFactory.init();
//			for(java.util.Map.Entry<String, ILogicalOperator> entry :  DataDictionary.getInstance().getViews()) {
//				System.out.println("Entry: "+entry.getKey());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public boolean isGuiEnabled() {
		return guiEnabled;
	}

	protected void publishSources(){
		
	}

	public void setBiddingStrategy(IBiddingStrategy biddingStrategy) {
		this.biddingStrategy = biddingStrategy;
	}

	public void setGui(MainWindow gui) {
		this.gui = gui;
	}

	public void setPriority(IPriority priority) {
		this.priority = priority;
	}

//	public void setScheduler(IScheduler scheduler) {
//		this.scheduler = scheduler;
//	}

	public void setSocketServerListener(
			ISocketServerListener socketServerListener) {
		this.socketServerListener = socketServerListener;
	}

//	public void setSources(ArrayList<String> sources) {
//		this.sources = sources;
//	}

	public void setTrafo(ITransformation trafo) {
		this.trafo = trafo;
	}

	protected void startAliveHandler() {
		if (aliveHandlerThread != null && aliveHandlerThread.isAlive()) {
			aliveHandlerThread.interrupt();
		}
		this.aliveHandlerThread = new Thread(aliveHandler);
		aliveHandlerThread.start();
	}

	private void startGui() {
		if (isGuiEnabled())
			gui = new MainWindow();
	}

	protected abstract void startNetwork();

	public void startPeer() {
		startNetwork();
		startGui();
		init();
		startSourceHandler();
		startServerSocketListener();
		startAliveHandler();
		startQuerySpezificationFinder();
		publishSources();
	}
	
	protected void startQuerySpezificationFinder() {
		if (querySpezificationFinderThread != null
				&& querySpezificationFinderThread.isAlive()) {
			querySpezificationFinderThread.interrupt();
		}
		this.querySpezificationFinderThread = new Thread(
				querySpezificationFinder);
		querySpezificationFinderThread.start();
	}

	protected void startServerSocketListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		this.socketListenerThread = new Thread(socketServerListener);
		socketListenerThread.start();
	}

	protected void startSourceHandler() {
		if (sourceHandlerThread != null && sourceHandlerThread.isAlive()) {
			sourceHandlerThread.interrupt();
		}
		this.sourceHandlerThread = new Thread(sourceHandler);
		sourceHandlerThread.start();
	}
	
	protected abstract void stopNetwork();

	public void stopPeer() {
		stopNetwork();
	}
}
