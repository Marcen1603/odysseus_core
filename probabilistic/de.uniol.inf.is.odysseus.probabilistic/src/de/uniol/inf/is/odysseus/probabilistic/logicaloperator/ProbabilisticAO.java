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
package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "Probabilistic")
public class ProbabilisticAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5230887041196691992L;
	private List<SDFAttribute> attributes;

	/**
	 * Constructs a new Probabilistic logical operator.
	 */
	public ProbabilisticAO() {
		super();
	}

	/**
	 * Clone constructor.
	 * 
	 * @param updateProbabilisticMetadataAO
	 *            The copy
	 */
	public ProbabilisticAO(final ProbabilisticAO updateProbabilisticMetadataAO) {
		super(updateProbabilisticMetadataAO);
		this.attributes = updateProbabilisticMetadataAO.attributes;
	}

	/**
	 * Sets the list of attributes.
	 * 
	 * @param attributes
	 *            The attributes
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = true)
	public final void setProbabilities(final List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the list of attributes.
	 * 
	 * @return The list of attributes
	 */
	public final List<SDFAttribute> getAttributes() {
		return this.attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public final AbstractLogicalOperator clone() {
		return new ProbabilisticAO(this);
	}

}
