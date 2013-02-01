/********************************************************************************** 
 * Copyright 2013 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.EnrichPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 * 
 */
public class TEnrichAddDataMergeFunctionRule extends AbstractTransformationRule<EnrichPO<Tuple<IMetaAttribute>, IMetaAttribute>> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getName() {
		return "Insert DataMerge Function Enrich (Relational)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super EnrichPO<?, ?>> getConditionClass() {
		return EnrichPO.class;
	}

	@Override
	public void execute(EnrichPO<Tuple<IMetaAttribute>, IMetaAttribute> operator, TransformationConfiguration config) {
		IDataMergeFunction<Tuple<IMetaAttribute>, IMetaAttribute> dmf = new RelationalMergeFunction<IMetaAttribute>(operator.getOutputSchema().size());
		operator.setDataMergeFunction(dmf);
		update(operator);

	}

	@Override
	public boolean isExecutable(EnrichPO<Tuple<IMetaAttribute>, IMetaAttribute> operator, TransformationConfiguration config) {

		if (config.getDataTypes().contains("relational")) {
			if (operator.getDataMergeFunction() == null) {
				return true;
			}
		}
		return false;
	}

}
