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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class AvgSum<R, W> extends AbstractAggregateFunction<R, W>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2341630908327295104L;
	boolean isAvg;
	
	protected AvgSum(boolean isAvg, boolean partialAggregateInput) {
		super (isAvg?"AVG":"SUM", partialAggregateInput);
		this.isAvg = isAvg;
	}
	
	@Override
	public IPartialAggregate<R> init(IPartialAggregate<R> in) {
		return new AvgSumPartialAggregate<R>(
				(AvgSumPartialAggregate<R>) in);
	}
	
	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p,
			R toMerge, boolean createNew) {
		return process_merge(createNew?p.clone():p, toMerge);
	}

	abstract protected IPartialAggregate<R> process_merge(
			IPartialAggregate<R> iPartialAggregate, R toMerge);
	
	public boolean isAvg(){
		return isAvg;
	}		
}
