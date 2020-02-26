/**********************************************************************************
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AbstractTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.state.JoinTIPOState;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;

/**
 * Join operator to process elements with time interval semantics
 *
 * @author Jonas Jacobi, abolles, jan steinke
 * @author Marco Grawunder
 *
 * @param <K>
 *            Metadatentyp
 * @param <T>
 *            Datentyp
 */
public class JoinTIPO<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractPipe<T, T>
		implements IHasPredicate, IStatefulOperator, IStatefulPO, IPhysicalOperatorKeyValueProvider {

	private static Logger LOGGER = LoggerFactory.getLogger(JoinTIPO.class);;

	protected IPredicate<T> joinPredicate;

	protected IDataMergeFunction<T, K> dataMerge;
	protected final IMetadataMergeFunction<K> metadataMerge;
	protected ITransferArea<T, T> transferFunction;
	protected IDummyDataCreationFunction<K, T> creationFunction;

	protected boolean inOrder = true;
	protected Cardinalities card = null;

	protected String[] sweepAreaName;
	private OptionMap options;

	// For the element join (internal element window)
	protected int[] elementSize;
	protected int[][] groupingIndices;
	protected List<Map<Object, ITimeIntervalSweepArea<T>>> groups;

	/*
	 * For implementations that set the SweepAreas from outside with the setAreas
	 * method and cannot set the names.
	 */
	protected boolean useInstanceInsteadOfName = false;
	/**
	 * This object will be used as fallback grouping key.
	 */
	protected static final Serializable DEFAULT_GROUPING_KEY = "";

	protected boolean keepEndtimestamp = false;

	// ------------------------------------------------------------------------------------

	public JoinTIPO(IMetadataMergeFunction<K> metadataMerge) {
		this.metadataMerge = metadataMerge;
		this.elementSize = new int[2];
		this.sweepAreaName = new String[2];
		this.groupingIndices = new int[2][];
		this.groups = new ArrayList<>(2);
		Map<Object, ITimeIntervalSweepArea<T>> groupsPort0 = new HashMap<>();
		Map<Object, ITimeIntervalSweepArea<T>> groupsPort1 = new HashMap<>();
		this.groups.add(groupsPort0);
		this.groups.add(groupsPort1);
	}

	public JoinTIPO(IDataMergeFunction<T, K> dataMerge, IMetadataMergeFunction<K> metadataMerge,
			ITransferArea<T, T> transferFunction) {
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
		this.elementSize = new int[2];
		this.groups = new ArrayList<>(2);
		Map<Object, ITimeIntervalSweepArea<T>> groupsPort0 = new HashMap<>();
		Map<Object, ITimeIntervalSweepArea<T>> groupsPort1 = new HashMap<>();
		this.groups.add(groupsPort0);
		this.groups.add(groupsPort1);
		this.sweepAreaName = new String[2];
		this.groupingIndices = new int[2][];
	}

	public IDataMergeFunction<T, K> getDataMerge() {
		return dataMerge;
	}

	public void setDataMerge(IDataMergeFunction<T, K> dataMerge) {
		this.dataMerge = dataMerge;
	}

	public IMetadataMergeFunction<K> getMetadataMerge() {
		return metadataMerge;
	}

	/**
	 * You can set the areas to use manually from outside. If that is the case, the
	 * SweepAreaRegistry is NOT used to create new instances of the SweepAreas (in
	 * the case that groups are used), but these given areas are used to create new
	 * instances. If you set the name of the SweepArea type after this call, the
	 * SweepAreaRegistry is again used to create instances using that name.
	 * 
	 * @param areas
	 */
	public void setAreas(ITimeIntervalSweepArea<T>[] areas) {
		for (int port = 0; port < this.groups.size(); port++) {
			if (port > 1) {
				break;
			}
			this.groups.get(port).put(DEFAULT_GROUPING_KEY, areas[port]);
			if (this.joinPredicate != null) {
				this.groups.get(port).get(DEFAULT_GROUPING_KEY).setQueryPredicate(this.joinPredicate.clone());
				this.groups.get(port).get(DEFAULT_GROUPING_KEY).setAreaName(this.getName());
			}
		}

		// If this method is used we need to copy instead of use the SweepAreaRegistry
		this.useInstanceInsteadOfName = true;
	}

	public void setOptions(OptionMap options) {
		this.options = options;
	}
	
	public OptionMap getOptions() {
		return options;
	}
	
	public IPredicate<T> getJoinPredicate() {
		return joinPredicate;
	}

	@Override
	public IPredicate<T> getPredicate() {
		return getJoinPredicate();
	}

	public void setJoinPredicate(IPredicate<T> joinPredicate) {
		this.joinPredicate = joinPredicate;
	}

	public void setCardinalities(Cardinalities card) {
		this.card = card;
	}

	public Cardinalities getCardinalities() {
		return this.card;
	}

	public void setSweepAreaName(String port0, String port1) {
		this.sweepAreaName[0] = port0;
		this.sweepAreaName[1] = port1;
		this.useInstanceInsteadOfName = false;
	}

	public void setKeepEndTimestamp(boolean keepEndTimestamp) {
		this.keepEndtimestamp = keepEndTimestamp;
	}

	/**
	 * Set the name of the SweepArea types. If you do so, the SweepAreaRegistry will
	 * be used to create new instances of that type if necessary (e.g., for
	 * grouping)
	 * 
	 * @param both
	 *            The name will be used for both input ports
	 */
	public void setSweepAreaName(String both) {
		this.sweepAreaName[0] = both;
		this.sweepAreaName[1] = both;
		this.useInstanceInsteadOfName = false;
	}

	public String getSweepAreaName(int port) {
		return sweepAreaName[port];
	}

	public String getSweepAreaName() {
		// Old implementation assuming that both are equal
		return sweepAreaName[0];
	}

	public void setTransferFunction(ITransferArea<T, T> transferFunction) {
		this.transferFunction = transferFunction;
	}

	public void setOutOfOrder(boolean outOfOrder) {
		this.inOrder = !outOfOrder;
	}

	/**
	 * For the internal element window (if used). Restrict the number of results to
	 * the n newest elements per port (per group, if grouping is used).
	 * 
	 * @param elementSizePort0
	 * @param elementSizePort1
	 */
	public void setElementSizes(int elementSizePort0, int elementSizePort1) {
		this.elementSize[0] = elementSizePort0;
		this.elementSize[1] = elementSizePort1;
	}

	/**
	 * Indices of attributes for the grouping (if used)
	 * 
	 * @param port0
	 * @param port1
	 */
	public void setGroupingIndices(int[] port0, int[] port1) {
		this.groupingIndices[0] = port0;
		this.groupingIndices[1] = port1;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public boolean deliversStoredElement(int outputPort) {
		return true;
	}

	@Override
	protected synchronized void process_next(T object, int port) {

		// Synchronized to avoid overtaking

		transferFunction.newElement(object, port);

		if (isDone()) {
			callCloseOnSources(port);
			return;
		}

		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);

		earlyCleanUp(object, port, otherport, order);

		// status could change, if the other port was done and
		// its sweeparea is now empty after purging
		if (isDone()) {
			propagateDone();
			return;
		}

		boolean hit = processOutput(object, port, otherport, order);

		Object groupKey = getGroupKey(object, this.groupingIndices[port]);
		insertElement(object, port, hit, groupKey);

		createHeartbeats(port, otherport);
	}

	private boolean processOutput(T object, int port, int otherport, Order order) {
		Iterator<T> qualifies;
		/*
		 * depending on card, delete hits from areas
		 * 
		 * deleting if port is ONE-side
		 * 
		 * cases for ONE_MANY, MANY_ONE:
		 * 
		 * ONE side element is earlier than MANY side elements, nothing will be found
		 * and nothing will be removed
		 * 
		 * ONE side element is later than some MANY side elements, find all
		 * corresponding elements and remove them
		 */
		boolean extract = false;
		if (card != null) {
			switch (card) {
			case ONE_ONE:
				extract = true;
				break;
			case MANY_ONE:
				extract = port == 1;
				break;
			case ONE_MANY:
				extract = port == 0;
				break;
			default:
				break;
			}
		}

		/*
		 * For each group on the other side search for the qualified elements and use
		 * the n newest (if n is set) or all.
		 */
		boolean hit = false;
		for (ITimeIntervalSweepArea<T> sweepArea : groups.get(otherport).values()) {
			qualifies = sweepArea.queryCopy(object, order, extract);
			if (!hit) {
				// If one group had a hit, this element had a hit
				hit = qualifies.hasNext();
			}

			// Internal element window
			if (this.elementSize[otherport] > 0) {
				qualifies = reduceToNewestNElements(qualifies, this.elementSize[otherport]);
			}

			while (qualifies.hasNext()) {
				T next = qualifies.next();
				T newElement = dataMerge.merge(object, next, metadataMerge, order);
				if (this.useElementRestriction(port) && !this.keepEndtimestamp) {
					/*
					 * Remove the end timestamp for correct semantic (later aggregation could have
					 * wrong results when expecting all elements from the window)
					 */
					newElement.getMetadata().setEnd(PointInTime.INFINITY);
				}
				transferFunction.transfer(newElement);
			}
		}
		return hit;
	}

	private void createHeartbeats(int port, int otherport) {
		PointInTime a = getMinStartTs(port);
		PointInTime b = getMinStartTs(otherport);
		PointInTime heartbeat = PointInTime.max(a, b);
		if (heartbeat != null) {
			transferFunction.newHeartbeat(heartbeat, port);
			transferFunction.newHeartbeat(heartbeat, otherport);
		}
	}

	private void earlyCleanUp(T object, int port, int otherport, Order order) {
		if (inOrder && object.isTimeProgressMarker()) {
			for (ITimeIntervalSweepArea<T> sweepArea : groups.get(otherport).values()) {
				sweepArea.purgeElements(object, order);
			}
			this.tryEarlyCleanup(port);
		}
	}

	private void callCloseOnSources(int port) {
		for (IOperatorOwner owner : getOwner()) {
			this.getSubscribedToSource(port).getSource().close(owner);
			this.getSubscribedToSource(port ^ 1).getSource().close(owner);
		}
		return;
	}

	private boolean useElementRestriction(int port) {
		return this.elementSize[port] > 0;
	}

	/**
	 * The minimum start timestamp of all SweepAreas of the given port
	 * 
	 * @param port
	 * @return
	 */
	private PointInTime getMinStartTs(int port) {
		PointInTime minStartTs = null;
		for (ITimeIntervalSweepArea<T> sweepArea : groups.get(port).values()) {
			if (minStartTs == null
					|| (sweepArea.getMinStartTs() != null && sweepArea.getMinStartTs().before(minStartTs))) {
				minStartTs = sweepArea.getMinStartTs();
			}
		}
		return minStartTs;
	}

	/**
	 * Returns an existing or creates and stores a new sweep area for a specific
	 * group.
	 *
	 * @param groupKey
	 *            The group key.
	 * @return An existing or a new sweep area for a specific group.
	 */
	@SuppressWarnings("unchecked")
	protected ITimeIntervalSweepArea<T> getSweepArea(int port, Object groupKey) {
		try {
			ITimeIntervalSweepArea<T> sa = groups.get(port).get(groupKey);
			if (sa == null) {
				if (this.useInstanceInsteadOfName) {
					/*
					 * We need to copy it by ourself as we did not get the name but the instance we
					 * shall use or the type of SweepArea cannot be created using the registry.
					 */
					ISweepArea<T> newInstance = groups.get(port).get(DEFAULT_GROUPING_KEY).newInstance(null);
					if (newInstance instanceof ITimeIntervalSweepArea) {
						sa = (ITimeIntervalSweepArea<T>) newInstance;
					} else {
						throw new InstantiationException(
								"Cannot use a SweepArea from a different type than ITimeIntervalSweepArea.");
					}
				} else {
					SDFSchema ownSchema = port==0?getInputSchema(0):getInputSchema(1);
					SDFSchema otherSchema = port==1?getInputSchema(1):getInputSchema(0);
					sa = (ITimeIntervalSweepArea<T>) SweepAreaRegistry.getSweepArea(this.sweepAreaName[port], getOptions(), ownSchema, otherSchema, getJoinPredicate(), card);
				}
				sa.setQueryPredicate(this.joinPredicate.clone());
				sa.setAreaName(this.sweepAreaName[port]);
				groups.get(port).put(groupKey, sa);
			}
			return sa;
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Error accessing sweep area", e);
		}
		return null;
	}

	protected Object getGroupKey(final T object, final int[] groupingAttributeIndices) {
		return getGroupKey(object, groupingAttributeIndices, DEFAULT_GROUPING_KEY);
	}

	/**
	 * Returns the grouping key for an object.
	 *
	 * If {@code groupingAttributeIndices} is {@code null} or empty, the default
	 * groupkey will be returned.
	 *
	 * @param object
	 *            The object.
	 * @param groupingAttributeIndices
	 *            The indices of the attributes that form the grouping key.
	 * @return The grouping key.
	 */
	public Object getGroupKey(final T object, final int[] groupingAttributeIndices, final Object defaultGroupingKey) {

		if (groupingAttributeIndices == null || groupingAttributeIndices.length == 0) {
			return defaultGroupingKey;
		}

		if (object.isSchemaLess()) {
			return DEFAULT_GROUPING_KEY;
		}

		if (object instanceof Tuple) {
			Tuple<K> tuple = (Tuple<K>) object;

			if (groupingAttributeIndices.length == 1) {
				return tuple.getAttribute(groupingAttributeIndices[0]);
			}
			return Arrays.asList(tuple.restrict(groupingAttributeIndices, true).getAttributes());
		}

		return DEFAULT_GROUPING_KEY;
	}

	@Override
	protected void process_open() {

		if (this.useInstanceInsteadOfName) {
			// We cannot throw away our default SweepAreas
			ITimeIntervalSweepArea<T> defaultSweepAreaPort0 = this.groups.get(0).get(DEFAULT_GROUPING_KEY);
			defaultSweepAreaPort0.clear();

			ITimeIntervalSweepArea<T> defaultSweepAreaPort1 = this.groups.get(1).get(DEFAULT_GROUPING_KEY);
			defaultSweepAreaPort1.clear();

			this.createNewEmptySweepAreaGroups();
			this.groups.get(0).put(DEFAULT_GROUPING_KEY, defaultSweepAreaPort0);
			this.groups.get(1).put(DEFAULT_GROUPING_KEY, defaultSweepAreaPort1);

		} else {
			this.createNewEmptySweepAreaGroups();
		}

		this.dataMerge.init();
		this.metadataMerge.init();
		this.transferFunction.init(this, getSubscribedToSource().size());
	}

	private void createNewEmptySweepAreaGroups() {
		this.groups = new ArrayList<>(2);
		Map<Object, ITimeIntervalSweepArea<T>> groupsPort0 = new HashMap<>();
		Map<Object, ITimeIntervalSweepArea<T>> groupsPort1 = new HashMap<>();
		this.groups.add(groupsPort0);
		this.groups.add(groupsPort1);
	}

	@Override
	protected synchronized void process_close() {
		this.clearAllSweepAreas();
		super.process_close();
	}

	private void clearAllSweepAreas() {
		for (Map<Object, ITimeIntervalSweepArea<T>> groups : this.groups) {
			groups.values().forEach(sweepArea -> sweepArea.clear());
		}
	}

	@Override
	protected void process_done(int port) {
		transferFunction.done(port);
	}

	@Override
	public boolean isDone() {
		try {
			if (getSubscribedToSource(0).isDone()) {
				boolean sweepAreasEmpty = true;
				for (ITimeIntervalSweepArea<T> sweepArea : groups.get(0).values()) {
					if (!sweepArea.isEmpty()) {
						sweepAreasEmpty = false;
						break;
					}
				}
				return getSubscribedToSource(1).isDone() || sweepAreasEmpty;
			}

			boolean sweepAreasEmpty = true;
			for (ITimeIntervalSweepArea<T> sweepArea : groups.get(1).values()) {
				if (!sweepArea.isEmpty()) {
					sweepAreasEmpty = false;
					break;
				}
			}
			return getSubscribedToSource(1).isDone() && getSubscribedToSource(0).isDone() && sweepAreasEmpty;
		} catch (ArrayIndexOutOfBoundsException e) {
			// Can happen if sources are unsubscribed while asking for done
			// Ignore
			return true;
		}
	}

	/**
	 * Insert with default group (no groups are used)
	 * 
	 * @param object
	 * @param port
	 * @param hit
	 */
	public void insertElement(T object, int port, boolean hit) {
		this.insertElement(object, port, hit, DEFAULT_GROUPING_KEY);
	}

	// Depending on card insert elements into sweep area
	public void insertElement(T object, int port, boolean hit, Object groupKey) {

		ITimeIntervalSweepArea<T> sweepArea = this.getSweepArea(port, groupKey);

		if (card == null || card == Cardinalities.MANY_MANY) {
			sweepArea.insert(object);
		} else {
			switch (card) {
			case ONE_ONE:
				// If one to one case, a hit cannot be produce another hit
				if (!hit) {
					sweepArea.insert(object);
				}
				break;
			case ONE_MANY:
				// If from left insert
				// if from right and no hit, insert (corresponding left
				// element not found now)
				if (port == 0 || (port == 1 && !hit)) {
					sweepArea.insert(object);
				}
				break;
			case MANY_ONE:
				// If from rightt insert
				// if from left and no hit, insert (corresponding right
				// element not found now)
				if (port == 1 || (port == 0 && !hit)) {
					sweepArea.insert(object);
				}
				break;
			default:
				sweepArea.insert(object);
				break;
			}
		}
	}

	public boolean isAreasSet() {

		for (Map<Object, ITimeIntervalSweepArea<T>> group : this.groups) {
			if (group.get(DEFAULT_GROUPING_KEY) != null) {
				return true;
			}
		}

		return false;
	}

	public ITransferArea<T, T> getTransferFunction() {
		return transferFunction;
	}

	public IDummyDataCreationFunction<K, T> getCreationFunction() {
		return creationFunction;
	}

	public void setCreationFunction(IDummyDataCreationFunction<K, T> creationFunction) {
		this.creationFunction = creationFunction;
	}

	@Override
	public synchronized void processPunctuation(IPunctuation inPunctuation, int port) {
		IPunctuation punctuation = joinPredicate.processPunctuation(inPunctuation);
		if (punctuation.isHeartbeat()) {

			// First a quick normal cleanup based on end timestamps
			for (ITimeIntervalSweepArea<T> sweepArea : groups.get(port ^ 1).values()) {
				sweepArea.purgeElementsBefore(punctuation.getTime());
			}

			// Maybe some more elements can be removed based on the start timestamp
			this.tryEarlyCleanup(port);
		}
		this.transferFunction.sendPunctuation(punctuation);
		this.transferFunction.newElement(punctuation, port);
	}

	/**
	 * If the element restriction is used, the SweepAreas can be cleaned earlier
	 * based on the start timestamps / age of the elements. The SweepAreas can be
	 * reduced to the n newest elements. If this is possible, it is done here.
	 *
	 * @param port
	 *            The port where the timestamp is from.
	 */
	private void tryEarlyCleanup(int port) {
		int otherport = port ^ 1;

		if (this.elementSize[otherport] < 0) {
			return;
		}

		for (ITimeIntervalSweepArea<T> sweepArea : groups.get(otherport).values()) {
			while (sweepArea.size() > this.elementSize[otherport]) {
				try {
					sweepArea.poll();
				} catch (UnsupportedOperationException e) {
					/*
					 * Early cleanup is not possible with this type of SweepArea: we will simply not
					 * do this optimization
					 */
					break;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof JoinTIPO)) {
			return false;
		}
		JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>> jtipo = (JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>>) ipo;

		if (this.card != jtipo.card) {
			return false;
		}

		if (!dataMerge.getClass().toString().equals(jtipo.dataMerge.getClass().toString())
				|| !metadataMerge.getClass().toString().equals(jtipo.metadataMerge.getClass().toString())
				|| !creationFunction.getClass().toString().equals(jtipo.creationFunction.getClass().toString())) {
			return false;
		}

		// Vergleichen des Join-Predicates und des Output-Schemas
		return (this.getJoinPredicate().equals(jtipo.getJoinPredicate())
				&& this.getOutputSchema().compareTo(jtipo.getOutputSchema()) == 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isContainedIn(IPipe<T, T> ip) {
		if (!(ip instanceof JoinTIPO)) {
			return false;
		}
		JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>> jtipo = (JoinTIPO<? extends ITimeInterval, ? extends IStreamObject<K>>) ip;

		if (this.card != jtipo.card) {
			return false;
		}

		if (!dataMerge.getClass().toString().equals(jtipo.dataMerge.getClass().toString())
				|| !metadataMerge.getClass().toString().equals(jtipo.metadataMerge.getClass().toString())
				|| !creationFunction.getClass().toString().equals(jtipo.creationFunction.getClass().toString())) {
			return false;
		}

		// Vergleichen des Join-Predicates
		return (this.getJoinPredicate().isContainedIn(jtipo.getJoinPredicate()));
	}

	/**
	 * Returns the latest endtimestamp in all sweepareas.
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PointInTime getLatestEndTimestamp() {
		PointInTime max = null;
		for (int i = 0; i < 2; i++) {
			for (ITimeIntervalSweepArea<T> sweepArea : groups.get(i).values()) {
				synchronized (sweepArea) {
					PointInTime maxi = ((ITimeIntervalSweepArea<IStreamObject<? extends ITimeInterval>>) sweepArea)
							.getMaxEndTs();
					if (max == null || (maxi != null && maxi.after(max))) {
						max = maxi;
					}
				}
			}
		}
		return PointInTime.max(max, transferFunction.calcMaxEndTs());
	}

	@Override
	public String toString() {
		return super.toString() + " predicate: " + this.joinPredicate;
	}

	@Override
	public long getElementsStored1() {
		long count = 0;
		for (ITimeIntervalSweepArea<T> sweepArea : groups.get(0).values()) {
			count += sweepArea.size();
		}
		return count;
	}

	@Override
	public long getElementsStored2() {
		long count = 0;
		for (ITimeIntervalSweepArea<T> sweepArea : groups.get(1).values()) {
			count += sweepArea.size();
		}
		return count;
	}

	@Override
	public IOperatorState getState() {
		JoinTIPOState<K, T> state = new JoinTIPOState<>();
		state.setGroups(this.groups);
		state.setTransferArea(transferFunction);
		return state;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setStateInternal(Serializable s) {
		try {
			JoinTIPOState<K, T> state = (JoinTIPOState<K, T>) s;

			// set sweep area from state
			synchronized (this.groups) {
				for (int port = 0; port < state.getGroups().size(); port++) {
					for (Object groupKey : state.getGroups().get(port).keySet()) {
						ISweepArea sweepArea = state.getGroups().get(port).get(groupKey);

						// save the remove predicate
						IPredicate<? super IStreamObject<? extends ITimeInterval>> tempRemovePredicate = null;
						if (sweepArea instanceof ITimeIntervalSweepArea) {
							ITimeIntervalSweepArea timeSweepArea = (ITimeIntervalSweepArea<IStreamObject<? extends ITimeInterval>>) sweepArea;
							tempRemovePredicate = timeSweepArea.getRemovePredicate();

							// set the sweep area from the state
							this.groups.get(port).put(groupKey, timeSweepArea);

							// set and initialize query and remove predicate
							if (this.joinPredicate != null && tempRemovePredicate != null) {
								timeSweepArea.setQueryPredicate(this.joinPredicate);
								timeSweepArea.setRemovePredicate(tempRemovePredicate);
								timeSweepArea.init();
							} else {
								throw new IllegalArgumentException(
										"query predicate or remove predicate must not be null");
							}

						} else {
							throw new IllegalArgumentException(
									"sweepArea type " + sweepArea.getName() + " is not supported");
						}
					}
				}
			}

			// set transfer area from state
			this.transferFunction = state.getTransferArea();
			this.transferFunction.setTransfer(this);
		} catch (Exception e) {
			LOGGER.error("Error setting state for JoinTIPO. Error: ", e);
		}
	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> map = new HashMap<>();
		map.put("Left Area Implementation", this.sweepAreaName[0]);
		map.put("Right Area Implementation", this.sweepAreaName[1]);
		map.put("Left Area Size", getElementsStored1() + "");
		map.put("Right Area Size", getElementsStored2() + "");

		ITimeIntervalSweepArea<T> sampleSweepAreaPort0 = this.getSweepArea(0, DEFAULT_GROUPING_KEY);
		ITimeIntervalSweepArea<T> sampleSweepAreaPort1 = this.getSweepArea(1, DEFAULT_GROUPING_KEY);
		if (sampleSweepAreaPort0 instanceof AbstractTISweepArea
				&& sampleSweepAreaPort1 instanceof AbstractTISweepArea) {
			map.put("Left Area Has End TS Order", ((AbstractTISweepArea<?>) sampleSweepAreaPort0).hasEndTsOrder() + "");
			map.put("Right Area Has End TS Order",
					((AbstractTISweepArea<?>) sampleSweepAreaPort1).hasEndTsOrder() + "");
		}

		if (sampleSweepAreaPort0 instanceof IPhysicalOperatorKeyValueProvider) {
			for (Iterator<Entry<String, String>> iter = ((IPhysicalOperatorKeyValueProvider) sampleSweepAreaPort0)
					.getKeyValues().entrySet().iterator(); iter.hasNext();) {
				Entry<String, String> e = iter.next();
				map.put("Left Area - " + e.getKey(), e.getValue());
			}
		}
		if (sampleSweepAreaPort1 instanceof IPhysicalOperatorKeyValueProvider) {
			for (Iterator<Entry<String, String>> iter = ((IPhysicalOperatorKeyValueProvider) sampleSweepAreaPort1)
					.getKeyValues().entrySet().iterator(); iter.hasNext();) {
				Entry<String, String> e = iter.next();
				map.put("Right Area - " + e.getKey(), e.getValue());
			}
		}

		map.put("OutputQueue", transferFunction.size() + "");
		map.put("Watermark", transferFunction.getWatermark() + "");
		return map;
	}

	/**
	 * This method is used to implement the element-window behavior of the join
	 * operator for an interval element window. It reduces the qualified elements to
	 * the given n newest ones.
	 * 
	 * @param qualifies
	 *            The unsorted, full set of qualified elements.
	 * @param n
	 *            The amount of the newest elements which are needed
	 * @return An iterator for the n newest qualified elements. In case that there
	 *         are less qualified elements than n, the original iterator is
	 *         returned.
	 */
	private Iterator<T> reduceToNewestNElements(Iterator<T> qualifies, int n) {
		List<T> elements = new ArrayList<>();

		while (qualifies.hasNext()) {
			elements.add(qualifies.next());
		}

		if (elements.size() < n) {
			return elements.iterator();
		}

		elements.sort(new Comparator<T>() {

			// Comparator so that the newest elements (highest start) are first
			@Override
			public int compare(T o1, T o2) {
				if (o1.getMetadata().getStart().after(o2.getMetadata().getStart())) {
					return -1;
				} else if (o1.getMetadata().getStart().before(o2.getMetadata().getStart())) {
					return 1;
				}
				return 0;
			}
		});

		return elements.subList(0, n).iterator();
	}

	@Override
	public void setPredicate(IPredicate<?> predicate) {
		// TODO
	}
}
