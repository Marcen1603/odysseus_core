package de.uniol.inf.is.odysseus.net.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartup;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupListener;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentStatus;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;

public class OdysseusNetStartupManager implements IOdysseusNetStartupManager {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetStartupManager.class);
	private static final String AUTOSTART_CONFIG_KEY = "net.autostart";
	private static final Boolean AUTOSTART_DEFAULT_VALUE = false;

	private final Map<IOdysseusNetComponent, OdysseusNetComponentStatus> componentMap = Maps.newHashMap();
	private final Object startUpSyncObject = new Object();
	private final Collection<IOdysseusNetStartupListener> listeners = Lists.newArrayList();

	private IOdysseusNetStartup startup;
	private Boolean started = false;
	private Boolean autostart = null;

	private IOdysseusNode localNode;

	private static IOdysseusNetStartupManager instance;

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
				componentMap.put(serv, OdysseusNetComponentStatus.ADDED);

				LOG.debug("Added odysseus net component {}", serv);

			}
		}
	}

	// called by OSGi-DS
	public void unbindOdysseusNetComponent(IOdysseusNetComponent serv) {
		synchronized (componentMap) {
			tryStopComponent(serv);
			tryTerminateComponent(serv);

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
		if (isAutostart()) {
			LOG.debug("Autostart of OdysseusNet is activated. Starting now...");

			try {
				start();
			} catch (OdysseusNetException e) {
				LOG.error("Could not autostart OdysseusNet", e);
			}
		} else {
			LOG.debug("Autostart of OdysseusNet is deactivated");
		}

		instance = this;
	}

	private boolean isAutostart() {
		if (autostart == null) {
			String doAutostartStr = OdysseusNetConfiguration.get(AUTOSTART_CONFIG_KEY, AUTOSTART_DEFAULT_VALUE.toString());
			autostart = tryToBoolean(doAutostartStr, AUTOSTART_DEFAULT_VALUE);
		}

		return autostart;
	}

	private static boolean tryToBoolean(String text, boolean defaultValue) {
		try {
			return Boolean.valueOf(text);
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	// called by OSGi-DS
	public void deactivate() {
		if (isStarted()) {
			stop();
		}
		instance = null;
	}

	public static Optional<IOdysseusNetStartupManager> getInstance() {
		return Optional.fromNullable(instance);
	}

	private boolean tryInitComponent(IOdysseusNetComponent component) {
		try {
			synchronized (componentMap) {
				if (componentMap.get(component) != OdysseusNetComponentStatus.ADDED) {
					return false;
				}

				LOG.info("Initializing OdysseusNet component {}", component);
				component.init(localNode);

				componentMap.put(component, OdysseusNetComponentStatus.INITED);
				return true;
			}

		} catch (Throwable t) {
			LOG.error("Exception during initializing OysseusNet component", t);
			return false;
		}
	}

	private boolean tryStartComponent(IOdysseusNetComponent component) {
		try {
			synchronized (componentMap) {
				if (componentMap.get(component) != OdysseusNetComponentStatus.INITED && componentMap.get(component) != OdysseusNetComponentStatus.STOPPED) {
					return false;
				}

				LOG.info("Starting OdysseusNet component {}", component);

				component.start();
				componentMap.put(component, OdysseusNetComponentStatus.RUNNING);
				return true;
			}

		} catch (Throwable t) {
			LOG.error("Exception during starting odysseus net component", t);
			return false;
		}
	}
	
	private boolean tryStartFinishedComponent(IOdysseusNetComponent component) {
		try {
			synchronized (componentMap) {
				if (componentMap.get(component) != OdysseusNetComponentStatus.RUNNING ) {
					return false;
				}

				component.startFinished();
				LOG.info("Starting OdysseusNet component finished {}", component);
				
				return true;
			}

		} catch (Throwable t) {
			LOG.error("Exception during finish starting odysseus net component", t);
			return false;
		}
	}

	private boolean tryStopComponent(IOdysseusNetComponent component) {
		try {
			synchronized (componentMap) {
				if (componentMap.get(component) != OdysseusNetComponentStatus.RUNNING) {
					return false;
				}

				LOG.info("Stopping OdysseusNet component {}", component);
				componentMap.put(component, OdysseusNetComponentStatus.STOPPED);
				component.stop();

				return true;
			}
		} catch (Throwable t) {
			LOG.error("Exception during stopping odysseus net component", t);
			return false;
		}
	}

	private boolean tryTerminateComponent(IOdysseusNetComponent component) {
		try {
			synchronized (componentMap) {
				if (componentMap.get(component) != OdysseusNetComponentStatus.STOPPED && componentMap.get(component) != OdysseusNetComponentStatus.INITED) {
					return false;
				}

				LOG.info("Terminating OdysseusNet component {}", component);
				component.terminate(localNode);
				componentMap.put(component, OdysseusNetComponentStatus.ADDED);
				return true;
			}

		} catch (Throwable t) {
			LOG.error("Exception during terminating OysseusNet component", t);
			return false;
		}
	}

	@Override
	public void start() throws OdysseusNetException {
		LOG.info("Starting OdysseusNet");
		waitForStartupService();

		synchronized (started) {
			if (started) {
				LOG.warn("OdysseusNet already started. Stop first");
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
		
		startFinishedComponents();

		fireStartEvent(localNode);
		
		// important to print directly into console
		System.out.println("OdysseusNet started");
	}

	private IOdysseusNode tryStart() throws OdysseusNetException {
		try {
			synchronized (startUpSyncObject) {
				return startup.start();
			}
		} catch (Throwable t) {
			throw new OdysseusNetException("Exception during startup", t);
		}
	}

	private void initComponents() {
		synchronized (componentMap) {
			for (IOdysseusNetComponent component : componentMap.keySet()) {
				tryInitComponent(component);
			}
		}
	}

	private void startComponents() {
		synchronized (componentMap) {
			for (IOdysseusNetComponent component : componentMap.keySet()) {
				tryStartComponent(component);
			}
		}
	}
	
	private void startFinishedComponents() {
		synchronized (componentMap) {
			for (IOdysseusNetComponent component : componentMap.keySet()) {
				tryStartFinishedComponent(component);
			}
		}
	}

	private void stopComponents() {
		synchronized (componentMap) {
			for (IOdysseusNetComponent component : componentMap.keySet()) {
				tryStopComponent(component);
			}
		}
	}

	private void terminateComponents() {
		synchronized (componentMap) {
			for (IOdysseusNetComponent component : componentMap.keySet()) {
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
			if (!started) {
				LOG.warn("OdysseusNet already stopped. Start first");
				return;
			}
		}

		LOG.info("Stopping OdysseusNet components");
		stopComponents();

		LOG.info("Terminating OdysseusNet components");
		terminateComponents();

		LOG.info("Stopping OdysseusNet startup");
		tryStop();

		localNode = null;

		synchronized (started) {
			started = false;
		}
		LOG.info("OdysseusNet stopped now");

		fireStopEvent();
		
		// important to print directly into console
		System.out.println("OdysseusNet stopped");
	}

	private void tryStop() {
		try {
			synchronized (startUpSyncObject) {
				startup.stop();
			}

		} catch (Throwable t) {
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
	public IOdysseusNode getLocalOdysseusNode() throws OdysseusNetException {
		if (localNode == null) {
			throw new OdysseusNetException("Local node not available since odysseus net is not started");
		}
		return localNode;
	}

	@Override
	public void addListener(IOdysseusNetStartupListener listener) {
		Preconditions.checkNotNull(listener, "listener must not be null!");

		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeListener(IOdysseusNetStartupListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	private void fireStartEvent(IOdysseusNode localNode) {
		synchronized (listeners) {
			for (IOdysseusNetStartupListener listener : listeners) {
				try {
					listener.odysseusNetStarted(this, localNode);
				} catch (Throwable t) {
					LOG.error("Exception in OdysseusNet startup listener", t);
				}
			}
		}
	}

	private void fireStopEvent() {
		synchronized (listeners) {
			for (IOdysseusNetStartupListener listener : listeners) {
				try {
					listener.odysseusNetStopped(this);
				} catch (Throwable t) {
					LOG.error("Exception in OdysseusNet startup listener", t);
				}
			}
		}
	}

	@Override
	public Collection<IOdysseusNetComponent> getComponents() {
		return Lists.newArrayList(componentMap.keySet());
	}

	@Override
	public Optional<OdysseusNetComponentStatus> getComponentStatus(IOdysseusNetComponent component) {
		Preconditions.checkNotNull(component, "component must not be null!");

		return Optional.fromNullable(componentMap.get(component));
	}
}
