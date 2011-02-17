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

/**
 * Needed because TimeIntervals and PointInTimes have other compare operations e.g. overlaps, before, equals, etc.
 * @author abolles
 *
 */
@SuppressWarnings("unchecked")
public class AfterPredicate<M extends IPosNeg> extends AbstractPredicate<IMetaAttributeContainer<M>>{

	private static final long serialVersionUID = 1L;
	public static AfterPredicate theInstance = new AfterPredicate();
	
	@Override
	@Deprecated
	public boolean evaluate(IMetaAttributeContainer<M> e){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<M> e1, IMetaAttributeContainer<M> e2){
		return e1.getMetadata().getTimestamp().after(e2.getMetadata().getTimestamp());
	}
	
	@Override
	public AfterPredicate clone(){
		return theInstance;
	}
	
	public static AfterPredicate getInstance(){
		return theInstance;
	}
}
