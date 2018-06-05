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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;

public class RSwitchSelectionWindowRule extends
		AbstractSwitchSelectionRule<AbstractWindowAO> {

	@Override
	public void execute(AbstractWindowAO win, RewriteConfiguration config) {
		SelectAO sel = (SelectAO) getSubscribingOperatorAndCheckType(win,
				SelectAO.class);
		if (sel != null) {
			Collection<ILogicalOperator> toUpdate = switchOperator(sel, win);
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}

			update(win);
			update(sel);
		}

	}

	@Override
	public boolean isExecutable(AbstractWindowAO win,
			RewriteConfiguration config) {
		return (SelectAO) getSubscribingOperatorAndCheckType(win,
				SelectAO.class) != null && checkWindowType(win);
	}

	private static boolean checkWindowType(AbstractWindowAO win) {
		return win.getWindowType() == WindowType.TIME;
	}

	@Override
	public Class<? super AbstractWindowAO> getConditionClass() {
		return AbstractWindowAO.class;
	}

}
