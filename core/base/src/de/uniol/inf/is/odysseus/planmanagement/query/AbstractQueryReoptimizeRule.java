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
package de.uniol.inf.is.odysseus.planmanagement.query;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule;

/**
 * This class is a base object for creating reoptimization rules for queries. It
 * provides methods to store queries and to send reoptimize
 * request to these queries if this rule is valid.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractQueryReoptimizeRule implements
		IReoptimizeRule<IQuery> {

	/**
	 * List of queries which are informed if this rule is valid.
	 */
	protected ArrayList<IQuery> reoptimizable;
	
	/**
	 * Informs all registered queries that this rule is valid.
	 */
	protected void fireReoptimizeEvent() {
		for (IQuery reoptimizableType : this.reoptimizable) {
			reoptimizableType.reoptimize();
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#addReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void addReoptimieRequester(IQuery reoptimizable) {
		this.reoptimizable.add(reoptimizable);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#removeReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void removeReoptimieRequester(IQuery reoptimizable) {
		this.reoptimizable.remove(reoptimizable);
	}
}
