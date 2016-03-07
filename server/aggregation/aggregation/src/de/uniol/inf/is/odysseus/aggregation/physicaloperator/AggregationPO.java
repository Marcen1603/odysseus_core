/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.aggregation.physicaloperator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.sweeparea.IAggregationSweepArea;
import de.uniol.inf.is.odysseus.aggregation.sweeparea.IndexedByEndTsAggregationSweepArea;
import de.uniol.inf.is.odysseus.aggregation.sweeparea.StartTsTimeOrderedAggregationSweepArea;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Cornelius Ludmann
 *
 */
public class AggregationPO<M extends ITimeInterval, T extends Tuple<M>> extends AbstractPipe<T, T>
		implements IPhysicalOperatorKeyValueProvider, IStatefulPO {

	protected static Logger LOG = LoggerFactory.getLogger(AggregationPO.class);

	/**
	 * This object will be used as fallback grouping key.
	 */
	protected Serializable defaultGroupingKey = "";

	/**
	 * A map of points in time where elements get invalid to a set of grouping
	 * keys of these groups that has elements that become invalid at this point
	 * in time.
	 */
	protected TreeMap<PointInTime, Set<Object>> outdatingGroups = new TreeMap<>();

	/**
	 * A map of group keys to a sweep area that holds the elements of each
	 * group.
	 */
	protected Map<Object, IAggregationSweepArea<M, T>> groups = new HashMap<>();

	/**
	 * This flag will be set to true when an element with end TS arrives this
	 * operator. Otherwise this is false and we do not need to check for
	 * outdating tuples because all tuples are valid forever.
	 */
	protected boolean hasOutdatingElements = false;

	/**
	 * The attribute indices of the incoming elements that form the grouping
	 * key.
	 */
	protected final int[] groupingAttributesIndices;

	/**
	 * A list of functions that get all valid elements of a point in time to
	 * calculate the aggregation. These functions do not have a state.
	 */
	protected final List<INonIncrementalAggregationFunction<M, T>> nonIncrementalFunctions;

	/**
	 * A list of functions that calculate the aggregation incrementally. They
	 * get only updates (new or outdated elements).
	 */
	protected final List<IIncrementalAggregationFunction<M, T>> incrementalFunctions;

	/**
	 * This map holds instances of incremental functions for each group.
	 */
	protected Map<Object, List<IIncrementalAggregationFunction<M, T>>> incrementalFunctionsForGroup = new HashMap<>();

	/**
	 * This flag is set if this operator has incremental functions. Shortcut for
	 * !incrementalFunctions.isEmpty().
	 */
	protected final boolean hasIncrementalFunctions;

	/**
	 * This flag is set if this operator has non-incremental functions. Shortcut
	 * for !nonIncrementalFunctions.isEmpty().
	 */
	protected final boolean hasNonIncrementalFunctions;

	/**
	 * There are sweep areas that return the valid tuples
	 * {@link IAggregationSweepArea#getValidTuples()} in start TS order and
	 * others don't. If we have at least one non-incremental function that needs
	 * start TS order
	 * {@link INonIncrementalAggregationFunction#needsOrderedElements()}, this
	 * flag ist {@code true}.
	 */
	protected final boolean hasFunctionsThatNeedStartTsOrder;

	/**
	 * This flag is set if this operator should output new elements when
	 * elements get outdated.
	 */
	protected final boolean evaluateAtOutdatingElements;
	protected final boolean evaluateBeforeRemovingOutdatingElements;

	/**
	 * This flag is set if this operator should output new elements when
	 * elements get valid.
	 */
	protected final boolean evaluateAtNewElement;

	/**
	 * This flag is set if this operator should output the last output element
	 * at done. This can be used when you want only the final aggr. value in an
	 * evaluation. E. g., the final AVG of the latency.
	 */
	protected final boolean evaluateAtDone;

	protected final boolean outputOnlyChanges;

	protected final Map<Object, T> lastOutput;

	/**
	 * The output schema.
	 */
	protected final SDFSchema outputSchema;

	/**
	 * The timestamp of the last incoming element.
	 */
	long watermark = 0l;

	/**
	 * The timestamp of the last outgoing element.
	 */
	long watermarkOut = 0l;

	/**
	 * Constructor.
	 * 
	 * @param nonIncrementalFunctions
	 *            A list of all non-incremental functions.
	 * @param incrementalFunctions
	 *            A list of all incremental functions.
	 * @param evaluateAtOutdatingElements
	 *            True if this operator should output new elements when elements
	 *            get outdated.
	 * @param evaluateAtNewElement
	 * 
	 *            True, if this operator should output new elements when
	 *            elements get valid.
	 * @param evaluateAtDone
	 *            True, if this operator should output the last output element
	 *            at done. This can be used when you want only the final aggr.
	 *            value in an evaluation. E. g., the final AVG of the latency.
	 * @param outputSchema
	 *            The output schema of this operator.
	 * @param groupingAttributesIdx
	 *            The indices that form the grouping attributes.
	 */
	public AggregationPO(final List<INonIncrementalAggregationFunction<M, T>> nonIncrementalFunctions,
			final List<IIncrementalAggregationFunction<M, T>> incrementalFunctions,
			final boolean evaluateAtOutdatingElements, final boolean evaluateBeforeRemovingOutdatingElements,
			final boolean evaluateAtNewElement, final boolean evaluateAtDone, final boolean outputOnlyChanges,
			final SDFSchema outputSchema, final int[] groupingAttributesIdx) {
		// REMARK: Consider safe copies.
		this.nonIncrementalFunctions = Collections.unmodifiableList(nonIncrementalFunctions);
		this.incrementalFunctions = Collections.unmodifiableList(incrementalFunctions);
		hasNonIncrementalFunctions = !nonIncrementalFunctions.isEmpty();
		hasIncrementalFunctions = !incrementalFunctions.isEmpty();
		this.evaluateAtDone = evaluateAtDone;
		this.evaluateAtNewElement = evaluateAtNewElement;
		this.evaluateAtOutdatingElements = evaluateAtOutdatingElements;
		this.evaluateBeforeRemovingOutdatingElements = evaluateBeforeRemovingOutdatingElements;
		this.outputOnlyChanges = outputOnlyChanges;
		if (outputOnlyChanges) {
			lastOutput = new HashMap<>();
		} else {
			lastOutput = null;
		}
		this.outputSchema = outputSchema;
		this.groupingAttributesIndices = groupingAttributesIdx;

		this.hasFunctionsThatNeedStartTsOrder = this.nonIncrementalFunctions.stream()
				.anyMatch(e -> e.needsOrderedElements());

	}

	/**
	 * Copy constructor.
	 */
	public AggregationPO(final AggregationPO<M, T> other) {
		// REMARK: Consider safe copies.
		this.nonIncrementalFunctions = other.nonIncrementalFunctions;
		this.incrementalFunctions = other.incrementalFunctions;
		this.hasNonIncrementalFunctions = other.hasNonIncrementalFunctions;
		this.hasIncrementalFunctions = other.hasIncrementalFunctions;
		this.evaluateAtDone = other.evaluateAtDone;
		this.evaluateAtNewElement = other.evaluateAtDone;
		this.evaluateAtOutdatingElements = other.evaluateAtOutdatingElements;
		this.evaluateBeforeRemovingOutdatingElements = other.evaluateBeforeRemovingOutdatingElements;
		this.outputOnlyChanges = other.outputOnlyChanges;
		this.lastOutput = other.lastOutput;
		this.outputSchema = other.outputSchema;
		this.groupingAttributesIndices = other.groupingAttributesIndices;
		this.hasFunctionsThatNeedStartTsOrder = other.hasFunctionsThatNeedStartTsOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_open()
	 */
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		clear();
	}

	/**
	 * Clears the state of this operator.
	 */
	private void clear() {
		groups.clear();
		incrementalFunctionsForGroup.clear();
		outdatingGroups.clear();
		watermark = 0l;
		watermarkOut = 0l;
		hasOutdatingElements = false;
		if (lastOutput != null) {
			lastOutput.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.ISink#processPunctuation(
	 * de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
	 */
	@Override
	public void processPunctuation(final IPunctuation punctuation, final int port) {
		// TODO: Process outdated elements that are before the punctuation.
		// sendPunctuation(punctuation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected synchronized void process_next(final T object, final int port) {

		if (!hasOutdatingElements) {
			if (!object.getMetadata().getEnd().isInfinite()) {
				hasOutdatingElements = true;
			}
		}

		final Object groupKey = getGroupKey(object, groupingAttributesIndices);

		if (this.watermark > object.getMetadata().getStart().getMainPoint()) {
			LOG.error("Element " + object + " is out of order. Watermark " + watermark + " > "
					+ object.getMetadata().getStart().getMainPoint());
		} else {
			this.watermark = object.getMetadata().getStart().getMainPoint();
		}

		// We process outdated elements, iff the following two conditions are
		// true:
		// (a) We have outdating elements: Otherwise there is nothing to
		// process.
		// (b) We want to produce an output when elements get outdated OR we
		// have incremental functions.
		// When we do not want to produce an output when elements get outdated
		// but we have incremental functions, than we need to update the state
		// of the incremental functions, though.
		if (hasOutdatingElements && (evaluateAtOutdatingElements || hasIncrementalFunctions)) {
			processOutdatedElements(object, groupKey);
		}

		if (evaluateAtNewElement || hasIncrementalFunctions) {
			processNewElement(object, groupKey);
		}
	}

	/**
	 * Processes all outdated points before the trigger element.
	 * 
	 * @param trigger
	 *            The new element that trigger this processing.
	 * @param triggerGroupKey
	 *            The group key of the trigger element,
	 */
	private void processOutdatedElements(final T trigger, final Object triggerGroupKey) {
		// Iterate over all points in time that are before trigger start TS.
		for (final Iterator<Entry<PointInTime, Set<Object>>> iter = outdatingGroups
				.headMap(trigger.getMetadata().getStart(), true).entrySet().iterator(); iter.hasNext();) {

			final Entry<PointInTime, Set<Object>> entry = iter.next();
			final PointInTime pointInTime = entry.getKey();

			// for all groups that has outdated elements at pointInTime
			for (final Object gkey : entry.getValue()) {

				// if this group the group of the trigger element?
				boolean triggerGroup;
				if (triggerGroupKey == null) {
					triggerGroup = gkey == null;
				} else {
					triggerGroup = triggerGroupKey.equals(gkey);
				}

				processOutdatedForGroup(getSweepArea(gkey), trigger, gkey, triggerGroup, pointInTime);
			}
			// remove point in time
			iter.remove();
		}
	}

	/**
	 * Processes the outdated elements of a group for a specific point in time
	 * and transfers the result.
	 * 
	 * @param sweepArea
	 *            The sweep area of the group.
	 * @param trigger
	 *            The element that triggers the calculation.
	 * @param groupKey
	 *            The group key of the group.
	 * @param isTriggerGroup
	 *            True, iff the the group is the group of the trigger element.
	 * @param pointInTime
	 *            The point in time where the element set changed due to invalid
	 *            elements in the sweep area.
	 */
	private void processOutdatedForGroup(final IAggregationSweepArea<M, T> sweepArea, final T trigger,
			final Object groupKey, final boolean isTriggerGroup, final PointInTime pointInTime) {

		// We output a result only if the flag evaluateAtOutdatingElements is
		// set true AND the outdating elements are not elements of the trigger
		// group that ends when the trigger element gets valid. In the latter
		// case, the result will be output by the processing of the trigger
		// element.
		final boolean evaluate = (evaluateAtOutdatingElements || evaluateBeforeRemovingOutdatingElements)
				&& (!evaluateAtNewElement || !(isTriggerGroup && pointInTime.equals(trigger.getMetadata().getStart())));

		// If we have incremental functions we need to update the state of these
		// functions even when we not want to calculate the result.
		if (evaluate || hasIncrementalFunctions) {
			// TODO: Do we need deep copy here? Depends on the values of the
			// functions, doesn't it?
			// If we do not want to produce an output (!evaluate) we set result
			// to null. The following methods have to respect this.
			@SuppressWarnings("unchecked")
			final T result = evaluate ? (T) new Tuple<>(outputSchema.size(), true) : null;

			// To get the grouping attributes, we need a sample of the group
			// to extract the grouping attributes.
			T sampleOfGroup = null;

			if (evaluate && hasNonIncrementalFunctions && evaluateBeforeRemovingOutdatingElements) {
				// get all valid valid tuples
				final Collection<T> objects = sweepArea.getValidTuples();

				if (sampleOfGroup == null && objects != null && !objects.isEmpty()) {
					sampleOfGroup = objects.iterator().next();
				}
				processNonIncrementalFunctions(result, objects, trigger, pointInTime);
			}

			// Get and remove outdated elements.
			final Collection<T> outdatedTuples = sweepArea.getOutdatedTuples(pointInTime, true);

			if (hasIncrementalFunctions) {
				final List<IIncrementalAggregationFunction<M, T>> statefulFunctionsForKey = getStatefulFunctions(
						groupKey);
				processStatefulFunctionsRemove(result, statefulFunctionsForKey, outdatedTuples, trigger, pointInTime);
			}

			if (evaluate) {

				if (isTriggerGroup) {
					// If this group is the same as the trigger element group,
					// the sample of the group is the trigger. Thats easy.
					sampleOfGroup = trigger;
				} else if (outdatedTuples != null && !outdatedTuples.isEmpty()) {
					// Otherwise use the first outdating element as sample of
					// the group (if we have one).
					sampleOfGroup = outdatedTuples.iterator().next();
				}

				if (hasNonIncrementalFunctions && !evaluateBeforeRemovingOutdatingElements) {
					// get all valid valid tuples
					final Collection<T> objects = sweepArea.getValidTuples();

					if (sampleOfGroup == null && objects != null && !objects.isEmpty()) {
						// Last chance for a sample of this group: the first
						// valid element.
						sampleOfGroup = objects.iterator().next();
					}
					processNonIncrementalFunctions(result, objects, trigger, pointInTime);
				}

				if (result != null) {
					transferResult(result, trigger, pointInTime, sampleOfGroup);
				}

			}

			// We do not remove this empty group if this group is the trigger
			// group because we have a new element (the trigger) that has to be
			// stored in this group.
			if (!isTriggerGroup) {
				removeGroupIfEmpty(sweepArea, groupKey);
			}
		}
	}

	/**
	 * Processes the new element.
	 * 
	 * @param object
	 *            The element.
	 * @param groupKey
	 *            The group key of the element.
	 */
	private void processNewElement(final T object, final Object groupKey) {

		// We use this object to store the results of the functions. This object
		// is null iff no output should be created.
		// TODO: Do we need deep copy here? Depends on the values of the
		// functions, doesn't it?
		@SuppressWarnings("unchecked")
		final T result = evaluateAtNewElement ? (T) new Tuple<>(outputSchema.size(), true) : null;

		IAggregationSweepArea<M, T> sa = null;

		// We need to store the new element in a sweep area iff we have non
		// incremental functions (than we need to invoke these functions with
		// all valid tuples therefore we need to store all elements) or the
		// element gets invalid (than we need to know the invalid elements even
		// we have only incremental functions to invoke them with the outdated
		// elements).
		if (hasNonIncrementalFunctions || !object.getMetadata().getEnd().isInfinite()) {
			sa = getSweepArea(groupKey);

			// If we have non-incremental functions we need to save all elements
			// because we invoke these functions with all valid tuples.
			// If we do not have non-incremental functions (only incremental
			// functions) we need to save only elements that gets invalid we
			// invoke these functions only with the new element or with a list
			// of outdated elements and never with all valid elements.
			sa.addElement(object, hasNonIncrementalFunctions);

			addToOutdatingGroups(object.getMetadata().getEnd(), groupKey);
		}

		if (hasIncrementalFunctions) {
			final List<IIncrementalAggregationFunction<M, T>> statefulFunctionsForKey = getStatefulFunctions(groupKey);
			processStatefulFunctionsAdd(result, statefulFunctionsForKey, object);
		}

		if (hasNonIncrementalFunctions && evaluateAtNewElement) {
			final Collection<T> objects = sa.getValidTuples();
			processNonIncrementalFunctions(result, objects, object, object.getMetadata().getStart());
		}

		if (result != null) {
			transferResult(result, object, object.getMetadata().getStart());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.
	 * IPhysicalOperatorKeyValueProvider#getKeyValues()
	 */
	@Override
	public Map<String, String> getKeyValues() {
		final Map<String, String> result = new HashMap<>();
		result.put("Grouping Attribute Indices", "" + Arrays.toString(groupingAttributesIndices));
		result.put("Groups", "" + groups.size());
		result.put("Groups Stateful Functions Map", "" + incrementalFunctionsForGroup.size());
		result.put("Has Stateful Functions", "" + hasIncrementalFunctions);
		result.put("No of Stateful Functions", "" + incrementalFunctions.size());
		result.put("Has Stateless Functions", "" + hasNonIncrementalFunctions);
		result.put("No of Stateless Functions", "" + nonIncrementalFunctions.size());
		result.put("Has Outdating Elements", "" + hasOutdatingElements);
		result.put("No of Outdating Points:", "" + outdatingGroups.size());
		result.put("Watermark:", "" + watermark);
		result.put("Watermark Out:", "" + watermarkOut);
		if (incrementalFunctionsForGroup.size() == 1) {
			final List<IIncrementalAggregationFunction<M, T>> functions = incrementalFunctionsForGroup.values()
					.iterator().next();
			for (final IIncrementalAggregationFunction<M, T> f : functions) {
				if (f instanceof IPhysicalOperatorKeyValueProvider) {
					result.putAll(((IPhysicalOperatorKeyValueProvider) f).getKeyValues());
				}
			}
		}
		result.put("eval outdating", "" + evaluateAtOutdatingElements);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/**
	 * @param time
	 * @param groupKey
	 */
	private void addToOutdatingGroups(final PointInTime time, final Object groupKey) {
		if (!time.isInfinite()) {
			Set<Object> s = outdatingGroups.get(time);
			if (s == null) {
				s = new LinkedHashSet<>();
				outdatingGroups.put(time, s);
			}
			s.add(groupKey);
		}
	}

	/**
	 * @param result
	 * @param objects
	 */
	private void processNonIncrementalFunctions(final T result, final Collection<T> objects, final T trigger,
			final PointInTime pointInTime) {
		for (final INonIncrementalAggregationFunction<M, T> function : nonIncrementalFunctions) {
			final Object[] result2 = function.evaluate(objects, trigger, pointInTime);
			for (int i = 0; i < result2.length; ++i) {
				result.setAttribute(function.getOutputAttributeIndices()[i], result2[i]);
			}
		}
	}

	/**
	 * @param result
	 * @param statefulFunctionsForKey
	 * @param object
	 */
	private void processStatefulFunctionsAdd(final T result,
			final List<IIncrementalAggregationFunction<M, T>> statefulFunctionsForKey, final T object) {
		if (result == null) {
			for (final IIncrementalAggregationFunction<M, T> function : statefulFunctionsForKey) {
				function.addNew(object);
			}
		} else {
			for (final IIncrementalAggregationFunction<M, T> function : statefulFunctionsForKey) {
				final Object[] result2 = function.addNewAndEvaluate(object);
				for (int i = 0; i < result2.length; ++i) {
					result.setAttribute(function.getOutputAttributeIndices()[i], result2[i]);
				}
			}
		}

	}

	private void processStatefulFunctionsRemove(final T result,
			final List<IIncrementalAggregationFunction<M, T>> statefulFunctionsForKey,
			final Collection<T> outdatedTuples, final T trigger, final PointInTime pointInTime) {

		if (result == null) {
			for (final IIncrementalAggregationFunction<M, T> function : statefulFunctionsForKey) {
				function.removeOutdated(outdatedTuples, trigger, pointInTime);
			}
		} else {
			for (final IIncrementalAggregationFunction<M, T> function : statefulFunctionsForKey) {
				Object[] result2;
				if (evaluateBeforeRemovingOutdatingElements) {
					result2 = function.evalute(trigger, pointInTime);
					result2 = Arrays.copyOf(result2, result2.length);
					function.removeOutdated(outdatedTuples, trigger, pointInTime);
				} else {
					result2 = function.removeOutdatedAndEvaluate(outdatedTuples, trigger, pointInTime);
				}
				for (int i = 0; i < result2.length; ++i) {
					result.setAttribute(function.getOutputAttributeIndices()[i], result2[i]);
				}
			}
		}
	}

	/**
	 * Returns an existing or creates and stores a new sweep area for a specific
	 * group.
	 * 
	 * @param groupKey
	 *            The group key.
	 * @return An existing or a new sweep area for a specific group.
	 */
	private IAggregationSweepArea<M, T> getSweepArea(final Object groupKey) {
		IAggregationSweepArea<M, T> sa = groups.get(groupKey);
		if (sa == null) {
			if (hasFunctionsThatNeedStartTsOrder) {
				sa = new StartTsTimeOrderedAggregationSweepArea<>();
			} else {
				sa = new IndexedByEndTsAggregationSweepArea<>();
			}
			groups.put(groupKey, sa);
		}
		return sa;
	}

	/**
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	private void transferResult(final T result, final T trigger, final PointInTime startTs, final T sampleOfGroup) {

		if (onlyNullAttributes(result)) {
			return;
		}

		if (watermarkOut > startTs.getMainPoint()) {
			LOG.warn("Out element " + result + " triggerd by " + trigger + " for start ts " + startTs
					+ " is out of order!");
		} else {
			watermarkOut = startTs.getMainPoint();
		}

		setGroupingAttributes(sampleOfGroup, result);

		boolean output = true;
		if (outputOnlyChanges) {
			final Object groupKey = getGroupKey(result, groupingAttributesIndices, defaultGroupingKey);
			final T last = lastOutput.get(groupKey);
			output = !result.equals(last);
			if (output) {
				lastOutput.put(groupKey, (T) result.clone());
			}
		}

		if (output) {
			final M meta = (M) trigger.getMetadata().clone();
			meta.setEnd(PointInTime.INFINITY);
			meta.setStart(startTs);
			result.setMetadata(meta);
			transfer(result);
		}
	}

	/**
	 * @param result
	 * @return
	 */
	private boolean onlyNullAttributes(final T result) {
		for (final Object attr : result.getAttributes()) {
			if (attr != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param result
	 */
	private void transferResult(final T result, final T trigger, final PointInTime startTs) {
		transferResult(result, trigger, startTs, trigger);
	}

	/**
	 * @param groupKey
	 * @return
	 */
	private List<IIncrementalAggregationFunction<M, T>> getStatefulFunctions(final Object groupKey) {
		List<IIncrementalAggregationFunction<M, T>> statefulFunctionsForKey = incrementalFunctionsForGroup
				.get(groupKey);
		if (statefulFunctionsForKey == null) {
			statefulFunctionsForKey = new ArrayList<>(incrementalFunctions.size());
			for (final IIncrementalAggregationFunction<M, T> f : incrementalFunctions) {
				statefulFunctionsForKey.add(f.clone());
			}
			incrementalFunctionsForGroup.put(groupKey, statefulFunctionsForKey);
		}
		return statefulFunctionsForKey;
	}

	/**
	 * @param groupKey
	 * @param groupKey2
	 */
	private void removeGroupIfEmpty(final IAggregationSweepArea<M, T> sa, final Object groupKey) {
		if (!sa.hasValidTuples()) {
			groups.remove(groupKey);
			incrementalFunctionsForGroup.remove(groupKey);
		}
	}

	/**
	 * @param result
	 */
	private void setGroupingAttributes(final T sampleOfGroup, final T result) {
		if (sampleOfGroup != null) {
			int i = 0;
			for (final int x : groupingAttributesIndices) {
				result.setAttribute(i, sampleOfGroup.getAttribute(x));
				++i;
			}
		}
	}

	protected Object getGroupKey(final T object, final int[] groupingAttributeIndices) {
		return getGroupKey(object, groupingAttributeIndices, defaultGroupingKey);
	}

	/**
	 * Returns the grouping key for an object.
	 * 
	 * <p>
	 * If {@code groupingAttributeIndices} is {@code null} or empty,
	 * {@link AggregationPO#defaultGroupingKey} will be returned.
	 * 
	 * @param object
	 *            The object.
	 * @param groupingAttributeIndices
	 *            The indices of the attributes that form the grouping key.
	 * @return The grouping key.
	 */
	public static <T extends Tuple<?>> Object getGroupKey(final T object, final int[] groupingAttributeIndices,
			final Object defaultGroupingKey) {
		if (groupingAttributeIndices == null || groupingAttributeIndices.length == 0) {
			return defaultGroupingKey;
		}
		if (groupingAttributeIndices.length == 1) {
			return object.getAttribute(groupingAttributeIndices[0]);
		}
		return Arrays.asList(object.restrict(groupingAttributeIndices, true).getAttributes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#isDone
	 * ()
	 */
	@Override
	public boolean isDone() {
		return super.isDone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_done()
	 */
	@Override
	protected void process_done() {
		super.process_done();
		if (evaluateAtDone) {

			for (final Entry<Object, IAggregationSweepArea<M, T>> group : groups.entrySet()) {
				final Object key = group.getKey();
				final IAggregationSweepArea<M, T> sa = group.getValue();

				// TODO: Do we need deep copy here? Depends on the values of the
				// functions, doesn't it?
				@SuppressWarnings("unchecked")
				final T result = (T) new Tuple<>(outputSchema.size(), true);

				T sampleOfGroup = null;
				final Collection<T> elements = sa.getValidTuples();
				if (!elements.isEmpty()) {
					sampleOfGroup = elements.iterator().next();
				}

				if (hasNonIncrementalFunctions) {

					if (!elements.isEmpty()) {
						sampleOfGroup = elements.iterator().next();

						for (final INonIncrementalAggregationFunction<M, T> function : nonIncrementalFunctions) {
							Object[] result2 = null;
							try {
								result2 = function.evaluate(elements, null, null);
							} catch (final NullPointerException ex) {
								// this method does not evaluate with no trigger
								// and no point in time
							}

							if (result2 == null) {
								result2 = new Object[function.getOutputAttributeIndices().length];
								Arrays.fill(result2, null);
							}

							for (int i = 0; i < result2.length; ++i) {
								result.setAttribute(function.getOutputAttributeIndices()[i], result2[i]);
							}
						}
					}
				}

				if (hasIncrementalFunctions) {
					for (final IIncrementalAggregationFunction<M, T> function : getStatefulFunctions(key)) {
						Object[] result2 = null;
						try {
							result2 = function.evalute(null, null);
						} catch (final NullPointerException ex) {
							// this method does not evaluate with no trigger
							// and no point in time
						}
						if (result2 == null) {
							result2 = new Object[function.getOutputAttributeIndices().length];
							Arrays.fill(result2, null);
						}
						for (int i = 0; i < result2.length; ++i) {
							result.setAttribute(function.getOutputAttributeIndices()[i], result2[i]);
						}
					}
				}

				transferResult(result, sampleOfGroup, new PointInTime(System.currentTimeMillis()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#
	 * process_isSemanticallyEqual(de.uniol.inf.is.odysseus.core.
	 * physicaloperator.IPhysicalOperator)
	 */
	@Override
	public boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
		// TODO Auto-generated method stub
		return super.process_isSemanticallyEqual(ipo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * isSemanticallyEqual(de.uniol.inf.is.odysseus.core.physicaloperator.
	 * IPhysicalOperator)
	 */
	@Override
	public boolean isSemanticallyEqual(final IPhysicalOperator ipo) {
		// TODO Auto-generated method stub
		return super.isSemanticallyEqual(ipo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO#getState()
	 */
	@Override
	public IOperatorState getState() {
		return new AggregationState<M, T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO#setState(java.
	 * io.Serializable)
	 */
	@Override
	public void setState(final Serializable state) {
		if (state instanceof AggregationState) {
			// TODO: synchronized?
			@SuppressWarnings("unchecked")
			final AggregationState<M, T> aggregationState = (AggregationState<M, T>) state;
			this.outdatingGroups = aggregationState.getOutdatingGroups();
			this.groups = aggregationState.getGroups();
			this.hasOutdatingElements = aggregationState.isHasOutdatingElements();
			this.incrementalFunctionsForGroup = aggregationState.getIncrementalFunctionsForGroup();
			this.watermark = aggregationState.getWatermark();
			this.watermarkOut = aggregationState.getWatermarkOut();
			this.defaultGroupingKey = aggregationState.getDefaultGroupingKey();

		} else {
			throw new IllegalArgumentException("wrong type of state");
		}

	}
}
