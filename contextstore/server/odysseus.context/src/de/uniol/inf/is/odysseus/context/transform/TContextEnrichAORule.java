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

package de.uniol.inf.is.odysseus.context.transform;

import de.uniol.inf.is.odysseus.context.logicaloperator.ContextEnrichAO;
import de.uniol.inf.is.odysseus.context.physicaloperator.ContextEnrichPO;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.04.2012
 */
public class TContextEnrichAORule extends AbstractTransformationRule<ContextEnrichAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(ContextEnrichAO operator, TransformationConfiguration config) throws RuleException {
		IContextStore<Tuple<ITimeInterval>> store = ContextStoreManager.getStore(operator.getStoreName());
		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMerge = new RelationalMergeFunction<ITimeInterval>(operator.getOutputSchema().size());		
		IMetadataMergeFunction metadataMerge = MetadataRegistry.getMergeFunction(operator.getInputSchema(0).getMetaAttributeNames());
		ContextEnrichPO<ITimeInterval> enrich = new ContextEnrichPO<ITimeInterval>(store, operator.isOuter(), dataMerge, metadataMerge, operator.getAttributes());		
		defaultExecute(operator, enrich, config, true, true);
	}

	@Override
	public boolean isExecutable(ContextEnrichAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "EnrichAO -> EnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super ContextEnrichAO> getConditionClass() {
		return ContextEnrichAO.class;	
	}


}
