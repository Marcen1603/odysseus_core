/********************************************************************************** 
  * Copyright 2013 The Odysseus Team
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
import java.util.Collection;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Dennis Geesen
 *
 */
@LogicalOperator(name = "XMLJOIN", minInputPorts = 2, maxInputPorts = 2, doc = "XML Join operator", category = {LogicalOperatorCategory.BASE})
public class JoinAO extends BinaryLogicalOp implements IHasPredicate
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8451182921980419314L;
	private int minimumSize = 0;
	private IPredicate<?> predicate;
	private String targetPath;

	public JoinAO()
	{

	}

	public JoinAO(JoinAO joinAO)
	{
		super(joinAO);
		this.minimumSize = joinAO.minimumSize;
		this.targetPath = joinAO.targetPath;
		if (joinAO.predicate != null)
		{
			this.predicate = joinAO.predicate.clone();
		}
	}

	@Override
	public JoinAO clone()
	{
		return new JoinAO(this);
	}

	@Parameter(name = "target", type = StringParameter.class, optional = false, doc = "Specifies the target path in the first object where the second object is joined onto.")
	public void setTargetPath(String _targetPath)
	{
		this.targetPath = _targetPath;
	}

	public String getTargetPath()
	{
		return this.targetPath;
	}

	@Parameter(name = "minimumSize", type = IntegerParameter.class, optional = true, doc = "Blocks all until there are at least minimumSize elements in the chache")
	public synchronized void setMinimumSize(int i)
	{
		this.minimumSize = i;
	}

	public int getMinimumSize()
	{
		return this.minimumSize;
	}

	@Parameter(name = "predicate", type = PredicateParameter.class, doc = "Predicate to filter combinations")
	public synchronized void setPredicate(IPredicate<?> joinPredicate)
	{
		this.predicate = joinPredicate;
	}

	@Override
	public IPredicate<?> getPredicate()
	{
		return predicate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int pos)
	{
			Collection<SDFAttribute> emptyAttributes = new ArrayList<>();
			SDFSchema newOutputSchema = SDFSchemaFactory.createNewSchema(getInputSchema(pos).getURI(),	(Class<? extends IStreamObject<?>>) XMLStreamObject.class, emptyAttributes);
			setOutputSchema(newOutputSchema);
			return newOutputSchema;
	}	

}
