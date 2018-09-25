/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;

/**
 * 
 * @author Dennis Geesen, Marco Grawunder Created at: 29.05.2012
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "CHANGECORRELATE", doc = "Operator used in DEBS Grand Challenge 2012", category = { LogicalOperatorCategory.PATTERN })
public class ChangeCorrelateAO extends BinaryLogicalOp implements IHasPredicates {

	// Predicates need to be stored in AbstractLogical Operator
	// remember positions
	int counter = 0;
	private int leftHighPredicate;
	private int leftLowPredicate;
	private int rightHighPredicate;
	private int rightLowPredicate;

	private List<IPredicate<?>> predicates = new LinkedList<IPredicate<?>>();

	private static final long serialVersionUID = 2341933392781838294L;

	public ChangeCorrelateAO() {
		super();
	}

	public ChangeCorrelateAO(ChangeCorrelateAO changeCorrelateAO) {
		super(changeCorrelateAO);
		if (changeCorrelateAO.predicates != null) {
			for (IPredicate<?> pred : changeCorrelateAO.predicates) {
				this.predicates.add(pred.clone());
			}
		}
		this.leftHighPredicate = changeCorrelateAO.leftHighPredicate;
		this.leftLowPredicate = changeCorrelateAO.leftLowPredicate;
		this.rightHighPredicate = changeCorrelateAO.rightHighPredicate;
		this.rightLowPredicate = changeCorrelateAO.rightLowPredicate;
	}

	public IPredicate<?> getLeftHighPredicate() {
		return getPredicates().get(leftHighPredicate);
	}

	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int pos) {
		// The Sum of all InputSchema
		SDFSchema outputSchema = null;
		for (LogicalSubscription l : getSubscribedToSource()) {
			outputSchema = SDFSchema.union(outputSchema, l.getSchema());
		}
		setOutputSchema(outputSchema);
		return outputSchema;
	}

	@Parameter(type = PredicateParameter.class, name = "lefthighpredicate")
	public void setLeftHighPredicate(IPredicate<?> leftHighPredicate) {
		this.leftHighPredicate = counter++;
		predicates.add(leftHighPredicate);
	}

	public IPredicate<?> getLeftLowPredicate() {
		return getPredicates().get(leftLowPredicate);
	}

	@Parameter(type = PredicateParameter.class, name = "leftlowpredicate")
	public void setLeftLowPredicate(IPredicate<?> leftLowPredicate) {
		this.leftLowPredicate = counter++;
		predicates.add(leftLowPredicate);
	}

	public IPredicate<?> getRightHighPredicate() {
		return getPredicates().get(rightHighPredicate);
	}

	@Parameter(type = PredicateParameter.class, name = "righthighpredicate")
	public void setRightHighPredicate(IPredicate<?> rightHighPredicate) {
		this.rightHighPredicate = counter++;
		predicates.add(rightHighPredicate);
	}

	public IPredicate<?> getRightLowPredicate() {
		return getPredicates().get(rightLowPredicate);
	}

	@Parameter(type = PredicateParameter.class, name = "rightlowpredicate")
	public void setRightLowPredicate(IPredicate<?> rightLowPredicate) {
		this.rightLowPredicate = counter++;
		predicates.add(rightLowPredicate);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ChangeCorrelateAO(this);
	}

	@Override
	public List<IPredicate<?>> getPredicates() {
		return predicates;
	}

	@Override
	public void setPredicates(List<IPredicate<?>> predicates) {
		this.predicates = predicates;
	}


}
