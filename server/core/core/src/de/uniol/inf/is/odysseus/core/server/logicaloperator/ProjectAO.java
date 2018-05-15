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
/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Marco Grawunder
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "PROJECT", doc = "Make a projection on the input object (i.e. filter attributes)", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Project+operator", category = {
		LogicalOperatorCategory.BASE })
public class ProjectAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 5487345119018834806L;
	private List<SDFAttribute> attributes = new ArrayList<>();

	public ProjectAO() {
		super();
	}

	public ProjectAO(ProjectAO ao) {
		super(ao);
		this.attributes = new ArrayList<>(ao.attributes);
	}

	public @Override ProjectAO clone() {
		return new ProjectAO(this);
	}

	public int[] determineRestrictList() {
		int[] ret = SDFSchema.calcRestrictList(this.getInputSchema(), this.getOutputSchema());
		return ret;
	}

	// Must be another name than setOutputSchema, else this method is not found!
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", aliasname = "PATHS", optional = false, isList = true, doc = "A list of attributes that should be used.")
	public void setOutputSchemaWithList(List<SDFAttribute> outputSchema) {
		attributes = outputSchema;
	}

	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	public List<SDFAttribute> getOutputSchemaWithList() {
		return attributes;
	}

	public SDFSchema getOutputSchemaIntern() {
		try {
			if (getInputSchema().getType().newInstance().isSchemaLess()) {
				return getInputSchema();
			} else {
				return SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.
	 * AbstractLogicalOperator #getOutputSchemaIntern(int)
	 */
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema());
	}

	@Override
	public void initialize() {
		/// WTF ???
		/// setOutputSchema(new SDFSchema(getInputSchema().getURI(),
		/// getOutputSchema()));
	}

	/**
	 * There should be no restriction for stateless operators
	 */
	@Override
	public InputOrderRequirement getInputOrderRequirement(int inputPort) {
		return InputOrderRequirement.NONE;
	}

}
