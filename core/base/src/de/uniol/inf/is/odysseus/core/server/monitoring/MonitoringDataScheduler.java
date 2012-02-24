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

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;

//TODO singleton missbrauch als globale variable
public class MonitoringDataScheduler extends ScheduledThreadPoolExecutor {
	
	private static final int THREADCOUNT = 1;
	private static MonitoringDataScheduler instance = new MonitoringDataScheduler();
	
	private HashMap<IPeriodicalMonitoringData<?>, ScheduledFuture<?>> map;

	public static MonitoringDataScheduler getInstance() {
		// Neuen Scheduler erstellen, wenn die vorhandene Instanz bereits
		// heruntergefahren wurde, weil sie nicht mehr benoetigt wurde.
		if (instance.isShutdown()) {
			instance = new MonitoringDataScheduler();
		}
		return instance;
	}

	private MonitoringDataScheduler() {
		super(THREADCOUNT);
		map = new HashMap<IPeriodicalMonitoringData<?>, ScheduledFuture<?>>();
	}
	
	/**
	 * Speichert ein MetadataItem und das dazugehoerige Future, in das
	 * das Item innerhalb der Queue gekapselt ist.
	 * @param item Das Item
	 * @param future Das Future
	 */
	public void addStartedPeriodicalMetadataItem(IPeriodicalMonitoringData<?> item, ScheduledFuture<?> future) {
		map.put(item, future);
	}
	
	public boolean isMetadataItemStarted(IPeriodicalMonitoringData<?> item) {
		return map.containsKey(item);
	}
	
	/**
	 * Bricht die periodische Ausfuehrung eines MetadataItem ab. Wenn
	 * es das letzte Item in der Queue war, dann wird der Scheduler
	 * anschliessend heruntergefahren.
	 * @param item Das abzubrechende MetadataItem
	 */
	public void cancelPeriodicalMetadataItem(IPeriodicalMonitoringData<?> item) {
		
		/*
		 * Ein MetadataItem, das zum ScheduledThreadPoolExecutor mit
		 * scheduleAtFixedRate() hinzugefuegt wurde, kann normalerweise
		 * nicht beendet werden. Man kann sich aber das Future, in das das
		 * Item gekapselt wird und von der Methode scheduleAtFixedRate()
		 * geliefert wird, merken (hier mit addStartedPeriodicalMetadataItem())
		 * und dann dieses Future abbrechen und aus der Queue entfernen.
		 */
		
		ScheduledFuture<?> future = map.remove(item);
		if (future != null) {
			future.cancel(true);
			this.getQueue().remove(future);
		}
		
		/*
		 * Wenn sich in der Queue nichts mehr befindet, kann der Scheduler
		 * heruntergefahren werden. Ansonsten laeuft der Thread immer weiter
		 * und das Programm wird nicht ordentlich beendet.
		 * Falls der Scheduler spaeter doch noch einmal benoetigt wird, wird
		 * in getInstance() eine neue Instanz erstellt.
		 */
		
		if (getQueue().size() == 0) {
			shutdownNow();
		}
	}
}
