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
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class FilterPO<T extends IStreamObject<?>> extends SelectPO<T> implements IHasPredicate{

	public FilterPO(IPredicate<? super T> predicate){
		super(predicate);
	}
	
	public FilterPO(FilterPO<T> po){
		super(po);
	}
	
	@Override
	protected void process_next(T object, int port) {
		if (getPredicate().evaluate(object)) {
			transfer(object);
		}else{
			// Send filtered data to output port 1
			transfer(object,1);
			getHeartbeatGenerationStrategy().generateHeartbeat(object, this);
		}
	}
	
	@Override
	public FilterPO<T> clone() {
		return new FilterPO<T>(this);
	}

	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof FilterPO<?>)) {
			return false;
		}
		return super.process_isSemanticallyEqual(ipo);
	}	
}

