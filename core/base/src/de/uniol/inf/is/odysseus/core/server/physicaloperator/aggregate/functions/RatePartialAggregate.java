/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class RatePartialAggregate<T> implements IPartialAggregate<T> {
	int count = 0;
	final PointInTime start;
	PointInTime end;

	public RatePartialAggregate(ITimeInterval meta) {
		this.start = meta.getStart();
		this.end = meta.getEnd();
		this.count = 1;
	}

	public RatePartialAggregate(RatePartialAggregate<T> ratePartialAggregate) {
		this.count = ratePartialAggregate.count;
		this.start = ratePartialAggregate.start;
		this.end = ratePartialAggregate.end;
	}

	public int getCount() {
		return count;
	}

	public PointInTime getStart() {
		return start;
	}

	public PointInTime getEnd() {
		return end;
	}

	public void add() {
		this.count++;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("RatePartialAggregate (")
				.append(hashCode()).append(")").append(count).append("/")
				.append(end.getMainPoint() - start.getMainPoint());
		return ret.toString();
	}

	@Override
	public RatePartialAggregate<T> clone() {
		return new RatePartialAggregate<T>(this);
	}
}
