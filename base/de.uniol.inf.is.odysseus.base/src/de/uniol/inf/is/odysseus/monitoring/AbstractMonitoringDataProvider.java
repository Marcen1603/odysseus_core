package de.uniol.inf.is.odysseus.monitoring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractMonitoringDataProvider implements IMonitoringDataProvider {

	@SuppressWarnings("unchecked")
	private Map<String, IMonitoringData> metaDataItem;

	@SuppressWarnings("unchecked")
	public AbstractMonitoringDataProvider() {
		this.metaDataItem = new HashMap<String, IMonitoringData>();
	}

	@SuppressWarnings("unchecked")
	public <T> IMonitoringData<T> getMonitoringData(String type) {
		return this.metaDataItem.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T> IPeriodicalMonitoringData<T> getMonitoringData(String type,
			long period) {
		String key = type+" Period"+period;
		
		IMonitoringData<T> item =  this.metaDataItem.get(key);
		if (item != null){
			return (IPeriodicalMonitoringData)item;
		}
		
		item = this.metaDataItem.get(type);
		if (item != null && item instanceof IPeriodicalMonitoringData){
			IPeriodicalMonitoringData<T> pitem = (IPeriodicalMonitoringData<T>) item.clone();
			pitem.reset();
			this.metaDataItem.put(key, pitem);
							
			ScheduledFuture future = MonitoringDataScheduler.getInstance().scheduleAtFixedRate(
					pitem, 0, period, TimeUnit.MILLISECONDS);
			
			// Speichere Item und Future in Scheduler, damit das Item spaeter wieder
			// angehalten / geloescht werden kann.
			MonitoringDataScheduler.getInstance().addStartedPeriodicalMetadataItem(pitem, future);
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

	public void addMonitoringData(String type,
			IMonitoringData<?> item) {
		if (this.metaDataItem.containsKey(type)) {
			throw new IllegalArgumentException(type + " is already registered");
		}

		this.metaDataItem.put(type, item);
	}

	public void removeMonitoringData(String type) {
		this.metaDataItem.remove(type);
	}
	
}
