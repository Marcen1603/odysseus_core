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
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Cleans up an operator plan. Removes metadata listeners.
 * 
 * @author Tobias Witt
 *
 */
public class CleanOperatorsVisitor implements INodeVisitor<IPhysicalOperator, Object> {
	
	Logger logger = LoggerFactory.getLogger(CleanOperatorsVisitor.class);
	
	private IQuery query;
	
	public CleanOperatorsVisitor(IQuery query) {
		this.query = query;
	}

	@Override
	public void ascendAction(IPhysicalOperator to) {
	}

	@Override
	public void descendAction(IPhysicalOperator to) {
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public void nodeAction(IPhysicalOperator op) {
		logger.debug("Remove Owner "+this.query+" from "+op);
		op.removeOwner((IOperatorOwner) this.query);
		if (!op.hasOwner()) {
			for (String data : op.getProvidedMonitoringData()) {
				// TODO: removeMonitoringData sollte auch die metadata listener aus der eventListener Map entfernen 
				op.removeMonitoringData(data);
			}
			// TODO: Das scheint irgendwie wo anders hinzugehören	
			((ISink<?>)op).close();
		}
	}

}
