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
package de.uniol.inf.is.odysseus.rewrite.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.rewrite.flow.IRewriteRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;

public class RewriteExecutor implements IRewrite {

	public static final Logger LOGGER = LoggerFactory.getLogger("rewrite");

	@Override
	public ILogicalPlan rewritePlan(ILogicalPlan logicalPlan,
			RewriteConfiguration conf, ISession caller, IDataDictionary dd) {
		LOGGER.info("Starting rewriting...");

		ILogicalOperator root = logicalPlan.getRoot();
		
		Set<String> rulesToApply = conf.getRulesToApply();

		final RewriteInventory rewriteInventory;
		if (rulesToApply == null) {
			rewriteInventory = new RewriteInventory(
					RewriteInventory.getInstance());
		} else {
			rewriteInventory = new RewriteInventory(
					RewriteInventory.getInstance(),rulesToApply);
		}

		RewriteEnvironment env = new RewriteEnvironment(conf, rewriteInventory,
				caller, dd);

		TopAO top = null;
		final boolean createdNewTopAO;
		if (root instanceof TopAO) {
			top = (TopAO) root;
			createdNewTopAO = false;
		} else {
			top = new TopAO();
			SDFSchema outputSchema = root.getOutputSchema();
			root.subscribeSink(top, 0, 0, outputSchema);
			createdNewTopAO = true;
		}

		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		addLogicalOperator(top, list, env);
		// *******
		if (LOGGER.isTraceEnabled()) {
			SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>();
			LOGGER.trace("Before rewriting: \n"
					+ planPrinter.createString(root));
			LOGGER.trace("Processing rules...");
		}
		// start transformation
		try {
			env.processEnvironment();
		} catch (RuleException e) {
			throw new TransformationException(e);
		}
		LOGGER.trace("Processing rules done.");
		final ILogicalOperator ret;
		if (createdNewTopAO) {
			LogicalSubscription sub = top.getSubscribedToSource(0);
			ret = sub.getSource();
			top.unsubscribeFromSource(ret, sub.getSinkInPort(),
					sub.getSourceOutPort(), sub.getSchema());
		} else {
			ret = top;
		}
		if (LOGGER.isTraceEnabled()) {
			SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>();
			LOGGER.trace("After rewriting: \n" + planPrinter.createString(ret));
		}
		LOGGER.info("Rewriting finished.");
		env.getWorkingMemory().clear();
		return new LogicalPlan(ret);
	}

	private void addLogicalOperator(ILogicalOperator op,
			List<ILogicalOperator> inserted, RewriteEnvironment env) {
		if (op == null) {
			return;
		}

		if (!inserted.contains(op)) {
			env.getWorkingMemory().insertObject(op);
			inserted.add(op);

			for (LogicalSubscription sub : op.getSubscribedToSource()) {
				addLogicalOperator(sub.getSource(), inserted, env);
			}
			for (LogicalSubscription sub : op.getSubscriptions()) {
				addLogicalOperator(sub.getSink(), inserted, env);
			}
		}
	}

	public void addRuleProvider(IRewriteRuleProvider provider) {
		RewriteInventory.getInstance().bindRuleProvider(provider);
	}

	public void removeRuleProvider(IRewriteRuleProvider provider) {
		RewriteInventory.getInstance().unbindRuleProvider(provider);
	}

	@Override
	public Collection<String> getRewriteRules() {
		return RewriteInventory.getInstance().getRules();
	}

}
