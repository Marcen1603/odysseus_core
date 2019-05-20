/**********************************************************************************
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.metadata.UseRightInputMetadata;

/**
 * @author Dennis Geesen
 * @author Marco Grawunder
 *
 */
public class EnrichPO<T extends IStreamObject<M>, M extends IMetaAttribute>
		extends AbstractPipe<T, T> implements IHasPredicate {

	private IPredicate<T> predicate;
	// TODO: check, if it can be merged with caching strategies form DB-Enrich
	private List<T> cache = new CopyOnWriteArrayList<>();
	private List<T> buffer = new CopyOnWriteArrayList<>();
	private UseRightInputMetadata<M> metaMergeFunction = new UseRightInputMetadata<>();
	private IDataMergeFunction<T, M> dataMergeFunction;

	private int minSize = 0;
	private T emptyObject;
	private final boolean outer;

	@SuppressWarnings("unchecked")
	public EnrichPO(IPredicate<?> predicate, int minimumSize, boolean outer) {
		this.predicate = (IPredicate<T>) predicate;
		this.minSize = minimumSize;
		this.outer = outer;
	}


	@Override
	public IPredicate<?> getPredicate() {
		return this.predicate;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.metaMergeFunction.init();
		this.dataMergeFunction.init();
		this.buffer.clear();
		this.cache.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		// if port == 0, it is a cached-object
		if (port == 0) {
			this.cache.add(object);
			if (emptyObject == null) {
				this.emptyObject = (T) object.copyAndReturnEmptyInstance();
			}
			// check, whether there are enough items in cache to write out the
			// buffer
			if (this.cache.size() >= minSize) {
				synchronized (this.buffer) {
					for (T buffered : this.buffer) {
						processEnrich(buffered);
					}
					this.buffer.clear();
				}
			}
		} else {
			// if we do not have enough items in cache, we put the objects into
			// a buffer
			if (this.cache.size() < minSize) {
				synchronized (this.buffer) {
					buffer.add(object);
				}
			} else {
				// if we have enough, we can enrich the object without waiting
				processEnrich(object);
			}
		}

	}

	private void processEnrich(T object) {
			boolean foundMatch = false;
			for (T cached : this.cache) {
				if (this.predicate.evaluate(cached, object)) {
					T enriched = this.dataMergeFunction.merge(cached, object,
							metaMergeFunction, Order.LeftRight);
					transfer(enriched);
					foundMatch=true;
				}
			}
			if (!foundMatch && outer) {
				T obj = this.dataMergeFunction.merge(emptyObject, object, metaMergeFunction, Order.LeftRight);
				transfer(obj);
			}
	}

	@Override
	protected void process_close() {
		super.process_close();
		this.buffer.clear();
		this.cache.clear();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		predicate.processPunctuation(punctuation);
		sendPunctuation(punctuation);
	}

	public IDataMergeFunction<T, M> getDataMergeFunction() {
		return dataMergeFunction;
	}

	public void setDataMergeFunction(IDataMergeFunction<T, M> dmf) {
		this.dataMergeFunction = dmf;
	}

	@Override
	public long getElementsStored1() {
		return cache.size();
	}

	@Override
	public long getElementsStored2() {
		return buffer.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPredicate(IPredicate<?> predicate) {
		this.predicate = (IPredicate<T>)predicate;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof EnrichPO)) {
			return false;
		}
		EnrichPO other = (EnrichPO) ipo;
		return Objects.equals(this.predicate, other.predicate) &&
				this.minSize == other.minSize &&
				this.outer == other.outer;
		
	}
	
}
