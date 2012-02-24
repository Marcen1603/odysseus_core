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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.util.INodeVisitor;

/**
 * Builds an string representation of a logical algebra tree 
 * @author Jonas Jacobi, Marco Grawunder
 */
public class AlgebraPlanToStringVisitor implements INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, String> {

	private StringBuilder builder;
	private boolean wasup;
	private boolean showSchema;

	public AlgebraPlanToStringVisitor() {
		reset();
	}
	
	public AlgebraPlanToStringVisitor(boolean showSchema) {
		reset();
		this.showSchema = showSchema;
		
	}
	
	@Override
	public void descendAction(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		if (this.wasup) {
			this.builder.append(',');
		} else {
			this.builder.append('(');
		}
		this.wasup = false;
	}

	@Override
	public void nodeAction(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		this.builder.append(' ');
		//this.builder.append(((ILogicalOperator)sub).getName()).append(sub.hashCode());
		this.builder.append(((ILogicalOperator)sub));
		if(showSchema) {
			this.builder.append("["+((ILogicalOperator)sub).getOutputSchema()+"]");
		}
	}

	@Override
	public void ascendAction(ISubscriber<ILogicalOperator, LogicalSubscription> sub) {
		if (this.wasup) {
			this.builder.append(" )");	
		}
		this.wasup = true;
	}
	
	@Override
	public String getResult() {
		if (this.wasup) {
			this.builder.append(" )");	
		}
		return this.builder.toString();
	}
	
	public void reset() {
		this.builder = new StringBuilder();
		this.wasup = false;
	}

}
