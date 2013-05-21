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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;

public class UnionPO<R extends IStreamObject<?>> extends AbstractPipe<R, R> implements IStatefulOperator{

	protected ITransferArea<R,R> transferFunction;
	
	public UnionPO(ITransferArea<R,R> transferFunction) {
		this.transferFunction = transferFunction;
		transferFunction.init(this);
	}

	public UnionPO(UnionPO<R> unionPO){
		this.transferFunction = unionPO.transferFunction.clone();
	}

	@Override
	public UnionPO<R> clone(){
		return new UnionPO<R>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected void newSourceSubscribed(
			PhysicalSubscription<ISource<? extends R>> sub) {
		transferFunction.addNewInput(sub);
	}
	
	@Override
	protected void sourceUnsubscribed(
			PhysicalSubscription<ISource<? extends R>> sub) {
		transferFunction.removeInput(sub);
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		transferFunction.init(this);
	}
	
	@Override
	protected synchronized void process_next(R object, int port) {
		transferFunction.transfer(object);
		transferFunction.newElement(object, port);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transferFunction.newElement(punctuation, port);
	}
	
	@Override
	@SuppressWarnings({"rawtypes"})
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof UnionPO)) {
			return false;
		}
		UnionPO<?> upo = (UnionPO) ipo;
		if(this.hasSameSources(upo)) {
			return true;
		}
		return false;
	}
	
	@Override
	public long getElementsStored1() {
		return transferFunction.size();
	}
}
