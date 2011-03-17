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
package de.uniol.inf.is.odysseus.logicaloperator.latency;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@LogicalOperator(name = "CALCLATENCY", minInputPorts = 1, maxInputPorts = Integer.MAX_VALUE)
public class LatencyCalculationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9078812899082643674L;

	public LatencyCalculationAO() {
		super();
	}

	public LatencyCalculationAO(LatencyCalculationAO ao) {
		super(ao);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LatencyCalculationAO(this);
	}

}
