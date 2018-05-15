/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.logicaloperator.IParallelizableOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Marco Grawunder
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "STATEMAP", doc = "Performs a mapping of incoming attributes to out-coming attributes using map functions. Odysseus also provides a wide range of mapping functions. Hint: StateMap can use history information. To access the last n.th version of an attribute use \"__last_n.\" Mind the two \"_\" at the beginning!", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/StateMap+operator", category = {
		LogicalOperatorCategory.BASE })
public class StateMapAO extends MapAO implements IStatefulAO, IParallelizableOperator {

	private static final long serialVersionUID = 1695948732660010522L;
	private boolean allowNull = true;
	private List<SDFAttribute> groupingAttributes;

	public StateMapAO() {
		super();
	}

	public StateMapAO(StateMapAO ao) {
		super(ao);
		allowNull = ao.allowNull;
		this.groupingAttributes = ao.groupingAttributes;
	}

	@Override
	public StateMapAO clone() {
		return new StateMapAO(this);
	}

	@Parameter(type = BooleanParameter.class, optional = true, deprecated=true)
	public void setAllowNullInOutput(boolean allowNull) {
		this.allowNull = allowNull;
	}

	/**
	 * @return
	 */
	public boolean isAllowNullInOutput() {
		return allowNull;
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	@Override
	public OperatorStateType getStateType() {
		if (this.groupingAttributes.isEmpty()) {
			return IOperatorState.OperatorStateType.ARBITRARY_STATE;
		} else {
			return IOperatorState.OperatorStateType.PARTITIONED_STATE;
		}
	}

	@Override
	public boolean isParallelizable() {
		//FIXME check expressions
		return true;
	}

}
