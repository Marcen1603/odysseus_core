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
package de.uniol.inf.is.odysseus.core.server.monitoring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;

public abstract class AbstractMonitoringDataProvider implements
		IMonitoringDataProvider {

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

	@Override
	@SuppressWarnings("unchecked")
	public <T> IMonitoringData<T> getMonitoringData(String type) {
		return this.metaDataItem.get(type);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void createAndAddMonitoringData(
			IPeriodicalMonitoringData item, long period) {

		if (this.metaDataItem.containsKey(item.getType())) {
			throw new IllegalArgumentException(item.getType() + " is already registered for "+this);
			// return ;
		}
		
		this.metaDataItem.put(item.getType(), item);

		ScheduledFuture future = MonitoringDataScheduler.getInstance()
				.scheduleAtFixedRate(item, 0, period, TimeUnit.MILLISECONDS);

		// Speichere Item und Future in Scheduler, damit das Item spaeter
		// wieder
		// angehalten / geloescht werden kann.
		MonitoringDataScheduler.getInstance().addStartedPeriodicalMetadataItem(
				item, future);
	}
	
	@Override
	public Collection<String> getProvidedMonitoringData() {
		return this.metaDataItem.keySet();
	}

	@Override
	public boolean providesMonitoringData(String type) {
		return this.metaDataItem.containsKey(type);
	}

	@Override
	public void addMonitoringData(String type, IMonitoringData<?> item) {
		getLogger().debug(
				"Add Monitoring Data " + type + " " + item + " to " + this);
		if (this.metaDataItem.containsKey(type)) {
			throw new IllegalArgumentException(type + " is already registered");
			// return;
		}

		this.metaDataItem.put(type, item);
	}

	@Override
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
}
