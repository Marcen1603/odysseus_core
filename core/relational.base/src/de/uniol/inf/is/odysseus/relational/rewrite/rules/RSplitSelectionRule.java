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

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.relational.base.Relational;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

@SuppressWarnings({"rawtypes"})
public class RSplitSelectionRule extends AbstractRewriteRule<SelectAO> {

	private static final String RULE_NAME = "Split Selection with more than one Predicate";

	@Override
	public int getPriority() {
		return 4;
	}

	@Override
	public void execute(SelectAO sel, RewriteConfiguration config) {
		List<IPredicate> preds = splitPredicate(sel.getPredicate());
		
		for (int i = 0; i < preds.size() - 1; i++) {
			SelectAO newSel = new SelectAO(sel);
			for (IOperatorOwner owner:sel.getOwner()){
				newSel.addOwner(owner);
			}
			newSel.setPredicate(preds.get(i));
			
			replacePredicateParameterInfo(newSel, preds.get(i));
			
			RestructHelper.insertOperator(newSel, sel, 0, 0, 0);
			insert(newSel);
		}
		sel.setPredicate(preds.get(preds.size() - 1));
		replacePredicateParameterInfo(sel, preds.get(preds.size() - 1));
		
		update(sel);
	}

	private static List<IPredicate> splitPredicate(IPredicate sel) {
		List<IPredicate> preds;
		if (ComplexPredicateHelper.isAndPredicate(sel)) {
			preds = ComplexPredicateHelper.splitPredicate(sel);
		} else { 
			preds = ((RelationalPredicate)sel).splitPredicate(false);
		}
		return preds;
	}

	private static void replacePredicateParameterInfo(ILogicalOperator sel, IPredicate newPredicate) {
		String predicateString = sel.getParameterInfos().get("PREDICATE");
		int pos = predicateString.indexOf("(");
		String newPredicateString = predicateString.substring(0, pos + 1) + "'" + newPredicate.toString() + "')";
		sel.addParameterInfo("PREDICATE", newPredicateString);
	}

	@Override
	public boolean isExecutable(SelectAO operator, RewriteConfiguration config) {
		if (!config.getQueryBuildConfiguration().getTransformationConfiguration().getDataTypes().contains(Relational.RELATIONAL)){
			return false;
		}
		IPredicate pred = operator.getPredicate();
		if (pred != null) {
			if (ComplexPredicateHelper.isAndPredicate(pred)
					|| (pred instanceof RelationalPredicate && ((RelationalPredicate) pred)
							.isAndPredicate())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return RULE_NAME;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SPLIT;
	}
	
	@Override
	public Class<? super SelectAO> getConditionClass() {	
		return SelectAO.class;
	}

}
