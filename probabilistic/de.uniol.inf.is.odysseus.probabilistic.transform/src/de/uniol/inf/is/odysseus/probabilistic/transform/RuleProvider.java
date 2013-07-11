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
package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TContinuousEquiJoinAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TContinuousEquiJoinAOSetDMRule;
import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TContinuousEquiJoinAOSetSARule;
import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TEMAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TLinearRegressionAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TLinearRegressionMergeAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TProbabilisiticContinuousMapAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.continuous.TProbabilisiticContinuousSelectAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.discrete.TAggregateProbabilisticRule;
import de.uniol.inf.is.odysseus.probabilistic.transform.discrete.TProbabilisiticDiscreteJoinAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.discrete.TProbabilisiticDiscreteMapAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.discrete.TProbabilisiticDiscreteSelectAORule;
import de.uniol.inf.is.odysseus.probabilistic.transform.discrete.TProbabilisticDiscreteJoinAOSetSARule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RuleProvider implements ITransformRuleProvider {

	@Override
	public final List<IRule<?, ?>> getRules() {
		final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TProbabilisticAORule());
		rules.add(new TProbabilisticValidatorRule());

		rules.addAll(this.getProbabilisticDiscreteOperatorRules());
		rules.addAll(this.getProbabilisticContinuousOperatorRules());
		return rules;
	}

	/**
	 * Transformation rules for operators processing discrete probabilistic values.
	 * 
	 * @return Transformation rules for discrete probabilistic values
	 */
	public final List<IRule<?, ?>> getProbabilisticDiscreteOperatorRules() {
		final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		// Probabilistic Aggregation Functions
		rules.add(new TAggregateProbabilisticRule());
		// Join AO -> PO Rule
		rules.add(new TProbabilisiticDiscreteJoinAORule());
		rules.add(new TProbabilisticDiscreteJoinAOSetSARule());
		// Map AO -> PO Rule
		rules.add(new TProbabilisiticDiscreteMapAORule());
		// Select AO -> PO Rule
		rules.add(new TProbabilisiticDiscreteSelectAORule());

		return rules;
	}

	/**
	 * Transformation rules for operators processing continuous probabilistic values.
	 * 
	 * @return Transformation rules for continuous probabilistic values
	 */
	public final List<IRule<?, ?>> getProbabilisticContinuousOperatorRules() {
		final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		// Map AO -> PO Rule
		rules.add(new TProbabilisiticContinuousMapAORule());
		// Select AO -> PO Rule
		rules.add(new TProbabilisiticContinuousSelectAORule());
		// EM AO -> PO Rule
		rules.add(new TEMAORule());
		// LinearRegression AO -> PO Rule
		rules.add(new TLinearRegressionAORule());
		// LinearRegressionMerge AO -> PO Rule
		rules.add(new TLinearRegressionMergeAORule());
		// Equi Join
		// Join AO -> PO Rule
		rules.add(new TContinuousEquiJoinAORule());
		// Set Join PO Sweep Areas Rule
		rules.add(new TContinuousEquiJoinAOSetSARule());
		// Set Join PO Data Merge Rule
		rules.add(new TContinuousEquiJoinAOSetDMRule());
		return rules;
	}
}
