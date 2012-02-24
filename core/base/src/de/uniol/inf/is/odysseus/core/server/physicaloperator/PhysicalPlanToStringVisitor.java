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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.util.INodeVisitor;

/**
 * Builds an string representation of a physical plan.
 * 
 * @author Tobias Witt
 *
 */
public class PhysicalPlanToStringVisitor implements INodeVisitor<IPhysicalOperator, String> {
	
	private StringBuilder builder;
	private int depth;
	private boolean showSchema;
	
	public PhysicalPlanToStringVisitor() {
		reset();
	}
	
	public PhysicalPlanToStringVisitor(boolean showSchema) {
		reset();
		this.showSchema = showSchema;
	}

	@Override
	public void ascendAction(IPhysicalOperator to) {
		this.depth--;
	}

	@Override
	public void descendAction(IPhysicalOperator to) {
		this.depth++;
	}

	@Override
	public String getResult() {
		return this.builder.toString();
	}

	@Override
	public void nodeAction(IPhysicalOperator op) {
		for (int i=0; i<this.depth; i++) {
			this.builder.append("  ");
		}
		//this.builder.append(op.getName());
		this.builder.append(op.toString());
		if(showSchema) {
			this.builder.append("["+op.getOutputSchema()+"]");
		}
		this.builder.append('\n');
	}
	
	public void reset() {
		this.builder = new StringBuilder();
		this.depth = 0;
	}

}
