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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Marco Grawunder
 */
@SuppressWarnings({"rawtypes"})
public class RoutePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private List<IPredicate<? super T>> predicates;

	public RoutePO(List<IPredicate<? super T>> predicates)  {
		super();
		initPredicates(predicates);
	}

	public RoutePO(RoutePO<T> splitPO) {
		super();
		initPredicates(splitPO.predicates);
	}
	
	private void initPredicates(List<IPredicate<? super T>> predicates) {
		this.predicates = new ArrayList<IPredicate<? super T>>(predicates.size());
		for (IPredicate<? super T> p: predicates){
			this.predicates.add(p.clone());
		}
	}
	


	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected boolean canHandleOutOfOrder() {
		return true;
	}


	@Override
	public void process_open() throws OpenFailedException{
		super.process_open();
		for (IPredicate<? super T> p: predicates){
			p.init();
		}		
	}
	
	@Override
	protected void process_next(T object, int port) {
		for (int i=0;i<predicates.size();i++){
			if (predicates.get(i).evaluate(object)) {
				transfer(object,i);
				return;
			}
		}
		transfer(object,predicates.size());
	}
	
	@Override
	public RoutePO<T> clone() {
		return new RoutePO<T>(this);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof RoutePO)) {
			return false;
		}
		RoutePO spo = (RoutePO) ipo;
		if(this.hasSameSources(spo) &&
				this.predicates.size() == spo.predicates.size()) {
			for(int i = 0; i<this.predicates.size(); i++) {
				if(!this.predicates.get(i).equals(spo.predicates.get(i))) {
					return false;
				}
			}
			return true;
		}
        return false;
	}

}
