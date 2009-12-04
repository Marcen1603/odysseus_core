package de.uniol.inf.is.odysseus.p2p.operatorpeer;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IDistributionClient;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.Priority;

public abstract class AbstractOperatorPeer extends AbstractPeer {

	private IAdvancedExecutor executor;

	protected IAliveHandler aliveHandler;

	private Thread aliveHandlerThread;

	protected MainWindow gui;
	
	protected IPriority priority;

	protected IQuerySpezificationListener querySpezificationFinder;

	private Thread querySpezificationFinderThread;

	private Thread socketListenerThread;

	protected ISocketServerListener socketServerListener;

	protected ISourceHandler sourceHandler;

	private Thread sourceHandlerThread;
	
	protected HashMap<String, String> sources = new HashMap<String, String>();
	
	private IDistributionClient distributionClient;


	public void bindDistributionClient(IDistributionClient dc) {
		System.out.println("binde DistributionClient");
		this.distributionClient = dc;
		this.distributionClient.setManagedQueries(getQueries());
		this.distributionClient.initializeService();
		try {
			registerMessageHandler(this.distributionClient.getMessageHandler());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unbindDistributionClient(IDistributionClient dc) {
		if(this.distributionClient == dc) {
			this.distributionClient = null;			
		}

	}
	
	public IDistributionClient getDistributionClient() {
		return distributionClient;
	}

	public HashMap<String, String> getSources() {
		return sources;
	}

	public void setSources(HashMap<String, String> sources) {
		this.sources = sources;
	}


	public MainWindow getGui() {
		return gui;
	}

	public IPriority getPriority() {
		return priority;
	}

	public ISocketServerListener getSocketServerListener() {
		return socketServerListener;
	}

	public Map<String, ISource<?>> getSourcesFromWrapperPlanFactory() {
		return WrapperPlanFactory.getSources();
	}
	
	public IAdvancedExecutor getExecutor() {
		return executor;
	}


	private void init() {
		initServerResponseConnection();
		Log.setWindow(getGui());
		initSources(this);
		initPriorityMode();
		initSourceHandler(this);
		initSocketServerListener(this);
		initExecutor();
		initDistributor();
		initQuerySpezificationFinder();
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
		this.executor = executor;
	}
	
	public void unbindExecutor(IAdvancedExecutor executor) {
		if(this.executor == executor) {
			this.executor = null;
		}
	}
	
	
	protected abstract void initAliveHandler();

	protected void initPriorityMode() {
		this.priority = new Priority();
	}

	protected abstract void initQuerySpezificationFinder();

	protected abstract void initSocketServerListener(AbstractOperatorPeer aPeer);

	protected abstract void initSourceHandler(AbstractOperatorPeer aPeer);

	protected abstract void initSources(AbstractOperatorPeer aPeer);
	
	protected abstract void initServerResponseConnection();

	public void setGui(MainWindow gui) {
		this.gui = gui;
	}

	public void setPriority(IPriority priority) {
		this.priority = priority;
	}


	public void setSocketServerListener(
			ISocketServerListener socketServerListener) {
		this.socketServerListener = socketServerListener;
	}


	protected void startAliveHandler() {
		if (aliveHandlerThread != null && aliveHandlerThread.isAlive()) {
			aliveHandlerThread.interrupt();
		}
		this.aliveHandlerThread = new Thread(aliveHandler);
		aliveHandlerThread.start();
	}

	private void startGui() {
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
