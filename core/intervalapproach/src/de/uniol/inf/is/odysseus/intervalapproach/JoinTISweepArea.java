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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.FastArrayList;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.IFastList;

public class JoinTISweepArea<T extends IStreamObject<? extends ITimeInterval>>
		extends DefaultTISweepArea<T> {

	public JoinTISweepArea() {
		super(new FastArrayList<T>());
	}
	
	public JoinTISweepArea(IFastList<T> list){
		super(list);
	}
	
	@Override
	public void insert(T s) {
		synchronized (this.getElements()) {
			setLatestTimeStamp(s);
			this.getElements().add(s);
		}
	}
	
	@Override
	public Iterator<T> queryCopy(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		synchronized(this.getElements()){
			switch (order) {
			case LeftRight:
				for (T next : this.getElements()) {
					if (TimeInterval.totallyBefore(next.getMetadata(), element
							.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element
							.getMetadata())) {
						break;
					}
					if (getQueryPredicate().evaluate(element, next)) {
						result.add(next);
					}

				}
				break;
			case RightLeft:
				for (T next : this.getElements()) {
					if (TimeInterval.totallyBefore(next.getMetadata(), element
							.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element
							.getMetadata())) {
						break;
					}
					if (getQueryPredicate().evaluate(next, element)) {
						result.add(next);
					}
				}
				break;
			}
		}
		return result.iterator();
	}
}
