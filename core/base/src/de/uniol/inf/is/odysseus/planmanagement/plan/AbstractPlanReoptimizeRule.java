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
package de.uniol.inf.is.odysseus.planmanagement.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule;

/**
 * This class is a base object for creating reoptimization rules for global plans. It
 * provides methods to store global plans and to send reoptimize
 * request to these global plans if this rule is valid.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractPlanReoptimizeRule 
	implements IReoptimizeRule<IPhysicalPlan> {

	/**
	 * List of global plans which are informed if this rule is valid.
	 */
	protected List<IPhysicalPlan> reoptimizable = Collections.synchronizedList(new ArrayList<IPhysicalPlan>());
	
	/**
	 * Informs all registered global plans that this rule is valid.
	 */
	protected void fireReoptimizeEvent() {
		for (IPhysicalPlan reoptimizableType : this.reoptimizable) {
			reoptimizableType.reoptimize();
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#addReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void addReoptimieRequester(IPhysicalPlan reoptimizable) {
		this.reoptimizable.add(reoptimizable);
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#removeReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void removeReoptimieRequester(IPhysicalPlan reoptimizable) {
		this.reoptimizable.remove(reoptimizable);
	}
	
	public abstract void deinitialize();
}
