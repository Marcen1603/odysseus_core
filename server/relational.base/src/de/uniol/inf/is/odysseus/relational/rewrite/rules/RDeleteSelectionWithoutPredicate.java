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
package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RDeleteSelectionWithoutPredicate extends AbstractRelationalRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SelectAO sel, RewriteConfiguration transformConfig) {
		Collection<ILogicalOperator> toUpdate = removeOperator(sel);		
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
	
		retract(sel);	
	}

	@Override
	public boolean isExecutable(SelectAO sel, RewriteConfiguration transformConfig) {
		return sel.getPredicate()==null ||  sel.getPredicate().isAlwaysTrue();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.DELETE;
	}
	
	@Override
	public Class<? super SelectAO> getConditionClass() {	
		return SelectAO.class;
	}

}
