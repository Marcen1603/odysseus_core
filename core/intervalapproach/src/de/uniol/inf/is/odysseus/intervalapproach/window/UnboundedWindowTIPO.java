/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;

public class UnboundedWindowTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractNonBlockingWindowTIPO<T> {

	public UnboundedWindowTIPO(WindowAO algebraOp) {
		super(algebraOp);
	}

	public UnboundedWindowTIPO(UnboundedWindowTIPO<T> unboundedWindowTIPO) {
		super(unboundedWindowTIPO);
	}

	@Override
	protected PointInTime calcWindowEnd(ITimeInterval interval) {
		return PointInTime.getInfinityTime();
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new UnboundedWindowTIPO<T>(this);
	}

}
