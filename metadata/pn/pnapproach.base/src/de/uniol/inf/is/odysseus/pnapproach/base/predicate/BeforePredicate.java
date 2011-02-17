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
package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

public class BeforePredicate extends AbstractPredicate<IMetaAttributeContainer<? extends IPosNeg>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9185637480957451344L;
	public static BeforePredicate theInstance = new BeforePredicate();
	
	@Override
	@Deprecated
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> e){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> e1, IMetaAttributeContainer<? extends IPosNeg> e2){
		return e1.getMetadata().getTimestamp().before(e2.getMetadata().getTimestamp());
	}
	
	@Override
	public BeforePredicate clone(){
		return theInstance;
	}
	
	public static BeforePredicate getInstance(){
		return theInstance;
	}
}
