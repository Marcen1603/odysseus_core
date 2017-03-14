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
package de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "TupleToKeyValue", doc = "deprecated. Use ToKeyValue instead", category = { LogicalOperatorCategory.TRANSFORM }, deprecation=true, hidden= true)
@Deprecated
public class TupleToKeyValueAO extends ToKeyValueAO {
	private static final long serialVersionUID = 3215936185841514847L;

	public TupleToKeyValueAO() {
	}

	public TupleToKeyValueAO(TupleToKeyValueAO tupleToKeyValue) {
		super(tupleToKeyValue);
	}


}
