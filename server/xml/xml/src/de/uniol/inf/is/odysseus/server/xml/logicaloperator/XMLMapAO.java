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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "XMLMAP", doc = "Performs a mapping of incoming attributes to out-coming attributes using map functions. Odysseus also provides a wide range of mapping functions. Hint: Map is stateless. To used Map in a statebased fashion see: StateMap", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Map+operator", category = {LogicalOperatorCategory.BASE })
public class XMLMapAO extends MapAO {

	private static final long serialVersionUID = -8811796037313025039L;
	private boolean tupleOutput;

	public XMLMapAO() {
		super();
	}

	public XMLMapAO(XMLMapAO ao) {
		super(ao);
		this.tupleOutput = ao.isTupleOutput();
	}

	@Override
	public void initialize() { }

	@Override
	public XMLMapAO clone() {
		return new XMLMapAO(this);
	}
	
	public boolean isTupleOutput() {
		return tupleOutput;
	}
	
}
