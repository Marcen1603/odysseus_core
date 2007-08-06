package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mg.dynaquest.monitor.POMonitor;
import mg.dynaquest.monitor.POMonitorEvent;
import mg.dynaquest.monitor.POMonitorEventListener;
import mg.dynaquest.queryexecution.event.POEventType;
import mg.dynaquest.queryexecution.po.access.PhysicalAccessPO;

/**
 * A partial implementation of the {@link IMetadataPreprocessor} interface.
 * 
 * Other preprocessors only need to implement the {@link #processMetadata(List)}
 * methods, everything else is handled by this class.
 * 
 * @author Jonas Jacobi
 */
public abstract class AbstractMetadataPreprocessor implements
		IMetadataPreprocessor, POMonitorEventListener {

	/**
	 * All registered offline leaners and their updateInterval
	 */
	private Map<IOfflineListener, Integer> offlineListener;

	/**
	 * All registered online learners
	 */
	private List<IOnlineListener> onlineListener;

	/**
	 * This map is used for the caching of fired po-events in a runnning
	 * DynaQuest system, so the event history doesn't need to be queried for
	 * those events.
	 * 
	 * @see POMonitorEvent
	 * @see POEventData
	 */
	private HashMap<mg.dynaquest.queryexecution.po.base.PlanOperator, List<POEventData>> planoperators;

	/**
	 * the event history
	 */
	private IPOMetadatarepository metadataRepository;

	/**
	 * Constructor
	 * 
	 * @param metadatarepository
	 *            the event history used to get old metadata
	 */
	public AbstractMetadataPreprocessor(IPOMetadatarepository metadatarepository) {
		super();
		offlineListener = new HashMap<IOfflineListener, Integer>();
		onlineListener = new ArrayList<IOnlineListener>();
		planoperators = new HashMap<mg.dynaquest.queryexecution.po.base.PlanOperator, List<POEventData>>();
		this.metadataRepository = metadatarepository;
	}

	public void registerOfflineListener(IOfflineListener listener,
			int updateInterval) {
		this.offlineListener.put(listener, updateInterval);
	}

	public void registerOnlineListener(IOnlineListener listener) {
		if (!this.onlineListener.contains(listener)) {
			this.onlineListener.add(listener);
		}
	}

	/**
	 * Add a {@link POMonitor} to the observed monitors and register as event
	 * listener.
	 * 
	 * @param monitor
	 *            the observed monitor - has to be a subclass of
	 *            PhysicalAccessPO
	 * @see {@link POMonitor#addPOMonitorEventListener(POMonitorEventListener)}
	 */
	public void addPoMonitor(POMonitor monitor) {
		if (this.planoperators.containsKey(monitor.getPlanOperator())
				|| !(monitor.getPlanOperator() instanceof PhysicalAccessPO)) {
			return;
		}

		this.planoperators.put(monitor.getPlanOperator(),
				new LinkedList<POEventData>());
		monitor.addPOMonitorEventListener(this);
	}

	public void poMonitorEventOccured(POMonitorEvent ev) {
		mg.dynaquest.queryexecution.po.base.PlanOperator po = (mg.dynaquest.queryexecution.po.base.PlanOperator) ev
				.getSource();
		List<POEventData> events = this.planoperators.get(po);
		if (po instanceof PhysicalAccessPO) {

			if (ev.getEvent().getPOEventType() == POEventType.CloseDone
					|| (ev.getEvent().getPOEventType() == POEventType.OpenInit && events
							.size() > 0)) {

				// create a new list first, so new events can be added,
				// while the old ones get processed
				this.planoperators.put(po, new LinkedList<POEventData>());

				if (ev.getEvent().getPOEventType() == POEventType.CloseDone) {
					events.add(toPOEventData(ev));
				} else {
					this.planoperators.get(po).add(toPOEventData(ev));
				}
				PreprocessedMetadata data = processMetadata(events);

				String source = ((PhysicalAccessPO) po).getAccessPO()
						.getSource().toString();

				for (IOnlineListener curListener : this.onlineListener) {
					curListener.processedRequest(source, data);
				}
			} else {
				events.add(toPOEventData(ev));
			}
		}
	}

	public List<mg.dynaquest.monitor.responseprediction.preprocessor.PlanOperator> getPlans() {
		return this.metadataRepository.getPlans();
	}

	public List<mg.dynaquest.monitor.responseprediction.preprocessor.PlanOperator> getPlans(
			long afterTimestamp) {
		return this.metadataRepository.getPlans(afterTimestamp);
	}

	public Map<PlanOperator, PreprocessedMetadata> getMetadata(
			Collection<PlanOperator> pos) throws Exception {
		Map<PlanOperator, PreprocessedMetadata> data = new HashMap<PlanOperator, PreprocessedMetadata>();
		for (PlanOperator curPo : pos) {
			PreprocessedMetadata dataadd = getMetadata(curPo.getGuid());
			data.put(curPo, dataadd);
		}
		return data;
	}

	public PreprocessedMetadata getMetadata(String poGuid) throws Exception {
		return processMetadata(getMetadataRepository().getEvents(poGuid));
	}

	/**
	 * Get the event history used by this preprocessor
	 * 
	 * @return event history
	 */
	protected IPOMetadatarepository getMetadataRepository() {
		return this.metadataRepository;
	}

	/**
	 * Convert a {@link POMonitorEvent} to a {@link POEventData} object
	 * 
	 * @param ev
	 *            the event to convert
	 * @return the converted event
	 */
	protected POEventData toPOEventData(POMonitorEvent ev) {
		return new POEventData(ev.getTime(), ev.getEvent().getPOEventType(), ev
				.getEvent().getSourceGUID());
	}

	/**
	 * Preprocess the metadata for one planoperator
	 * 
	 * @param events
	 *            all events for one planoperator sorted chronologically (from
	 *            open_init to close_done)
	 * @return result of the preprocessor phase
	 */
	protected abstract PreprocessedMetadata processMetadata(
			List<POEventData> events);

}
