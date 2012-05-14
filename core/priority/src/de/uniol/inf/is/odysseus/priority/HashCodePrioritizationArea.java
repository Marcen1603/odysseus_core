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
package de.uniol.inf.is.odysseus.priority;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class HashCodePrioritizationArea<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		implements IPostPrioritizationArea<T> {

	private IHashFunction<T> hashFunction;

	private HashSet<HashCodeWrapper<T>> elements = new HashSet<HashCodeWrapper<T>>();
	private PriorityQueue<HashCodeWrapper<T>> orderByTimestamp = new PriorityQueue<HashCodeWrapper<T>>(
			50, new Comparator<HashCodeWrapper<T>>() {
				@Override
				public int compare(HashCodeWrapper<T> o1, HashCodeWrapper<T> o2) {
					return o1.getElement().getMetadata().getEnd().compareTo(
							o2.getElement().getMetadata().getEnd());

				}
			});
	private ReentrantLock lock = new ReentrantLock();

	public HashCodePrioritizationArea() {
	}

	public void setHashFunction(IHashFunction<T> hashFunction) {
		this.hashFunction = hashFunction;
	}

	@Override
	public boolean hasPartner(T element) {
		lock.lock();
		HashCodeWrapper<T> hashedElement = createHashElement(element);
		boolean value = elements.contains(hashedElement);
		lock.unlock();
		return value;
	}

	@Override
	public void insert(T element) {
		lock.lock();
		HashCodeWrapper<T> hashElement = createHashElement(element);
		orderByTimestamp.add(hashElement);
		elements.add(hashElement);
		lock.unlock();
	}

	@Override
	public void purgeElements(T element) {
		lock.lock();
		while (!orderByTimestamp.isEmpty()
				&& TimeInterval.totallyBefore(orderByTimestamp.peek()
						.getElement().getMetadata(), element.getMetadata())) {
			elements.remove(element);
		}
		lock.unlock();
	}

	private HashCodeWrapper<T> createHashElement(T element) {
		return new HashCodeWrapper<T>(element, hashFunction.hashCode(element));
	}

}
