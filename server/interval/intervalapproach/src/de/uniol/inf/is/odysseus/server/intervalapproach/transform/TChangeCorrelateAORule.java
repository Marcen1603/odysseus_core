/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeCorrelateAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.ChangeCorrelatePO;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * 
 * @author Dennis Geesen
 * Created at: 29.05.2012
 */
public class TChangeCorrelateAORule extends AbstractIntervalTransformationRule<ChangeCorrelateAO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(ChangeCorrelateAO ccAO, TransformationConfiguration transformConfig) throws RuleException {
		TIInputStreamSyncArea inputStreamSyncArea = new TIInputStreamSyncArea(ccAO.getSubscribedToSource().size());
		TITransferArea outputTransferArea = new TITransferArea();
		
		ChangeCorrelatePO ccPO = new ChangeCorrelatePO(ccAO.getLeftHighPredicate(), ccAO.getLeftLowPredicate(), ccAO.getRightHighPredicate(), ccAO.getRightLowPredicate(), inputStreamSyncArea, outputTransferArea);
		
		ccPO.setMetadataMerge(new CombinedMergeFunction());
		ccPO.setOutputSchema(ccAO.getOutputSchema() == null?null:ccAO.getOutputSchema().clone());
		
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(ccAO, ccPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(ccPO);
		retract(ccAO);		
	}


	@Override
	public String getName() {
		return "ChangeCorrelateAO -> ChangeCorrelatePO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super ChangeCorrelateAO> getConditionClass() {	
		return ChangeCorrelateAO.class;
	}

}
