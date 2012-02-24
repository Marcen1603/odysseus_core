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
package de.uniol.inf.is.odysseus.p2p.administrationpeer;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.gui.MainWindow;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.handler.IAliveHandler;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IHotPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IOperatorPeerListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IQuerySpezificationListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISourceListener;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IHotPeerStrategy;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.IDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.gui.Log;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.splitting.base.ISplittingStrategy;

public abstract class AbstractAdministrationPeer extends AbstractOdysseusPeer implements IOdysseusPeer{

	static Logger logger = LoggerFactory.getLogger(AbstractAdministrationPeer.class);
	static Logger getLogger(){
		return logger;
	}
	
	protected IQuerySpezificationListener querySpezificationListener;
	private Thread querySpezificationListenerThread;
	private Thread operatorPeerListenerThread;
	private Thread aliveHandlerThread;
	private Thread hotPeerFinderThread;
	protected IAliveHandler aliveHandler;
	protected IOperatorPeerListener operatorPeerListener;
	protected IHotPeerListener hotPeerFinder;
	protected ISourceListener sourceListener;
	private Thread sourceListenerThread;
	protected IMessageHandler queryResultHandler;
	protected IHotPeerStrategy hotPeerStrategy;	
	
	private IExecutor executor;
	protected ISplittingStrategy splitting;
	protected IDistributionProvider<?> distributionProvider;
	
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
	public IExecutor getExecutor() {
		return executor;
	}

	public void bindExecutor(IExecutor executor) {
		getLogger().info("Binding Executor" , executor);
		this.executor = executor;
	}
	
	public void unbindExecutor(IExecutor executor) {
		getLogger().info("Unbinding Executor" , executor);
		if(this.executor == executor) {
			this.executor = null;
		}
	}
	
	public IDistributionProvider<?> getDistributionProvider() {
		return distributionProvider;
	}

	public void bindDistributionProvider(IDistributionProvider<?> dp) {
		try {
			getLogger().info("Binding Distribution Provider" , dp);
			this.distributionProvider = dp;
			this.distributionProvider.setPeer(this);
			this.distributionProvider.initializeService();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void unbindDistributionProvider(IDistributionProvider<?> dp) {
		if(this.distributionProvider == dp) {
			getLogger().info("Unbinding Distribution Provider" , dp);
			this.distributionProvider.finalizeService();
			this.distributionProvider = null;
		}
	}
	
	
	public AbstractAdministrationPeer(ISocketServerListener listener, ILogListener log) {
		super(listener, log);
	}
	
	public void bindSplitting(ISplittingStrategy splitting) {
		try {
			getLogger().info("Binding Splitting Service" , splitting);
			this.splitting = splitting;
			this.splitting.setPeer(this);
			this.splitting.initializeService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void unbindSplitting(ISplittingStrategy splitting) {
		getLogger().info("Unbinding Splitting Service" , splitting);
		if(this.splitting == splitting) {
			this.splitting.finalizeService();
			this.splitting = null;
		}
	}
	
	public IHotPeerStrategy getHotPeerStrategy() {
		return hotPeerStrategy;
	}

	public ISplittingStrategy getSplitting() {
		return splitting;
	}


	private void init() {
		initServerResponseConnection();
		initOperatorPeerListener();
		initMessageSender();
		initQuerySpezificationListener();
		initSourceListener();
		initAliveHandler();
		initHotPeerFinder();
		initLocalMessageHandler();
		initHotPeerStrategy();
		initLocalExecutionHandler();
	}
	
	protected abstract void initServerResponseConnection();

	protected abstract void initSourceListener();

	protected abstract void initQuerySpezificationListener();

	protected abstract void initOperatorPeerListener();
	
	protected abstract void initAliveHandler();
		
	protected MainWindow gui;

	private Thread socketListenerThread;
	
	public MainWindow getGui() {
		return gui;
	}

	public void setGui(MainWindow gui) {
		this.gui = gui;
	}
	
	private void startGui(){
			gui = new MainWindow(getName());
	}
	

	protected abstract void startNetwork();

	@Override
	public void startPeer() {
		getLogger().info("Start Peer Service");
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
		((Log)getLog()).setWindow(getGui());
		getLogger().info("Peer Services started");
	}

	

	private void startSocketServerListener() {
		if (socketListenerThread != null && socketListenerThread.isAlive()) {
			socketListenerThread.interrupt();
		}
		
		this.socketListenerThread = new Thread(getSocketServerListener());
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
	
	protected abstract void initHotPeerStrategy();
	
	@Override
	public void stopPeer() {
		stopNetwork();
	}
}
