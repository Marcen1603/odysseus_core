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
package de.uniol.inf.is.odysseus.rewrite.engine;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.rewrite.flow.IRewriteRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;

public class RewriteExecutor implements IRewrite {

	private static final String LOGGER_NAME = "rewrite";

	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan, RewriteConfiguration conf) {
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO, "Starting rewriting...");
		RewriteInventory rewriteInventory = new RewriteInventory(RewriteInventory.getInstance());
		
		RewriteEnvironment env = new RewriteEnvironment(conf, rewriteInventory);
		TopAO top = new TopAO();
		SDFSchema outputSchema = plan.getOutputSchema();
		plan.subscribeSink(top, 0, 0, outputSchema);

		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		addLogicalOperator(top, list, env);
		// *******
		SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Before rewriting: \n"+planPrinter.createString(plan));
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Processing rules...");
		// start transformation
		env.processEnvironment();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Processing rules done.");
		LogicalSubscription sub = top.getSubscribedToSource(0);
		ILogicalOperator ret = sub.getTarget();
		top.unsubscribeFromSource(ret, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
		planPrinter = new SimplePlanPrinter<ILogicalOperator>();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "After rewriting: \n"+planPrinter.createString(ret));
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO, "Rewriting finished.");
		return ret;
	}

	private void addLogicalOperator(ILogicalOperator op, List<ILogicalOperator> inserted, RewriteEnvironment env) {
		if (op == null) {
			return;
		}

		if (!inserted.contains(op)) {
			env.getWorkingMemory().insertObject(op);
			inserted.add(op);

			for (LogicalSubscription sub : op.getSubscribedToSource()) {
				addLogicalOperator(sub.getTarget(), inserted, env);
			}
			for (LogicalSubscription sub : op.getSubscriptions()) {
				addLogicalOperator(sub.getTarget(), inserted, env);
			}
		}
	}
	
	public void addRuleProvider(IRewriteRuleProvider provider){		
		RewriteInventory.getInstance().bindRuleProvider(provider);
	}
	
	public void removeRuleProvider(IRewriteRuleProvider provider){		
		RewriteInventory.getInstance().unbindRuleProvider(provider);
	}

}
