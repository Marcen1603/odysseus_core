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
package de.uniol.inf.is.odysseus.p2p.operatorpeer;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.collection.IPair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IDistributionClient;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority.Priority;

public abstract class AbstractOperatorPeer extends AbstractOdysseusPeer {

	static Logger logger = LoggerFactory.getLogger(AbstractOdysseusPeer.class);
	static Logger getLogger(){
		return logger;
	}
	
	private IServerExecutor executor;

	protected IAliveHandler aliveHandler;

	private Thread aliveHandlerThread;

	protected MainWindow gui;
	
	protected IPriority priority;

	private Thread socketListenerThread;

	protected ISourceHandler sourceHandler;

	private Thread sourceHandlerThread;
	
	protected HashMap<String, IPair<String,String>> sources = new HashMap<String, IPair<String,String>>();
	
	private IDistributionClient distributionClient;

	public AbstractOperatorPeer(ISocketServerListener socketServerListener, ILogListener log) {
		super(socketServerListener, log);
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

	public HashMap<String, IPair<String, String>> getSources() {
		return sources;
	}

	public void setSources(HashMap<String, IPair<String, String>> sources) {
		this.sources = sources;
	}


	public MainWindow getGui() {
		return gui;
	}

	public IPriority getPriority() {
		return priority;
	}
	
	public IExecutor getExecutor() {
		return executor;
	}


	private void init() {
		try {
			initServerResponseConnection();
			((Log)getLog()).setWindow(getGui());
			initPriorityMode();
			initMessageSender();
			initSourceHandler(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bindExecutor(IExecutor executor) {
		getLogger().info("Binding Executor: "+ executor.getCurrentSchedulerID() +" "+ executor.getCurrentSchedulingStrategyID());
		this.executor = (IServerExecutor)executor;
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
		gui = new MainWindow(this.getName());
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
			@Override
            public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				initSources(me);
				startSourceHandler();
			}
		}.start();
		
		// sofort executor starten
		try {
			executor.startExecution();
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
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
