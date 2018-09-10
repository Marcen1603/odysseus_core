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
package de.uniol.inf.is.odysseus.rewrite.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteWorkingMemory;
import de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule;

public abstract class AbstractRewriteRule<T> extends
		AbstractRule<T, RewriteConfiguration> {

	protected ISession getCaller() {
		return ((RewriteWorkingMemory) this.getCurrentWorkingMemory())
				.getCaller();
	}

	protected IDataDictionaryWritable getDataDictionary() {
		return (IDataDictionaryWritable) ((RewriteWorkingMemory) this
				.getCurrentWorkingMemory()).getDataDictionary();
	}

	@Override
	final public String getName() {
		return getClass().getName();
	}
	
	protected boolean isLastOne(ILogicalOperator operator) {
		if (operator.getSubscriptions().size() == 1) {
			if (operator.getSubscriptions().iterator().next().getSink() instanceof TopAO) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Returns null, if there is not exactly one operator subscribing the
	 * operator or if it's not the correct type.
	 */
	protected ILogicalOperator getSubscribingOperatorAndCheckType(T t,
			Class<? extends ILogicalOperator> desiredClass) {
		if (!ILogicalOperator.class.isInstance(t)) {
			return null;
		}
		ILogicalOperator operator = (ILogicalOperator) t;
		if (operator.getSubscriptions().size() != 1) {
			return null;
		}
		ILogicalOperator target = operator.getSubscriptions().iterator().next()
				.getSink();
		if (desiredClass.isInstance(target)) {
			return target;
		}
		return null;
	}

}
