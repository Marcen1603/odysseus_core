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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.DifferenceTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TDifferenceAORule extends AbstractIntervalTransformationRule<DifferenceAO> {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DifferenceAO differenceAO, TransformationConfiguration transformConfig) throws RuleException {
		
		ITimeIntervalSweepArea<IStreamObject<ITimeInterval>> left;
		ITimeIntervalSweepArea<IStreamObject<ITimeInterval>> right;
		try {
			left = (ITimeIntervalSweepArea<IStreamObject<ITimeInterval>>) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME, null);
			right = (ITimeIntervalSweepArea<IStreamObject<ITimeInterval>>) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME, null);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}
		ITransferArea<IStreamObject<ITimeInterval>,IStreamObject<ITimeInterval>> transferArea = new TITransferArea<>();
		//AntiJoinTIPO<ITimeInterval, IStreamObject<ITimeInterval>> po = new AntiJoinTIPO<ITimeInterval, IStreamObject<ITimeInterval>>(differenceAO, left, right, transferArea);
		DifferenceTIPO<ITimeInterval, IStreamObject<ITimeInterval>> po = new DifferenceTIPO<ITimeInterval, IStreamObject<ITimeInterval>>(differenceAO, left, right, transferArea);
		defaultExecute(differenceAO, po, transformConfig, true, true);		
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super DifferenceAO> getConditionClass() {	
		return DifferenceAO.class;
	}
}
