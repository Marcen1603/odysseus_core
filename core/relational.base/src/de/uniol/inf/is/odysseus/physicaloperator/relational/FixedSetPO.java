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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.concurrent.atomic.AtomicBoolean;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;


public final class FixedSetPO<T extends IMetaAttributeContainer<? extends IClone>> extends
		AbstractIterableSource<T>{

	private final T[] tuples;
	private int index;
	private AtomicBoolean isDone;

	@SafeVarargs
	public FixedSetPO(T... tuples) {
		this.tuples = tuples;
		this.isDone = new AtomicBoolean(false);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		index = 0;
	}

	@Override
	public synchronized boolean hasNext() {
		return index < tuples.length;
	}

	@Override
	public synchronized void transferNext() {
		transfer(tuples[index++]);
		if (index == tuples.length) {
			this.isDone.set(true);
			propagateDone();
		}
	}

	@Override
	public boolean isDone() {
		return this.isDone.get();
	}

	@Override
	public FixedSetPO<T> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

}
