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
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.CSVTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.LineTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.LineFileInputHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@Deprecated
public class TFileAccessAORule extends AbstractTransformationRule<FileAccessAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(FileAccessAO fileAccessAO,
			TransformationConfiguration transformConfig) {
		String fileAccessPOName = fileAccessAO.getSourcename();
		LineFileInputHandler input = new LineFileInputHandler(fileAccessAO.getPath());
		LineTransformer transform = null;
		// TODO: auslagern !
		if ("csv".equalsIgnoreCase(fileAccessAO.getFileType())){
			transform = new CSVTransformer(fileAccessAO.getSeparator());
		}else{
			transform = new LineTransformer();
		}
		
		IDataHandler dataHandler = DataHandlerRegistry.getDataHandler(fileAccessAO.getDataHandler(), fileAccessAO.getOutputSchema());
		
		ISource<?> fileAccessPO = new AccessPO<String,Tuple<?>>(input, transform, 
				dataHandler);

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
