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
package de.uniol.inf.is.odysseus.mining.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.logicaloperator.ClassificationAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.ClassificationPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 * 
 */
public class TClassificationAORule extends AbstractTransformationRule<ClassificationAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ClassificationAO operator, TransformationConfiguration config) throws RuleException {
		CombinedMergeFunction<ITimeInterval> metaDataMerge = new CombinedMergeFunction<ITimeInterval>();
		metaDataMerge.add(new TimeIntervalInlineMetadataMergeFunction());
		TITransferArea<Tuple<ITimeInterval>, Tuple<ITimeInterval>> transferFunction = new TITransferArea<>();
		int portofclassifier = 0;
		int positionofclassifier = -1;
		positionofclassifier = operator.getInputSchema(0).indexOf(operator.getClassifier());
		if(portofclassifier==-1){
			portofclassifier = 1;
			positionofclassifier = operator.getInputSchema(1).indexOf(operator.getClassifier());
		}
		if(portofclassifier==-1){
			portofclassifier = -1;
			throw new IllegalArgumentException("the classifier attribute must be either one of port 0 or port 1!");
		}
		ClassificationPO<ITimeInterval> po = new ClassificationPO<ITimeInterval>(positionofclassifier, portofclassifier, metaDataMerge, transferFunction);
		po.setOneClassifier(operator.isOneClassifier());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(ClassificationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ClassificationAO -> ClassificationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}