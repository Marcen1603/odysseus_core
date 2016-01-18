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
package de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate;

import java.util.ArrayList;
import java.util.Arrays;
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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.SimpleAggregateFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.SimpleStatefulAggregateFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.SimpleStatelessAggregateFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.impl.Count;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.impl.Nest;
import de.uniol.inf.is.odysseus.server.intervalapproach.simpleaggregate.functions.impl.Sum;

/**
 * @author Cornelius Ludmann
 *
 */
public class RelationalSimpleAggregatePO<M extends ITimeInterval, T extends Tuple<M>> extends AbstractPipe<T, T>
		implements IPhysicalOperatorKeyValueProvider {

	protected static Logger LOG = LoggerFactory.getLogger(RelationalSimpleAggregatePO.class);

	private List<SimpleStatelessAggregateFunction<M, T>> aggrFunctions = new ArrayList<>();
	private List<SimpleStatefulAggregateFunction<M, T>> statefulAggrFunctions = new ArrayList<>();
	private Map<Object, List<SimpleStatefulAggregateFunction<M, T>>> statefulAggrFunctionsMap = new HashMap<>();
	private TreeMap<Long, Set<Object>> outdatingGroups = new TreeMap<>();
	private int[] groupingAttributesIdx;
	private Map<Object, SimpleSweepArea<M, T>> groups = new HashMap<>();
	private boolean hasStatelessFunctions = false;
	private boolean hasStatefulFunctions = false;
	private boolean hasOutdatingElements = false;
	private SDFSchema outputSchema;
	private Long watermark = 0l;

	/**
	 * TODO: Find a better name for this attribute. ;-)
	 */
//	final private boolean onlyOutputNewValueWithNewElement = false;

	/**
	 * 
	 */
	public RelationalSimpleAggregatePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes, Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			IMetadataMergeFunction<M> metadataMerge) {
		this.groupingAttributesIdx = createGroupingAttrIdx(inputSchema, groupingAttributes);
		for (Entry<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggr : aggregations.entrySet()) {
			int[] restr = createRestictionAttrIdx(inputSchema, aggr.getKey());
			if (restr.length == inputSchema.size() && isOrdered(restr)) {
				restr = null;
			}
			for (Entry<AggregateFunction, SDFAttribute> aggr2 : aggr.getValue().entrySet()) {
				String name = aggr2.getKey().getName();
				SimpleAggregateFunction<M, T> function = null;
				if ("nest".equalsIgnoreCase(name)) {
					if (restr == null) {
						function = new Nest<>();
					} else {
						function = new Nest<>(restr);
					}
				} else if ("sum".equalsIgnoreCase(name)) {
					if (restr == null) {
						int[] a = new int[1];
						a[0] = 0;
						function = new Sum<>(a);
					} else {
						function = new Sum<>(restr);
					}
				} else if ("count".equalsIgnoreCase(name)) {
					function = new Count<>();
				}

				function.setOutputAttribute(outputSchema.indexOf(aggr2.getValue()));

				if (function != null) {
					if (function instanceof SimpleStatefulAggregateFunction) {
						statefulAggrFunctions.add((SimpleStatefulAggregateFunction<M, T>) function);
						this.hasStatefulFunctions = true;
					} else if (function instanceof SimpleStatelessAggregateFunction) {
						aggrFunctions.add((SimpleStatelessAggregateFunction<M, T>) function);
						this.hasStatelessFunctions = true;
					}
				}
			}
		}

		this.outputSchema = outputSchema;
	}

	/**
	 * @param restr
	 * @return
	 */
	private static boolean isOrdered(int[] restr) {
		int[] copy = Arrays.copyOf(restr, restr.length);
		Arrays.sort(copy);
		for (int i = 0; i < restr.length; ++i) {
			if (restr[i] != copy[i])
				return false;
		}
		return true;
	}

	private static int[] createRestictionAttrIdx(SDFSchema inputSchema, SDFSchema restrictedSchema) {
		int[] result = new int[restrictedSchema.size()];
		for (int i = 0; i < restrictedSchema.size(); ++i) {
			result[i] = inputSchema.indexOf(restrictedSchema.get(i));
		}
		return result;
	}

	/**
	 * @param inputSchema
	 * @param groupingAttributes
	 * @return
	 */
	private static int[] createGroupingAttrIdx(SDFSchema inputSchema, List<SDFAttribute> grAttribs) {
		int[] gRestrict = new int[grAttribs.size()];
		for (int i = 0; i < grAttribs.size(); i++) {
			gRestrict[i] = inputSchema.indexOf(grAttribs.get(i));
		}
		return gRestrict;
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
		groups.clear();
		statefulAggrFunctionsMap.clear();
		outdatingGroups.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.physicaloperator.ISink#processPunctuation(
	 * de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)
	 */
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.
	 * IPhysicalOperatorKeyValueProvider#getKeyValues()
	 */
	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> result = new HashMap<>();
		result.put("Groups", "" + groups.size());
		result.put("Groups Stateful Functions Map", "" + statefulAggrFunctionsMap.size());
		result.put("Has Stateful Functions", "" + hasStatefulFunctions);
		result.put("No of Stateful Functions", "" + statefulAggrFunctions.size());
		result.put("Has Stateless Functions", "" + hasStatelessFunctions);
		result.put("No of Stateless Functions", "" + aggrFunctions.size());
		result.put("Has Outdating Elements", "" + hasOutdatingElements);
		result.put("No of Outdating Points:", "" + outdatingGroups.size());
		result.put("Watermark:", "" + watermark);
		result.put("Watermark Out:", "" + watermarkOut);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * getOutputMode()
	 */
	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#
	 * process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@Override
	protected void process_next(T object, int port) {

		if (!hasOutdatingElements) {
			if (!object.getMetadata().getEnd().isInfinite())
				hasOutdatingElements = true;
		}

		Object groupKey = getGroupKey(object, groupingAttributesIdx);

		synchronized (groups) {

			if (this.watermark > object.getMetadata().getStart().getMainPoint()) {
				LOG.error("Element " + object + " is out of order. Watermark " + watermark + " > "
						+ object.getMetadata().getStart().getMainPoint());
			} else {
				this.watermark = object.getMetadata().getStart().getMainPoint();
			}

			if (!hasStatelessFunctions && !hasOutdatingElements) {
				// process all stateful functions with new tuple

				@SuppressWarnings("unchecked")
				T result = (T) new Tuple<>(outputSchema.size(), false);

				List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey = getStatefulFunctions(groupKey);

				processStatefulFunctionsAdd(result, statefulFunctionsForKey, object);
				transferResult(result, object, object.getMetadata().getStart());
			} else if (hasStatelessFunctions && !hasOutdatingElements) {

				@SuppressWarnings("unchecked")
				T result = (T) new Tuple<>(outputSchema.size(), false);

				SimpleSweepArea<M, T> sa = getSweepArea(groupKey);
				sa.addElement(object);

				if (hasStatefulFunctions) {
					List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey = getStatefulFunctions(
							groupKey);
					processStatefulFunctionsAdd(result, statefulFunctionsForKey, object);
				}

				List<T> objects = sa.getValidTuples();
				processStatelessFunctions(result, objects);
				transferResult(result, object, object.getMetadata().getStart());
			} else {

				processOutdated(object, groupKey);

				Long time = null;

				// process outdating
				SimpleSweepArea<M, T> sa = getSweepArea(groupKey);
				if (hasStatelessFunctions) {
					time = sa.addElement(object);
				} else {
					time = sa.addToOutdatingTuples(object);
				}

				if (time != null) {
					addToOutdatingGroups(time, groupKey);
				}

				// process new
				@SuppressWarnings("unchecked")
				T result = (T) new Tuple<>(outputSchema.size(), false);

				if (hasStatefulFunctions) {
					List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey = getStatefulFunctions(
							groupKey);
					processStatefulFunctionsAdd(result, statefulFunctionsForKey, object);
				}

				List<T> objects = sa.getValidTuples();
				processStatelessFunctions(result, objects);
				transferResult(result, object, object.getMetadata().getStart());
			}
		}
	}

	/**
	 * @param object
	 * @param groupKey
	 */
	private void processOutdated(T object, Object groupKey) {
		Iterator<Entry<Long, Set<Object>>> iter = outdatingGroups
				.headMap(object.getMetadata().getStart().getMainPoint(), true).entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Long, Set<Object>> entry = iter.next();

			Long pointInTime = entry.getKey();

			for (Object gkey : entry.getValue()) {
				boolean triggerGroup;
				if (groupKey == null) {
					triggerGroup = gkey == null;
				} else {
					triggerGroup = groupKey.equals(gkey);
				}

				processOutdated(getSweepArea(gkey), object, gkey, triggerGroup, pointInTime);
			}
			iter.remove();
		}
	}

	/**
	 * @param time
	 * @param groupKey
	 */
	private void addToOutdatingGroups(Long time, Object groupKey) {
		if (time != null) {
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
	private void processStatelessFunctions(T result, List<T> objects) {
		for (SimpleStatelessAggregateFunction<M, T> function : aggrFunctions) {
			Object result2 = function.evaluate(Collections.unmodifiableList(objects));
			result.setAttribute(function.getOutputAttribute(), result2);
		}
	}

	/**
	 * @param groupKey
	 * @return
	 */
	private SimpleSweepArea<M, T> getSweepArea(Object groupKey) {
		SimpleSweepArea<M, T> sa = groups.get(groupKey);
		if (sa == null) {
			sa = new SimpleSweepArea<>();
			groups.put(groupKey, sa);
		}
		return sa;
	}

	private long watermarkOut = 0l;
	
	/**
	 * @param result
	 */
	private void transferResult(T result, T trigger, PointInTime startTs, T sampleOfGroup) {
		// TODO: TransferArea!
		
		if(watermarkOut > startTs.getMainPoint()) {
			LOG.warn("Out element " + result + " triggerd by " + trigger + " for start ts " + startTs + " is out of order!");
		} else {
			watermarkOut = startTs.getMainPoint();
		}

		setGroupingAttributes(sampleOfGroup, result);

		@SuppressWarnings("unchecked")
		M meta = (M) trigger.getMetadata().clone();
		meta.setStart(startTs);
		meta.setEnd(PointInTime.INFINITY);
		result.setMetadata(meta);
		transfer(result);
	}

	/**
	 * @param result
	 */
	private void transferResult(T result, T trigger, PointInTime startTs) {
		transferResult(result, trigger, startTs, trigger);
	}

	/**
	 * @param result
	 * @param statefulFunctionsForKey
	 * @param object
	 */
	private void processStatefulFunctionsAdd(T result,
			List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey, T object) {
		for (SimpleStatefulAggregateFunction<M, T> function : statefulFunctionsForKey) {
			Object result2 = function.evaluate(object, null);
			result.setAttribute(function.getOutputAttribute(), result2);
		}
	}

	private void processStatefulFunctionsRemove(T result,
			List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey, List<T> objects) {
		for (SimpleStatefulAggregateFunction<M, T> function : statefulFunctionsForKey) {
			Object result2 = function.evaluate(null, Collections.unmodifiableList(objects));
			if (result != null) {
				result.setAttribute(function.getOutputAttribute(), result2);
			}
		}
	}

	/**
	 * @param groupKey
	 * @return
	 */
	private List<SimpleStatefulAggregateFunction<M, T>> getStatefulFunctions(Object groupKey) {
		List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey = statefulAggrFunctionsMap.get(groupKey);
		if (statefulFunctionsForKey == null) {
			statefulFunctionsForKey = new ArrayList<>();
			for (SimpleStatefulAggregateFunction<M, T> f : statefulAggrFunctions) {
				statefulFunctionsForKey.add(f.clone());
			}
			statefulAggrFunctionsMap.put(groupKey, statefulFunctionsForKey);
		}
		return statefulFunctionsForKey;
	}

	/**
	 * @param sa
	 * @param b
	 * @param pointInTime
	 */
	private void processOutdated(SimpleSweepArea<M, T> sa, T trigger, Object groupKey, boolean isTriggerGroup,
			Long pointInTime) {

		List<T> outdatedTuples = sa.getOutdatedTuples(pointInTime);
		if (isTriggerGroup && pointInTime == trigger.getMetadata().getStart().getMainPoint()) {
			if (hasStatefulFunctions) {
				List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey = getStatefulFunctions(groupKey);
				processStatefulFunctionsRemove(null, statefulFunctionsForKey, outdatedTuples);
			}
			removeOutdating(sa, outdatedTuples, pointInTime);
		} else {

			@SuppressWarnings("unchecked")
			T result = (T) new Tuple<>(outputSchema.size(), false);

			T sampleOfGroup = null;
			if (isTriggerGroup)
				sampleOfGroup = trigger;

			if (hasStatefulFunctions) {
				List<SimpleStatefulAggregateFunction<M, T>> statefulFunctionsForKey = getStatefulFunctions(groupKey);
				processStatefulFunctionsRemove(result, statefulFunctionsForKey, outdatedTuples);
			}

			if (sampleOfGroup == null && outdatedTuples != null && !outdatedTuples.isEmpty()) {
				sampleOfGroup = outdatedTuples.get(0);
			}

			removeOutdating(sa, outdatedTuples, pointInTime);

			if (hasStatelessFunctions) {
				List<T> objects = sa.getValidTuples();
				if (sampleOfGroup == null && objects != null && !objects.isEmpty()) {
					sampleOfGroup = objects.get(0);
				}
				processStatelessFunctions(result, objects);
			}

			transferResult(result, trigger, new PointInTime(pointInTime), sampleOfGroup);
		}

		if (!isTriggerGroup)
			removeGroupIfEmpty(sa, groupKey);
	}

	/**
	 * @param groupKey
	 * @param groupKey2
	 */
	private void removeGroupIfEmpty(SimpleSweepArea<M, T> sa, Object groupKey) {
		if (!sa.hasValidTuples()) {
			groups.remove(groupKey);
			statefulAggrFunctionsMap.remove(groupKey);
		}
	}

	/**
	 * @param sa
	 * @param entry
	 */
	private void removeOutdating(SimpleSweepArea<M, T> sa, List<T> tuples, Long pointInTime) {
		sa.removeFromValidTuples(tuples);
		sa.removeFromOutdatingTuples(pointInTime);
	}

	/**
	 * @param result
	 */
	private void setGroupingAttributes(T sampleOfGroup, T result) {
		if (sampleOfGroup != null) {
			int i = 0;
			for (int x : groupingAttributesIdx) {
				result.setAttribute(i, sampleOfGroup.getAttribute(x));
				++i;
			}
		}
	}

	private static final Object defaultGroupingKey = new Object();

	/**
	 * @param object
	 * @param groupingAttributesIdx2
	 * @return
	 */
	private static <T extends Tuple<?>> Object getGroupKey(T object, int[] groupingAttributesIdx2) {
		if (groupingAttributesIdx2.length == 0) {
			return defaultGroupingKey;
		}
		if (groupingAttributesIdx2.length == 1) {
			return object.getAttribute(groupingAttributesIdx2[0]);
		}
		return Arrays.asList(object.restrict(groupingAttributesIdx2, true).getAttributes());
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
}
