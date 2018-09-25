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

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;

public class RSwitchSelectionJoinRule extends
		AbstractSwitchSelectionRule<JoinAO> {

	@Override
	public void execute(JoinAO join, RewriteConfiguration config) {
		SelectAO sel = (SelectAO) getSubscribingOperatorAndCheckType(join,
				SelectAO.class);
		if (sel != null) {
			Collection<ILogicalOperator> toInsert = new ArrayList<ILogicalOperator>();
			Collection<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
			Collection<ILogicalOperator> toUpdate = switchOperator(sel, join, toInsert, toRemove);
			for (ILogicalOperator o : toInsert) {
				insert(o);
			}
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}
			for (ILogicalOperator o : toRemove) {
				retract(o);
			}
			update(join);
		}

	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		SelectAO sel = (SelectAO) getSubscribingOperatorAndCheckType(join,
				SelectAO.class);
		return sel != null && canSwitch(sel, join);
	}

	public static boolean canSwitch(SelectAO sel, JoinAO join) {
		return subsetPredicate(sel.getPredicate(),
				join.getInputSchema(0))
				|| subsetPredicate(sel.getPredicate(),
						join.getInputSchema(1));
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}
}
