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

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

@LogicalOperator(name="ROUTE", minInputPorts=1, maxInputPorts=1)
public class RouteAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8015847502104587689L;
	
	public RouteAO(){
		super();
	}
	
	public RouteAO(RouteAO routeAO){
		super(routeAO);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		// since it is a routing, schema is always from input port 0
		return super.getOutputSchemaIntern(0);
	}

	@Override
	@Parameter(type=PredicateParameter.class, isList=true)
	public void setPredicates(List<IPredicate<?>> predicates) {
		super.setPredicates(predicates);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new RouteAO(this);
	}
	
}
