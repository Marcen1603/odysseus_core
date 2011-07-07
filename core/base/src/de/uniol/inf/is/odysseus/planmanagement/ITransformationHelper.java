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
package de.uniol.inf.is.odysseus.planmanagement;

import java.util.Collection;

import de.uniol.inf.is.odysseus.ISubscription;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
@SuppressWarnings({"unchecked","rawtypes"})
public interface ITransformationHelper {
	
	public Collection<ILogicalOperator> replace(ILogicalOperator logical, IPipe physical);

	public Collection<ILogicalOperator> replace(ILogicalOperator logical, ISink physical);
	public Collection<ILogicalOperator> replace(ILogicalOperator logical, ISource physical);	
	/**
	 * Inserts a new operator between a physical and a logical operator.
	 * 
	 * @param oldFather The old lower operator (from the dataflow point of view)
	 * @param children The following operators of oldFather (from the dataflow point of view)
	 * @param newFather The new lower operator. oldFather becomes the father of newFather
	 * @return the modified children must be returned to update the drools working memory
	 */
	public Collection<ILogicalOperator> insertNewFather(ISource oldFather, Collection<ILogicalOperator> children, IPipe newFather);
	
	/**
	 * Inserts a new operator into a completely transformed physical query plan.
	 * 
	 * @param oldFather The old lower operator (from the dataflow point of view)
	 * @param children The following operators of oldFather (from the dataflow point of view)
	 * @param newFather The new lower operator. oldFather becomes the father of newFather
	 * @return the modified children must be returned to update the drools working memory
	 */
	public Collection<ISink> insertNewFatherPhysical(ISource oldFather, Collection<ISubscription<ISink>> children, IPipe newFather);

	public boolean containsWindow(ILogicalOperator inputOp);

}
