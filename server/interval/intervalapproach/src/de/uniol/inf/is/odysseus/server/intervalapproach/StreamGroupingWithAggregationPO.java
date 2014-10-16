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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;

/**
 * 
 * @author Marco Grawunder
 *
 * @param <Q>
 * @param <R>
 * @param <W>
 *
 * Use AggregateTIPO instead
 */
@Deprecated
public class StreamGroupingWithAggregationPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregateTIPO<Q, R, W> {

	static final Logger logger = LoggerFactory
			.getLogger(StreamGroupingWithAggregationPO.class);


	public StreamGroupingWithAggregationPO(SDFSchema inputSchema,
			SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fastGrouping) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations,
				fastGrouping);
	}


}
