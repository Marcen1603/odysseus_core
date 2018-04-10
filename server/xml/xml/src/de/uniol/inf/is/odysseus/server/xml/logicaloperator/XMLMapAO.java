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
package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "XMLMAP", doc = "Performs a mapping of incoming attributes to out-coming attributes using map functions. Odysseus also provides a wide range of mapping functions. Hint: Map is stateless. To used Map in a statebased fashion see: StateMap", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Map+operator", category = {LogicalOperatorCategory.BASE})
public class XMLMapAO extends UnaryLogicalOp
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8811796037313025039L;
	private List<SDFAttribute> target;
	private List<SDFAttribute> source;

	public XMLMapAO()
	{
		super();
	}

	public XMLMapAO(XMLMapAO ao)
	{
		super(ao);
		this.setSource(ao.source);
		this.setTarget(ao.target);
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "source", isList = true, optional = false, doc = "A list of expressions.")
	public void setSource(List<SDFAttribute> sourceExpressions)
	{
		this.source = sourceExpressions;
		source = new ArrayList<>();
		if (sourceExpressions != null)
		{
			for (SDFAttribute e : sourceExpressions)
			{
				source.add(e);
			}
		}
		setOutputSchema(null);
	}

	public List<SDFAttribute> getTarget()
	{
		return this.target;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "target", isList = true, optional = false, doc = "A list of expressions.")
	public void setTarget(List<SDFAttribute> targetExpressions)
	{
		this.target = targetExpressions;
		target = new ArrayList<>();
		if (targetExpressions != null)
		{
			for (SDFAttribute e : targetExpressions)
			{
				target.add(e);
			}
		}
		setOutputSchema(null);
	}

	public List<SDFAttribute> getSource()
	{
		return this.source;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos)
	{
		return SDFSchemaFactory.createNewWithAttributes(target, getInputSchema());
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public XMLMapAO clone()
	{
		return new XMLMapAO(this);
	}
}
