package de.uniol.inf.is.odysseus.monitoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IEvent;
import de.uniol.inf.is.odysseus.base.IEventListener;
import de.uniol.inf.is.odysseus.base.IEventType;

public abstract class AbstractMonitoringDataProvider implements
		IMonitoringDataProvider {

	final private Map<IEventType, ArrayList<IEventListener>> eventListener = new HashMap<IEventType, ArrayList<IEventListener>>();
	final private ArrayList<IEventListener> genericEventListener = new ArrayList<IEventListener>();

	static Logger _logger = null;

	static private Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory
					.getLogger(AbstractMonitoringDataProvider.class);
		}
		return _logger;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, IMonitoringData> metaDataItem;

	@SuppressWarnings("rawtypes")
	public AbstractMonitoringDataProvider() {
		this.metaDataItem = new HashMap<String, IMonitoringData>();
	}

	@SuppressWarnings("unchecked")
	public <T> IMonitoringData<T> getMonitoringData(String type) {
		return this.metaDataItem.get(type);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> IPeriodicalMonitoringData<T> getMonitoringData(String type,
			long period) {
		String key = type + " Period" + period;

		IMonitoringData<T> item = this.metaDataItem.get(key);
		if (item != null) {
			return (IPeriodicalMonitoringData) item;
		}

		item = this.metaDataItem.get(type);
		if (item != null && item instanceof IPeriodicalMonitoringData) {
			IPeriodicalMonitoringData<T> pitem = null;
			pitem = (IPeriodicalMonitoringData<T>) item.clone();
			pitem.reset();
			this.metaDataItem.put(key, pitem);

			ScheduledFuture future = MonitoringDataScheduler.getInstance()
					.scheduleAtFixedRate(pitem, 0, period,
							TimeUnit.MILLISECONDS);

			// Speichere Item und Future in Scheduler, damit das Item spaeter
			// wieder
			// angehalten / geloescht werden kann.
			MonitoringDataScheduler.getInstance()
					.addStartedPeriodicalMetadataItem(pitem, future);
			return pitem;

		}
		throw new IllegalArgumentException("No such Metadataitem");

	}

	public Collection<String> getProvidedMonitoringData() {
		return this.metaDataItem.keySet();
	}

	public boolean providesMonitoringData(String type) {
		return this.metaDataItem.containsKey(type);
	}

	public void addMonitoringData(String type, IMonitoringData<?> item) {
		getLogger().debug(
				"Add Monitoring Data " + type + " " + item + " to " + this);
		if (this.metaDataItem.containsKey(type)) {
			throw new IllegalArgumentException(type + " is already registered");
			// return;
		}

		this.metaDataItem.put(type, item);
	}

	public void removeMonitoringData(String type) {
		getLogger().debug("Remove Monitoring Data " + type + " from " + this);
		this.metaDataItem.remove(type);
	}

	protected void stopMonitoring() {
		getLogger().debug("Stop Monitoring " + this);
		for (IMonitoringData<?> m : metaDataItem.values()) {
			m.cancelMonitoring();
		}
		metaDataItem.clear();
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribe(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventType)
	 */
	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IEventListener> curEventListener = this.eventListener
					.get(type);
			if (curEventListener == null) {
				curEventListener = new ArrayList<IEventListener>();
				this.eventListener.put(type, curEventListener);
			}
			curEventListener.add(listener);
		}
		if (listener instanceof IMonitoringData) {
			IMonitoringData<?> mItem = (IMonitoringData<?>) listener;
			// nur dann registrieren, falls es noch nicht registriert ist
			// es kann z.B. sein, dass ein MetadataItem f�r zwei Events
			// registriert wird, aber nat�rlich trotzdem nur einmal
			// als MetadataItem registriert sein muss

			// TODO: Die Loesung mit der Exception war nicht schoen ...
			// Jetzt wird es einfach ignoriert
			// try {
			if (!this.providesMonitoringData(mItem.getType())) {
				this.addMonitoringData(mItem.getType(), mItem);
			}
			// } catch (IllegalArgumentException e) {
			// }
		}
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IEventListener> curEventListener = this.eventListener
					.get(type);
			curEventListener.remove(listener);
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribeToAll(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener)
	 */
	@Override
	public void subscribeToAll(IEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.add(listener);
		}
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.remove(listener);
		}
	}

	final protected void fire(IEvent<?,?> event) {
		synchronized (eventListener) {

			ArrayList<IEventListener> list = this.eventListener.get(event
					.getEventType());
			if (list != null) {
				synchronized (list) {
					for (IEventListener listener : list) {
						listener.eventOccured(event);
					}
				}
			}
			for (IEventListener listener : this.genericEventListener) {
				listener.eventOccured(event);
			}
		}
	}

}
