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
package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;

@SuppressWarnings({"rawtypes"})
public class Write<R> extends AbstractAggregateFunction<R, R> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4490710243269539616L;
	private static Write instance;

	private Write() {
		super("WRITE");
	}
	
	static public Write getInstance(){
		if (instance == null){
			instance = new Write();
		}
		return instance;
	}

	@Override
	public IPartialAggregate<R> init(R in) {
		return new ElementPartialAggregate<R>(in);
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge,
			boolean createNew) {
		((ElementPartialAggregate<R>)p).setElem(toMerge);
		return p;
	}

	@Override
	public R evaluate(IPartialAggregate<R> p) {
		return ((ElementPartialAggregate<R>)p).getElem();
	}

	
}
