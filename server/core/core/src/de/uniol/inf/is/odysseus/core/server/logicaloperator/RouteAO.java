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

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;

@LogicalOperator(name = "ROUTE", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can be used to route the elements in the stream to different further processing operators, depending on the predicate.", category = { LogicalOperatorCategory.PROCESSING })
public class RouteAO extends UnaryLogicalOp implements IHasPredicates{

	private static final long serialVersionUID = -8015847502104587689L;

	private boolean overlappingPredicates = false;
	private List<IPredicate<?>> predicates = new LinkedList<IPredicate<?>>();

	/**
	 * if an element is routed to an output, heartbeats will be send to all
	 * other outputs.
	 */
	private boolean sendingHeartbeats = false;

	public RouteAO() {
		super();
	}

	public RouteAO(RouteAO routeAO) {
		super(routeAO);
		this.overlappingPredicates = routeAO.overlappingPredicates;
		this.sendingHeartbeats = routeAO.sendingHeartbeats;
		if (routeAO.predicates != null) {
			for (IPredicate<?> pred : routeAO.predicates) {
				this.predicates.add(pred.clone());
			}
		}
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		// since it is a routing, schema is always from input port 0
		return super.getOutputSchemaIntern(0);
	}

	@Parameter(type = PredicateParameter.class, isList = true)
	public void setPredicates(List<IPredicate<?>> predicates) {
		this.predicates = predicates;
		addParameterInfo("PREDICATES", generatePredicatesString(predicates));
	}
	
	@Override
	public List<IPredicate<?>> getPredicates() {
		return predicates;
	}

	private String generatePredicatesString(List<IPredicate<?>> predicates) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < predicates.size(); i++) {
			IPredicate<?> predicate = predicates.get(i);
			sb.append(generatePredicateString(predicate));
			if (i < predicates.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private String generatePredicateString(IPredicate<?> predicate) {
		return "RelationalPredicate('" + predicate.toString() + "')";
	}

	@Parameter(name = "overlappingPredicates", type = BooleanParameter.class, optional = true, doc = "Evaluate all (true) or only until first true predicate (false), i.e. deliver to all ports where predicate is true or only to first")
	public void setOverlappingPredicates(boolean overlappingPredicates) {
		this.overlappingPredicates = overlappingPredicates;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RouteAO(this);
	}

	/**
	 * @return
	 */
	public boolean isOverlappingPredicates() {
		return this.overlappingPredicates;
	}

	@Parameter(name = "sendingHeartbeats", type = BooleanParameter.class, optional = true, doc = "If an element is routed to an output, heartbeats will be send to all other outputs")
	public void setSendingHeartbeats(boolean sendingHeartbeats) {

		this.sendingHeartbeats = sendingHeartbeats;

	}

	public boolean isSendingHeartbeats() {

		return this.sendingHeartbeats;

	}

}
