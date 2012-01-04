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
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.FileAccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.relational.FileAccessPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFileAccessAORule extends AbstractTransformationRule<FileAccessAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(FileAccessAO fileAccessAO,
			TransformationConfiguration transformConfig) {
		String fileAccessPOName = fileAccessAO.getSource().getURI(false);
		ISource<?> fileAccessPO = new FileAccessPO<RelationalTuple<?>>(
				fileAccessAO.getPath(), fileAccessAO.getFileType(), fileAccessAO.getSeparator());

		fileAccessPO.setOutputSchema(fileAccessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(fileAccessPOName, fileAccessPO);
		Collection<ILogicalOperator> toUpdate = transformConfig
				.getTransformationHelper().replace(fileAccessAO, fileAccessPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		retract(fileAccessAO);
		insert(fileAccessPO);

	}

	@Override
	public boolean isExecutable(FileAccessAO fileAccessAO,
			TransformationConfiguration trafo) {
		if (WrapperPlanFactory.getAccessPlan(fileAccessAO.getSource().getURI()) == null) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "FileAccessAO -> FileAccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<?> getConditionClass() {
		return FileAccessAO.class;
	}

}
