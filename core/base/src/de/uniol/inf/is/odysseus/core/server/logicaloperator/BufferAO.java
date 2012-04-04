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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "BUFFER", minInputPorts = 1, maxInputPorts = 1)
public class BufferAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 9204364375031967542L;

	private String type;

	public BufferAO(BufferAO ao) {
		super(ao);
		this.type = ao.type;
	}

	public BufferAO() {
	}

	@Override
	public BufferAO clone() {
		return new BufferAO(this);
	}

	public String getType() {
		return type;
	}

	@Parameter(type = StringParameter.class)
	public void setType(String type) {
		this.type = type;
	}

}
