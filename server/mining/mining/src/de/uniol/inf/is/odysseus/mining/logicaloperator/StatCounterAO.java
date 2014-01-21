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
package de.uniol.inf.is.odysseus.mining.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

/**
 * @author Dennis Geesen
 * 
 */
@LogicalOperator(name = "COUNTSTATS", minInputPorts = 1, maxInputPorts = 1, doc="TODO",category={LogicalOperatorCategory.BENCHMARK, LogicalOperatorCategory.MINING})
public class StatCounterAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 4443355945512399432L;
	public int outputeach;

	public StatCounterAO() {
		
	}

	/**
	 * @param statCounterAO
	 */
	public StatCounterAO(StatCounterAO statCounterAO) {
		this.outputeach = statCounterAO.outputeach;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new StatCounterAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		SDFAttribute attributeCount = new SDFAttribute(null, "count", SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeCount);
		SDFAttribute attributeNeeded = new SDFAttribute(null, "needed", SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeNeeded);
		SDFAttribute attributeTotal = new SDFAttribute(null, "total", SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeTotal);
		SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), getInputSchema(0).getType(), attributes);
		return outSchema;

	}
	
	@Parameter(name="outputeach", type=IntegerParameter.class)
	public void setOutputeach(int each){
		this.outputeach = each;
	}
	
	public int getOutputeach(){
		return this.outputeach;
	}
}
