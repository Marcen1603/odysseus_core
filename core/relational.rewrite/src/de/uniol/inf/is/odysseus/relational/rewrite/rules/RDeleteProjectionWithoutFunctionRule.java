/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RDeleteProjectionWithoutFunctionRule extends AbstractRewriteRule<ProjectAO> {

	@Override
	public int getPriority() {		
		return 5;
	}

	@Override
	public void execute(ProjectAO proj, RewriteConfiguration transformConfig) {
		Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.removeOperator(proj);		
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		 
		// Den ProjectAO aus dem working memory löschen
		retract(proj);
		
	}

	@Override
	public boolean isExecutable(ProjectAO proj, RewriteConfiguration transformConfig) {
		return proj.getInputSchema().equals(proj.getOutputSchema());
	}

	@Override
	public String getName() {
		return "Delete Projection without function";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.DELETE;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return ProjectAO.class;
	}

}
