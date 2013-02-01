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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;

/**
 * @author Dennis Geesen
 * 
 */
public class EnrichPO extends AbstractPipe<IStreamObject<IMetaAttribute>, IStreamObject<IMetaAttribute>> implements IHasPredicate, IHasMetadataMergeFunction<IMetaAttribute> {

	private IPredicate<IStreamObject<IMetaAttribute>> predicate;
	private List<IStreamObject<IMetaAttribute>> cache = new ArrayList<>();
	private IMetadataMergeFunction<IMetaAttribute> metaMergeFunction;
	private IDataMergeFunction<IStreamObject<IMetaAttribute>, IMetaAttribute> dataMergeFunction;
	private ITransferArea<IStreamObject<IMetaAttribute>, IStreamObject<IMetaAttribute>> transferFunction;
	
	private int minSize = 0;
	

	/**
	 * @param enrichPO
	 */
	public EnrichPO(EnrichPO po) {
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
	public IMetadataMergeFunction<IMetaAttribute> getMetadataMerge() {
		return this.metaMergeFunction;
	}

	public void setMetadataMergeFunction(IMetadataMergeFunction<IMetaAttribute> metamergeFunction) {
		this.metaMergeFunction = metamergeFunction;
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
	}

	@Override
	protected synchronized void process_next(IStreamObject<IMetaAttribute> object, int port) {
		if (port == 0) {
			this.cache.add(object);
		} else {
			this.transferFunction.newElement(object, port);
			for (IStreamObject<IMetaAttribute> cached : this.cache) {
				if (this.predicate.evaluate(cached, object)) {
					IStreamObject<IMetaAttribute> enriched = this.dataMergeFunction.merge(cached, object, metaMergeFunction, Order.LeftRight);
					this.transferFunction.transfer(enriched);					
				}
			}
		}

	}

	@Override
	public EnrichPO clone() {
		return new EnrichPO(this);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.transferFunction.newHeartbeat(punctuation.getTime(), port);
		super.processPunctuation(punctuation, port);
		
	}

	public IDataMergeFunction<IStreamObject<IMetaAttribute>, IMetaAttribute> getDataMergeFunction() {
		return dataMergeFunction;
	}

	public void setDataMergeFunction(IDataMergeFunction<IStreamObject<IMetaAttribute>, IMetaAttribute> dataMergeFunction) {
		this.dataMergeFunction = dataMergeFunction;
	}

}
