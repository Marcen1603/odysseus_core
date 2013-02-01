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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.IInlineMetadataMergeFunction;

/**
 * @author Dennis Geesen
 * 
 */
public class EnrichPO<T extends IStreamObject<M>, M extends IMetaAttribute> extends AbstractPipe<T, T> implements IHasPredicate, IHasMetadataMergeFunction<M> {

	private IPredicate<T> predicate;
	// TODO: check, if it can be merged with caching strategies form DB-Enrich
	private List<T> cache = new ArrayList<>();
	private List<T> buffer = new ArrayList<>();
	private CombinedMergeFunction<M> metaMergeFunction = new CombinedMergeFunction<>();
	private IDataMergeFunction<T, M> dataMergeFunction;
	private ITransferArea<T, T> transferFunction;

	private int minSize = 0;

	public EnrichPO(IPredicate<T> predicate) {
		this.predicate = predicate;
	}

	public EnrichPO(int minimalSize) {
		this.minSize = minimalSize;
	}

	@SuppressWarnings("unchecked")
	public EnrichPO(IPredicate<?> predicate, int minimumSize) {
		this.predicate = (IPredicate<T>) predicate;
		this.minSize = minimumSize;
	}

	/**
	 * @param enrichPO
	 */
	public EnrichPO(EnrichPO<T, M> po) {
		super(po);
		this.minSize = po.minSize;
		this.predicate = po.predicate.clone();
		this.dataMergeFunction = po.dataMergeFunction.clone();
		this.dataMergeFunction.init();

		this.metaMergeFunction = po.metaMergeFunction.clone();
		this.metaMergeFunction.init();
		this.transferFunction = po.transferFunction.clone();
		this.transferFunction.init(this);
	}

	@Override
	public IMetadataMergeFunction<M> getMetadataMerge() {
		return this.metaMergeFunction;
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
		this.transferFunction.init(this);
		this.buffer.clear();
		this.cache.clear();
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		// if port == 0, it is a cached-object
		if (port == 0) {
			this.cache.add(object);
			// check, whether there are enough items in cache to write out the
			// buffer
			if (this.cache.size() >= minSize) {
				for (T buffered : this.buffer) {
					processEnrich(buffered);
				}
				this.buffer.clear();
			}
		} else {
			// stream time is also cached time here 
			// TODO: check intervals?!
			this.transferFunction.newElement(object, 0);
			this.transferFunction.newElement(object, port);			
			// if we do not have enough items in cache, we put the objects into
			// a buffer
			if (this.cache.size() < minSize) {
				buffer.add(object);
			} else {
				// if we have enough, we can enrich the object without waiting
				processEnrich(object);
			}
		}

	}

	private void processEnrich(T object) {
		for (T cached : this.cache) {
			if (this.predicate.evaluate(cached, object)) {
				T enriched = this.dataMergeFunction.merge(cached, object, metaMergeFunction, Order.LeftRight);
				this.transferFunction.transfer(enriched);
			}
		}
	}

	@Override
	protected void process_close() {
		super.process_close();
		this.transferFunction.done();

		this.buffer.clear();
		this.cache.clear();
	}

	@Override
	public EnrichPO<T, M> clone() {
		return new EnrichPO<T, M>(this);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.transferFunction.newHeartbeat(punctuation.getTime(), port);
		this.transferFunction.sendPunctuation(punctuation);

	}

	public IDataMergeFunction<T, M> getDataMergeFunction() {
		return dataMergeFunction;
	}

	public void setDataMergeFunction(IDataMergeFunction<T, M> dmf) {
		this.dataMergeFunction = dmf;
	}

	public void addInlineMergeFunction(IInlineMetadataMergeFunction<M> inlineFunction) {
		this.metaMergeFunction.add(inlineFunction);
	}

	/**
	 * @param tiTransferArea
	 */
	public void setTransferFunction(ITransferArea<T, T> tiTransferArea) {
		this.transferFunction = tiTransferArea;
	}

}
