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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * This interface is used to represent different grouping processors. Its main function is 
 * a) to create an ID for in input element
 * b) to create output elements
 * 
 * 
 * @author Marco Grawunder
 *
 * @param <R> Elements that should be group
 * @param <W> Outputs that should be created
 */
public interface IGroupProcessor<R, W extends IClone> extends IClone{

	/**
	 * Group processors must be inialized (typically in open phase)
	 */
	void init();
	
	/**
	 * Determine a group object for the given input
	 * @param elem
	 * @return
	 */
	Object getGroupID(R elem);

	/**
	 * Determine an numeric group id for the given input
	 * @param elem
	 * @return
	 */
	Long getAscendingGroupID(R elem);

	
	/**
	 * Allow to set for a spefic elemtent the group id
	 * should typically only be used in distributed cases
	 * @param id
	 * @param elem
	 */
	void setGroup(Object id, R elem);
	
	/**
	 * Especially for aggregation, this method creates an output element (with real values)
	 * @param groupID Output for which group ID
	 * @param r the aggregations for this group
	 * @return
	 */
	W createOutputElement(Object groupID,
			PairMap<SDFSchema, AggregateFunction, W, ?> r);

	/**
	 * Especially for aggregation, this method creates an output element (with partial aggregates)
	 * @param groupID Output for which group ID
	 * @param r the aggregations for this group
	 * @return
	 */

	W createOutputElement2(Object groupID,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, ?> e);
	

	/**
	 * Retrieve from the input the part that is used to determine the group. 
	 * Typically, for each group, this value is unique
	 * @param elem
	 * @return
	 */
	R getGroupingPart(R elem);

	/**
	 * A string representation of the group part 
	 * @param elem
	 * @return
	 */
	String toGroupString(R elem);
	
	/**
	 * 
	 */
	@Override
	IGroupProcessor<R, W> clone();
	

}
