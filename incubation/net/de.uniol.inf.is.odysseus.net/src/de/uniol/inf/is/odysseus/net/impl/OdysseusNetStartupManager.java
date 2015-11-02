package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartup;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupListener;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class OdysseusNetStartupManager implements IOdysseusNetStartupManager {

	private enum ComponentStatus { ADDED, INITED, RUNNING, STOPPED};
	
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetStartupManager.class);
	
	private final Map<IOdysseusNetComponent, ComponentStatus> componentMap = Maps.newHashMap();
	private final Object startUpSyncObject = new Object();
	private final Collection<IOdysseusNetStartupListener> listeners = Lists.newArrayList();

	private IOdysseusNetStartup startup;
	private Boolean started = false;

	private IOdysseusNode localNode;

	// called by OSGi-DS
	public void bindOdysseusNetStartup(IOdysseusNetStartup serv) {
		synchronized (startUpSyncObject) {
			startup = serv;
		}
		LOG.debug("Bound odysseus net startup {}", serv);
	}

	// called by OSGi-DS
	public void unbindOdysseusNetStartup(IOdysseusNetStartup serv) {
		synchronized (startUpSyncObject) {
			if (startup == serv) {
				startup = null;
				
				LOG.debug("Unbound odysseus net startup {}", serv);
			}
		}
	}

	// called by OSGi-DS
	public void bindOdysseusNetComponent(IOdysseusNetComponent serv) {
		synchronized (componentMap) {
			if (!componentMap.containsKey(serv)) {
				componentMap.put(serv, ComponentStatus.ADDED);
				
				LOG.debug("Added odysseus net component {}", serv);
			}
		}
	}

	// called by OSGi-DS
	public void unbindOdysseusNetComponent(IOdysseusNetComponent serv) {
		synchronized (componentMap) {
			ComponentStatus status = componentMap.get(serv);
			if( status == ComponentStatus.INITED) {
				serv.terminate(localNode);
			} else if( status == ComponentStatus.RUNNING) {
				serv.stop();
			}
			
			componentMap.remove(serv);
			
			LOG.debug("Removed odysseus net component {}", serv);
		}
	}
	
	// called by OSGi-DS
	public void bindOdysseusNetStartupListener(IOdysseusNetStartupListener serv) {
		addListener(serv);
	}

	// called by OSGi-DS
	public void unbindOdysseusNetStartupListener(IOdysseusNetStartupListener serv) {
		removeListener(serv);
	}
	
	// called by OSGi-DS
	public void activate() {
		// do nothing
	}
	
	// called by OSGi-DS
	public void deactivate() {
		synchronized( started ) {
			if( started ) {
				stop();
			}
		}
	}

	private boolean tryInitComponent(IOdysseusNetComponent component) {
		try {
			if( componentMap.get(component) != ComponentStatus.ADDED ) {
				return false;
			}
			
			LOG.info("Initializing OdysseusNet component {}", component);
			component.init(localNode);
			
			componentMap.put(component, ComponentStatus.INITED);
			return true;
			
		} catch( Throwable t ) {
			LOG.error("Exception during initializing OysseusNet component", t);
			return false;
		}
	}

	private boolean tryStartComponent(IOdysseusNetComponent component) {
		try {
			if( componentMap.get(component) != ComponentStatus.INITED && componentMap.get(component) != ComponentStatus.STOPPED ) {
				return false;
			}
			
			LOG.info("Starting OdysseusNet component {}", component);
			
			component.start();
			componentMap.put(component, ComponentStatus.RUNNING);
			return true;
			
		} catch( Throwable t ) {
			LOG.error("Exception during starting odysseus net component", t);
			return false;
		}
	}

	private boolean tryStopComponent(IOdysseusNetComponent component) {
		try {
			if( componentMap.get(component) != ComponentStatus.RUNNING ) {
				return false;
			}
			
			LOG.info("Stopping OdysseusNet component {}", component);
			componentMap.put(component, ComponentStatus.STOPPED);
			component.stop();

			return true;
		} catch( Throwable t ) {
			LOG.error("Exception during stopping odysseus net component", t);
			return false;
		}
	}
	
	private boolean tryTerminateComponent(IOdysseusNetComponent component) {
		try {
			if( componentMap.get(component) != ComponentStatus.STOPPED && componentMap.get(component) != ComponentStatus.INITED) {
				return false;
			}
			
			LOG.info("Terminating OdysseusNet component {}", component);
			component.terminate(localNode);
			componentMap.put(component, ComponentStatus.ADDED);
			return true;
			
		} catch( Throwable t ) {
			LOG.error("Exception during terminating OysseusNet component", t);
			return false;
		}
	}

	@Override
	public void start() throws OdysseusNetException {
		LOG.info("Starting OdysseusNet");
		waitForStartupService();

		synchronized (started) {
			if( started ) {
				LOG.error("OdysseusNet already started. Stop first");
				return;
			}
			
			LOG.info("Beginning Startup of OdysseusNet");
			localNode = tryStart();
			LOG.info("Local odysseus node is {}", localNode);
			
			LOG.info("Initializing OdysseusNet components");
			initComponents();
			
			LOG.info("Starting OdysseusNet components");
			startComponents();

			started = true;
			LOG.info("Start of OdysseusNet finished");
			LOG.info("Resulting local odysseus node: {}", localNode);
		}
		
		fireStartEvent(localNode);
	}

	private IOdysseusNode tryStart() throws OdysseusNetException {
		try {
			synchronized( startUpSyncObject ) {
				return startup.start();
			}
		} catch( Throwable t ) {
			throw new OdysseusNetException("Exception during startup", t);
		}
	}
	
	private void initComponents() {
		synchronized( componentMap ) {
			for( IOdysseusNetComponent component : componentMap.keySet() ) {
				tryInitComponent(component);
			}
		}
	}
	
	private void startComponents() {
		synchronized( componentMap ) {
			for( IOdysseusNetComponent component : componentMap.keySet() ) {
				tryStartComponent(component);
			}
		}
	}

	private void stopComponents() {
		synchronized( componentMap ) {
			for( IOdysseusNetComponent component : componentMap.keySet() ) {
				tryStopComponent(component);
			}
		}
	}
	
	private void terminateComponents() {
		synchronized( componentMap ) {
			for( IOdysseusNetComponent component : componentMap.keySet() ) {
				tryTerminateComponent(component);
			}
		}
	}
	
	private void waitForStartupService() {
		IOdysseusNetStartup su = null;

		do {
			synchronized (startUpSyncObject) {
				su = startup;
			}
			if (su == null) {
				waitSomeTime();
			}
		} while (su == null);
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void stop() {
		LOG.info("Stopping OdysseusNet");
		
		synchronized (started) {
			if( !started ) {
				LOG.error("OdysseusNet already stopped. Start first");
				return;
			}
			
			LOG.info("Stopping OdysseusNet components");
			stopComponents();
			
			LOG.info("Terminating OdysseusNet components");
			terminateComponents();

			LOG.info("Stopping OdysseusNet startup");
			tryStop();
			
			localNode = null;
			started = false;
			LOG.info("OdysseusNet stopped now");
		}
		
		fireStopEvent();
	}

	private void tryStop() {
		try {
			synchronized( startUpSyncObject ) {
				startup.stop();
			}
			
		} catch( Throwable t ) {
			LOG.error("Exception during stopping odysseus net", t);
		}
	}


	@Override
	public boolean isStarted() {
		boolean result = false;
		synchronized (started) {
			result = started;
		}
		return result;
	}
	
	@Override
	public IOdysseusNode getLocalOdysseusNode() {
		return localNode;
	}

	@Override
	public void addListener(IOdysseusNetStartupListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized( listeners ) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IOdysseusNetStartupListener listener) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	private void fireStartEvent(IOdysseusNode localNode) {
		synchronized( listeners ) {
			for( IOdysseusNetStartupListener listener : listeners ) {
				try {
					listener.odysseusNetStarted(this, localNode);
				} catch( Throwable t ) {
					LOG.error("Exception in OdysseusNet startup listener", t);
				}
			}
		}
	}
	
	private void fireStopEvent() {
		synchronized( listeners ) {
			for( IOdysseusNetStartupListener listener : listeners ) {
				try {
					listener.odysseusNetStopped(this);
				} catch( Throwable t ) {
					LOG.error("Exception in OdysseusNet startup listener", t);
				}
			}
		}
	}
}
