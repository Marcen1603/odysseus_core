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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Marco Grawunder
 * 
 * Similar to select, but splits data into filtered and not filtered
 * 
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="FILTER")
public class FilterAO extends SelectAO {

	private static final long serialVersionUID = 2008672448859070252L;

	public FilterAO() {
		super();		
	}

	public FilterAO(FilterAO po) {
		super(po);
	}

	@Override
	public FilterAO clone() {
		return new FilterAO(this);
	}
	

}
