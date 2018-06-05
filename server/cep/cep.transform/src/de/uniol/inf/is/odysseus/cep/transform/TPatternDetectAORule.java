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
package de.uniol.inf.is.odysseus.cep.transform;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.cep.PatternDetectAO;
import de.uniol.inf.is.odysseus.cep.epa.PatternDetectPO;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.IComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.relational.RelationalCreator;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.IEventReader;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.relational.RelationalReader;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TPatternDetectAORule extends AbstractTransformationRule<PatternDetectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(PatternDetectAO cepAO,
			TransformationConfiguration transformConfig) throws RuleException {

		Map<Integer, IEventReader> rMap = new HashMap<>();

		StateMachine m = cepAO.getStateMachine();

		for (LogicalSubscription s : cepAO.getSubscribedToSource()) {
			String name = cepAO.getInputTypeName(s.getSinkInPort());
			if (s.getSchema().getType() == Tuple.class) {
				rMap.put(s.getSinkInPort(), new RelationalReader(s.getSchema(),
						name));
			} else {
				throw new TransformationException("Input "
						+ s.getSchema().getType()
						+ " is not allowed for Pattern Detect!");
			}
		}
		IComplexEventFactory complexEventFactory = new RelationalCreator();
		PatternDetectPO cepPO = null;
		boolean onlyOneMatchPerInstance = cepAO.isOneMatchPerInstance();
		try {
			cepPO = new PatternDetectPO(m, cepAO.getSecondStateMachine(), rMap,
					complexEventFactory, false, new TIInputStreamSyncArea(),
					new TITransferArea(), onlyOneMatchPerInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cepPO != null) {
			if (cepAO.getHeartbeatRate() > 0) {
				cepPO.setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(cepAO.getHeartbeatRate()));
			}

			defaultExecute(cepAO, cepPO, transformConfig, true, true);
		}
	}

	@Override
	public boolean isExecutable(PatternDetectAO operator,
			TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "CepAO --> CepOperator (Relational)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
