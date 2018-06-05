package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.DataSet;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

public class LayerUpdater extends ArrayList<ILayer> implements
		IStreamElementListener<Object>, Serializable, PropertyChangeListener {

	private static final long serialVersionUID = 1092858542289960843L;

	private static final Logger LOG = LoggerFactory
			.getLogger(LayerUpdater.class);

	private final IStreamMapEditor streamMapEditor;
	private final IStreamConnection<Object> connection;
	private final IQuery query;
	private int maxNumerOfElements;
	private int userDefinedTimeRange;

	private DefaultTISweepArea<Tuple<? extends ITimeInterval>> puffer;
	private HashMap<Integer, Quadtree> index = new HashMap<Integer, Quadtree>();
	private HashMap<Integer, Envelope> env = new HashMap<Integer, Envelope>();
	private int srid;

	private List<Tuple<? extends ITimeInterval>> elementList;

	@SuppressWarnings("unchecked")
	public LayerUpdater(IStreamMapEditor streamMapEditor, IQuery query,
			IStreamConnection<Object> connection) {
		super();
		this.streamMapEditor = streamMapEditor;
		this.connection = connection;
		this.query = query;
		try {
			this.puffer = (DefaultTISweepArea<Tuple<? extends ITimeInterval>>) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		connection.addStreamElementListener(this);
		if (!connection.isConnected()) {
			connection.connect();
		}
		maxNumerOfElements = 100000;

	}

	public IStreamConnection<Object> getConnection() {
		return connection;
	}

	public IQuery getQuery() {
		return query;
	}

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: StreamMap is only for spatial relational tuple!");
			return;
		}

		// SweepArea definiert Element Fenster bzw. die zu visualisierenden
		// Elemente.
		@SuppressWarnings("unchecked")
		Tuple<? extends ITimeInterval> tuple = (Tuple<? extends ITimeInterval>) element;
		PointInTime timestamp = tuple.getMetadata().getStart().clone();
		if (timestamp.afterOrEquals(streamMapEditor.getScreenManager()
				.getMaxIntervalEnd())
				|| streamMapEditor.getScreenManager().getMaxIntervalEnd()
						.isInfinite()) {
			// Maybe the stream elements do not come in the right order (e.g.
			// wrong csv-data)
			this.streamMapEditor.getScreenManager()
					.setMaxIntervalEnd(timestamp);
		} else if (timestamp.beforeOrEquals(streamMapEditor.getScreenManager()
				.getMaxIntervalStart())) {
			this.streamMapEditor.getScreenManager().setMaxIntervalStart(
					timestamp);
		}

		puffer.insert(tuple);
		if (this.streamMapEditor.getScreenManager().getInterval().getEnd()
				.isInfinite()
				|| (this.streamMapEditor.getScreenManager().getInterval()
						.getStart().beforeOrEquals(timestamp) && this.streamMapEditor
						.getScreenManager().getInterval().getEnd()
						.afterOrEquals(timestamp))) {
			// Add tuple to current list if the new timestamp is in the interval
			addTuple(tuple);
		}

		// Prevent an overflow in the puffer
		checkForPufferSize();

		// Should we redraw here or just if we added the tupel to the current
		// list?
		streamMapEditor.getScreenManager().redraw();

	}

	/**
	 * Removes the oldest elements if puffer is bigger than the configured size
	 */
	public void checkForPufferSize() {
		// Prevent an overflow in the puffer

		// Maybe the user don't want to cut the time: he defines '0' and we
		// won't delete
		if (userDefinedTimeRange != 0) {
			// The newest timestamp: the time of the newest element in ms - the
			// timerange in ms user wants to save
			long timeRangeInMs = userDefinedTimeRange * 1000;
			long userDefinedTimeStamp = 0;
			if (!puffer.isEmpty()) {
				userDefinedTimeStamp = puffer.getMaxStartTs().getMainPoint()
						- timeRangeInMs;
			}

			if (userDefinedTimeStamp > 0) {
				PointInTime userEndTS = new PointInTime(userDefinedTimeStamp);
				puffer.purgeElementsBefore(userEndTS);

				// Update "current-list", timeSlider and all the other things
				// which rely on the startTimeStamp
				streamMapEditor.getScreenManager().setMaxIntervalStart(
						puffer.getMinStartTs());
				this.elementList = this.puffer
						.queryOverlapsAsList(this.streamMapEditor
								.getScreenManager().getInterval());
				this.index = new HashMap<Integer, Quadtree>(this.index.size());
			}
		}

		// Maybe in the time the user-defined were too many tuples
		// then delete the oldest tuples, so we prevent an overflow
		while (puffer.size() > maxNumerOfElements) {
			// Remove old element(s)
			Iterator<Tuple<? extends ITimeInterval>> oldestElements = puffer
					.peekElementsContaing(puffer.getMinStartTs(), false);

			PointInTime deleteTime = null;
			if (oldestElements.hasNext()) {
				// We need just one (therefore no while)
				// This should be one of the last elements
				// if more than one start with the same oldest timestamp
				Tuple<? extends ITimeInterval> elem = oldestElements.next();
				deleteTime = elem.getMetadata().getEnd();
			}
			// This deletes the oldest element. If more than one element
			// is in this time-window, more than one element will
			// be deleted.
			if( deleteTime != null ) {
				puffer.purgeElementsBefore(deleteTime.plus(1));
			}

			// Update "current-list", timeSlider and all the other things which
			// rely on the startTimeStamp
			streamMapEditor.getScreenManager().setMaxIntervalStart(
					puffer.getMinStartTs());
			this.elementList = this.puffer
					.queryOverlapsAsList(this.streamMapEditor
							.getScreenManager().getInterval());
			this.index = new HashMap<Integer, Quadtree>(this.index.size());
		}
	}

	/**
	 * Updates the interval in the screenManager, e.g. when a connection was
	 * deleted and the maximal possible interval changed
	 *
	 * @param first
	 *            If this is the first connection to update, it will ignore, if
	 *            there was an older / earlier date in the settings of the
	 *            manager -> just sets the times from this buffer as the
	 *            timerange in the manager
	 */
	public boolean updateIntervallInScreenManager(boolean first) {

		// If this LayerUpdater changed anything in the screenManager
		boolean changedSomething = false;

		// Start
		if (puffer.getMinStartTs() != null
				&& (streamMapEditor.getScreenManager().getMaxIntervalStart()
						.after(puffer.getMinStartTs()) || first)) {
			streamMapEditor.getScreenManager().setMaxIntervalStart(
					puffer.getMinStartTs());
			changedSomething = true;
		}

		// End
		if (puffer.getMaxStartTs() != null
				&& (streamMapEditor.getScreenManager().getMaxIntervalEnd()
						.before(puffer.getMaxStartTs()) || first)) {
			streamMapEditor.getScreenManager().setMaxIntervalEnd(
					puffer.getMaxStartTs());
			changedSomething = true;
		}

		return changedSomething;
	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator, IPunctuation point, int port) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param tuple
	 */
	public void addTuple(Tuple<? extends ITimeInterval> tuple) {
		this.getElementList();
		synchronized (elementList) {
			this.elementList.add(tuple);
		}
		ScreenTransformation transformation = this.streamMapEditor
				.getScreenManager().getTransformation();
		int destSrid = this.streamMapEditor.getScreenManager().getSRID();
		for (Entry<Integer, Quadtree> tree : this.index.entrySet()) {
			synchronized (tree) {
				DataSet newDataSet = new DataSet(tuple, tree.getKey(),
						destSrid, transformation);
				tree.getValue().insert(newDataSet.getEnvelope(), newDataSet);
				this.env.get(tree.getKey()).expandToInclude(
						newDataSet.getEnvelope());

			}
		}
		// if (this.configuration.getMaxTupleCount() < this.dataSets.size()){
		// LOG.debug("(REMOVE)Current Geometries:" + dataSets.size());
		// synchronized (dataSets) {
		// DataSet toRemove = this.dataSets.poll();
		// this.tree.remove(toRemove.getEnvelope(), toRemove);
		// }
		// }
	}

	public List<?> query(Envelope searchEnv, int idx) {
		return getTree(idx).query(searchEnv);
	}

	private Quadtree getTree(int idx) {
		Quadtree tree = null;
		synchronized (this.index) {
			tree = this.index.get(idx);
			ScreenTransformation transformation = this.streamMapEditor
					.getScreenManager().getTransformation();
			int destSrid = this.streamMapEditor.getScreenManager().getSRID();
			if (this.srid != destSrid) {
				tree = null;
				this.srid = destSrid;
			}
			if (tree == null) {
				tree = new Quadtree();
				this.env.put(idx, new Envelope());
				getElementList();
				synchronized (elementList) {
					for (Tuple<? extends ITimeInterval> tuple : this
							.getElementList()) {
						DataSet newDataSet = new DataSet(tuple, idx, destSrid,
								transformation);
						tree.insert(newDataSet.getEnvelope(), newDataSet);
						this.env.get(idx).expandToInclude(
								newDataSet.getEnvelope());
					}
				}
				this.index.put(idx, tree);
			}
		}
		return tree;
	}

	@Override
	public boolean add(ILayer e) {
		e.setLayerUpdater(this);
		return super.add(e);
	}

	public List<Tuple<? extends ITimeInterval>> getElementList() {

		if (this.elementList == null) {
			this.elementList = this.puffer
					.queryOverlapsAsList(this.streamMapEditor
							.getScreenManager().getInterval());
		}
		return this.elementList;
	}

	public Envelope getEnvelope(int idx) {
		// TODO Auto-generated method stub
		this.index.get(idx);
		return this.env.get(idx);
	}

	/**
	 * Size of the actual list (not the whole puffer)
	 *
	 * @param idx
	 * @return
	 */
	public int getViewSize() {
		if (elementList != null)
			return this.elementList.size();
		return 0;
	}

	/**
	 * Size of the whole puffer (number of elements)
	 *
	 * @return
	 */
	public int getPufferSize() {
		return puffer.size();
	}

	/**
	 *
	 * @return The maximum size of the puffer
	 */
	public int getMaxPufferSize() {
		return maxNumerOfElements;
	}

	/**
	 *
	 * @param size
	 *            The size which the puffer should have max
	 */
	public void setMaxPufferSize(int size) {
		if (size > 0)
			this.maxNumerOfElements = size;

		// Prevent an overflow in the puffer
		checkForPufferSize();
	}

	/**
	 * Sets the timerange the user wants to save E.g. if he wants to save 60
	 * seconds, the puffer will save all elements with a start-timestamp between
	 * the newest element and the newest timestamp-60 seconds
	 *
	 * The max-puffer-size has a higher priority (if more elements are in the
	 * timerange the puffer will delete the oldest)
	 *
	 * Set to 0, if no time-range should be defined (just the maxPufferSize will
	 * limit the puffer)
	 *
	 * @param seconds
	 */
	public void setTimeRange(int seconds) {
		userDefinedTimeRange = seconds;
		checkForPufferSize();
	}

	/**
	 *
	 * @return Timerange in seconds
	 */
	public int getTimeRange() {
		return userDefinedTimeRange;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("intervalStart".equals(evt.getPropertyName())) {
			this.elementList = this.puffer
					.queryOverlapsAsList(this.streamMapEditor
							.getScreenManager().getInterval());
			this.index = new HashMap<Integer, Quadtree>(this.index.size());
		}
		if ("intervalEnd".equals(evt.getPropertyName())) {
			this.elementList = this.puffer
					.queryOverlapsAsList(this.streamMapEditor
							.getScreenManager().getInterval());
			this.index = new HashMap<Integer, Quadtree>(this.index.size());
		}
	}
}