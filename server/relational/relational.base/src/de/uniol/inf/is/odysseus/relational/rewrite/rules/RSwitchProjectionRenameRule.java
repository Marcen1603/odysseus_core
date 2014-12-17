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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;

public class RSwitchProjectionRenameRule extends AbstractSwitchProjectionRule<RenameAO> {

	@Override
	public void execute(RenameAO rename, RewriteConfiguration config) {
		for (ProjectAO proj : getAllOfSameTyp(new ProjectAO())) {
			if (isValidProject(proj, rename)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper
						.switchOperator(proj, rename);
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				update(proj);
				update(rename);
			}
		}

	}

	@Override
	public boolean isExecutable(RenameAO ren, RewriteConfiguration config) {
		if (ren.getSubscriptions().size() > 1) {
			return false;
		}

		for (ProjectAO proj : getAllOfSameTyp(new ProjectAO())) {
			if (isValidProject(proj, ren)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<? super RenameAO> getConditionClass() {
		return RenameAO.class;
	}

}
