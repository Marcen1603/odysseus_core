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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.sweeparea.IAggregationSweepArea;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;

class AggregationState<M extends ITimeInterval, T extends Tuple<M>> implements IOperatorState, Serializable {

	private static final long serialVersionUID = -3151681597160956002L;

	protected final TreeMap<PointInTime, Set<Object>> outdatingGroups;
	protected final Set<Object> groupKeys;
	protected final Map<Object, IAggregationSweepArea<M, T>> groups;
	protected boolean hasOutdatingElements;
	protected final Map<Object, List<IIncrementalAggregationFunction<M, T>>> incrementalFunctionsForGroup;
	private final long watermark;
	private final long watermarkOut;
	private final Serializable defaultGroupingKey;

	public AggregationState(final AggregationPO<M, T> aggregationPo) {
		this.defaultGroupingKey = aggregationPo.defaultGroupingKey;
		this.outdatingGroups = aggregationPo.outdatingGroups;
		this.groupKeys = aggregationPo.groupKeys;
		this.groups = aggregationPo.sweepAreas;
		this.hasOutdatingElements = aggregationPo.hasOutdatingElements;
		this.incrementalFunctionsForGroup = aggregationPo.incrementalFunctionsForGroup;
		this.watermark = aggregationPo.watermark;
		this.watermarkOut = aggregationPo.watermarkOut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState#
	 * getSerializedState()
	 */
	@Override
	public Serializable getSerializedState() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState#
	 * estimateSizeInBytes()
	 */
	@Override
	public long estimateSizeInBytes() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return the outdatingGroups
	 */
	public TreeMap<PointInTime, Set<Object>> getOutdatingGroups() {
		return outdatingGroups;
	}

	/**
	 * @return the groups
	 */
	public Map<Object, IAggregationSweepArea<M, T>> getGroups() {
		return groups;
	}

	/**
	 * @return the hasOutdatingElements
	 */
	public boolean isHasOutdatingElements() {
		return hasOutdatingElements;
	}

	/**
	 * @return the incrementalFunctionsForGroup
	 */
	public Map<Object, List<IIncrementalAggregationFunction<M, T>>> getIncrementalFunctionsForGroup() {
		return incrementalFunctionsForGroup;
	}

	/**
	 * @return the watermark
	 */
	public long getWatermark() {
		return watermark;
	}

	/**
	 * @return the watermarkOut
	 */
	public long getWatermarkOut() {
		return watermarkOut;
	}

	public Serializable getDefaultGroupingKey() {
		return defaultGroupingKey;
	}

	public Set<Object> getGroupKeys() {
		return groupKeys;
	}
}