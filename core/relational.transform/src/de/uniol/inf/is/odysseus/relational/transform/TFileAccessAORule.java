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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.FileAccessPO;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.relational.base.TupleDataHandler;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFileAccessAORule extends AbstractTransformationRule<FileAccessAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(FileAccessAO fileAccessAO,
			TransformationConfiguration transformConfig) {
		String fileAccessPOName = fileAccessAO.getSourcename();
		ISource<?> fileAccessPO = new FileAccessPO<Tuple<?>>(
				fileAccessAO.getPath(), fileAccessAO.getFileType(), fileAccessAO.getSeparator(), new TupleDataHandler(
						fileAccessAO.getOutputSchema()));

		fileAccessPO.setOutputSchema(fileAccessAO.getOutputSchema());
		getDataDictionary().putAccessPlan(fileAccessPOName, fileAccessPO);
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
		if (getDataDictionary().getAccessPlan(fileAccessAO.getSourcename()) == null) {
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
