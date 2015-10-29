package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartup;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupListener;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNetStartupData;

public class OdysseusNetStartupManager implements IOdysseusNetStartupManager {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetStartupManager.class);
	
	private final Collection<IOdysseusNetComponent> components = Lists.newArrayList();
	private final Collection<IOdysseusNetComponent> startedComponents = Lists.newArrayList();
	private final Object startUpSyncObject = new Object();
	private final Collection<IOdysseusNetStartupListener> listeners = Lists.newArrayList();

	private IOdysseusNetStartup startup;
	private Boolean started = false;

	private OdysseusNetStartupData startupData;

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
		synchronized (components) {
			if (!components.contains(serv)) {
				components.add(serv);
				
				LOG.debug("Added odysseus net component {}", serv);
			}
		}
	}

	// called by OSGi-DS
	public void unbindOdysseusNetComponent(IOdysseusNetComponent serv) {
		synchronized (components) {
			components.remove(serv);
			
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
			startupData = tryStart();
			LOG.info("Startupdata is {}", startupData);
			LOG.info("Starting OdysseusNet components");
			startComponents();

			started = true;
			LOG.info("Start of OdysseusNet finished");
		}
		
		fireStartEvent(startupData);
	}

	private OdysseusNetStartupData tryStart() throws OdysseusNetException {
		try {
			synchronized( startUpSyncObject ) {
				return startup.start();
			}
		} catch( Throwable t ) {
			throw new OdysseusNetException("Exception during startup", t);
		}
	}

	private void startComponents() {
		synchronized( components ) {
			for( IOdysseusNetComponent component : components ) {
				try {
					LOG.info("Starting OdysseusNet component {}", component);
					
					component.start(startupData);
					startedComponents.add(component);
					
				} catch( Throwable t ) {
					LOG.error("Exception during starting odysseus net component", t);
				}
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
			
			LOG.info("Stopping OdysseusNet startup");
			tryStop();
			startupData = null;
			LOG.info("Stopping OdysseusNet components");
			stopComponents();

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

	private void stopComponents() {
		synchronized( components ) {
			for( IOdysseusNetComponent component : startedComponents ) {
				try {
					LOG.info("Stopping OdysseusNet component {}", component);
					component.stop();
					
				} catch( Throwable t ) {
					LOG.error("Exception during starting odysseus net component", t);
				}
			}
			
			startedComponents.clear();
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
	public OdysseusNetStartupData getStartupData() {
		return startupData;
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
	
	private void fireStartEvent(OdysseusNetStartupData data) {
		synchronized( listeners ) {
			for( IOdysseusNetStartupListener listener : listeners ) {
				try {
					listener.odysseusNetStarted(this, data);
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
