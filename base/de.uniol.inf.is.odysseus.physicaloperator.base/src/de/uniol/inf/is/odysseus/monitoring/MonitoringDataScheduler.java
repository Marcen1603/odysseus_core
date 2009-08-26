package de.uniol.inf.is.odysseus.monitoring;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
	public void cancelPeriodicalMetadataItem(IPeriodicalMonitoringData item) {
		
		/*
		 * Ein MetadataItem, das zum ScheduledThreadPoolExecutor mit
		 * scheduleAtFixedRate() hinzugefuegt wurde, kann normalerweise
		 * nicht beendet werden. Man kann sich aber das Future, in das das
		 * Item gekapselt wird und von der Methode scheduleAtFixedRate()
		 * geliefert wird, merken (hier mit addStartedPeriodicalMetadataItem())
		 * und dann dieses Future abbrechen und aus der Queue entfernen.
		 */
		
		ScheduledFuture future = map.remove(item);
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
