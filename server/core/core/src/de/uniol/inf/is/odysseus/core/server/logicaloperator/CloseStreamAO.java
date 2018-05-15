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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Marco Grawunder
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CLOSESTREAM", doc = "This operator allow to stop stream processing based on a predicate.", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Close+Stream+operator", category = {
		LogicalOperatorCategory.BENCHMARK })
public class CloseStreamAO extends UnaryLogicalOp implements IHasPredicate {
	private static final long serialVersionUID = 3215936185841514846L;
	private long maxCount = 0;

	private IPredicate<?> predicate;

	public CloseStreamAO() {
		super();
	}

	public CloseStreamAO(CloseStreamAO ao) {
		super(ao);
		this.maxCount = ao.maxCount;
		if (predicate != null) {
			this.predicate = ao.predicate.clone();
		}
	}

	public CloseStreamAO(IPredicate<?> predicate) {
		setPredicate(predicate);
	}

	@SuppressWarnings("rawtypes")
	@Parameter(type = PredicateParameter.class, optional = true)
	public void setPredicate(IPredicate predicate) {
		this.predicate = predicate;

	}

	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}

	@Parameter(name = "count", type = LongParameter.class, optional = true)
	public void setMaxCount2(Long maxCount) {
		this.maxCount = maxCount;
	}

	public long getMaxCount() {
		return maxCount;
	}

	@Override
	public CloseStreamAO clone() {
		return new CloseStreamAO(this);
	}

	@Override
	public boolean isValid() {
		if (maxCount == 0 && predicate == null) {
			addError("Must use maxCount or predicate");
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + getPredicate();
	}

}
