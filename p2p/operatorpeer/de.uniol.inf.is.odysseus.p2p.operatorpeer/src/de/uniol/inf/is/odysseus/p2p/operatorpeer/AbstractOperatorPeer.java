package de.uniol.inf.is.odysseus.p2p.operatorpeer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.collection.Pair;
import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IDistributionClient;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.ExecutorInitializeException;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.Priority;

public abstract class AbstractOperatorPeer extends AbstractOdysseusPeer {

	static Logger logger = LoggerFactory.getLogger(AbstractOdysseusPeer.class);
	static Logger getLogger(){
		return logger;
	}
	
	private IExecutor executor;

	protected IAliveHandler aliveHandler;

	private Thread aliveHandlerThread;

	protected MainWindow gui;
	
	protected IPriority priority;

	private Thread socketListenerThread;

	protected ISourceHandler sourceHandler;

	private Thread sourceHandlerThread;
	
	protected HashMap<String, Pair<String,String>> sources = new HashMap<String, Pair<String,String>>();
	
	private IDistributionClient distributionClient;


	public AbstractOperatorPeer() {
		super();
	}
	
	public void bindDistributionClient(IDistributionClient dc) {
		getLogger().info("Binding Distribution Client", dc);
		try {
			this.distributionClient = dc;
			this.distributionClient.setPeer(this);
			this.distributionClient.initializeService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unbindDistributionClient(IDistributionClient dc) {
		getLogger().info("Unbinding Distribution Client", dc);
		if(this.distributionClient == dc) {
			this.distributionClient = null;			
		}

	}
	
	public IDistributionClient getDistributionClient() {
		return distributionClient;
	}

	public HashMap<String, Pair<String, String>> getSources() {
		return sources;
	}

	public void setSources(HashMap<String, Pair<String, String>> sources) {
		this.sources = sources;
	}


	public MainWindow getGui() {
		return gui;
	}

	public IPriority getPriority() {
		return priority;
	}

	public Map<String, ISource<?>> getSourcesFromWrapperPlanFactory() {
		return WrapperPlanFactory.getSources();
	}
	
	public IExecutor getExecutor() {
		return executor;
	}


	private void init() {
		try {
			initServerResponseConnection();
			Log.setWindow(getGui());
			initExecutor();
			initPriorityMode();
			initMessageSender();
			initSourceHandler(this);
			initSocketServerListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void bindExecutor(IExecutor executor) {
		getLogger().info("Binding Executor: "+ executor.getCurrentSchedulerID() +" "+ executor.getCurrentSchedulingStrategyID());
		this.executor = executor;
	}
	
	public void unbindExecutor(IExecutor executor) {
		if(this.executor == executor) {
			getLogger().info("Unbinding Executor: "+ executor.getCurrentSchedulerID() +" "+ executor.getCurrentSchedulingStrategyID());
			this.executor = null;
		}
	}
	
	
	protected abstract void initAliveHandler();

	protected void initPriorityMode() {
		this.priority = new Priority();
	}

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

	@Override
	public void startPeer() {
		startNetwork();
		startGui();
		init();
		startServerSocketListener();
		startAliveHandler();
		getDistributionClient().startService();
		// Notwendiger Hack da die Quellen erst dann gebunden werden können
		// wenn das Execution Environment fertig ist ...
		// Später kein Problem mehr, da die Quellen ja anders eingebunden werden müssen!
		final AbstractOperatorPeer me = this;
		new Thread(){
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				initSources(me);
				startSourceHandler();
			};
		}.start();
		
	}

	protected void startServerSocketListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		this.socketListenerThread = new Thread(getSocketServerListener());
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

	@Override
	public void stopPeer() {
		stopNetwork();
	}
}
