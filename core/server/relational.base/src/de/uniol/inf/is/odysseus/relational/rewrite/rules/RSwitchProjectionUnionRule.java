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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;

public class RSwitchProjectionUnionRule extends
		AbstractSwitchProjectionRule<UnionAO> {

	@Override
	public void execute(UnionAO union, RewriteConfiguration config) {
		ProjectAO proj = (ProjectAO) getSubscribingOperatorAndCheckType(union,
				ProjectAO.class);
		if (proj != null) {
			Collection<ILogicalOperator> toInsert = new ArrayList<ILogicalOperator>();
			Collection<ILogicalOperator> toRemove = new ArrayList<ILogicalOperator>();
			Collection<ILogicalOperator> toUpdate = switchOperator(proj, union, toInsert);
			for (ILogicalOperator o : toInsert) {
				insert(o);
			}
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}
			for (ILogicalOperator o : toRemove) {
				retract(o);
			}
			update(union);
		}

	}

	@Override
	public boolean isExecutable(UnionAO union, RewriteConfiguration config) {
		return getSubscribingOperatorAndCheckType(union, ProjectAO.class) != null;
	}

	@Override
	public Class<? super UnionAO> getConditionClass() {
		return UnionAO.class;
	}
}