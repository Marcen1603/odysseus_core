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
package de.uniol.inf.is.odysseus.cep.transform;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.cep.CepAO;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.IComplexEventFactory;
import de.uniol.inf.is.odysseus.cep.epa.eventgeneration.relational.RelationalCreator;
import de.uniol.inf.is.odysseus.cep.epa.eventreading.relational.RelationalReader;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TCep extends AbstractTransformationRule<CepAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(CepAO cepAO,
			TransformationConfiguration transformConfig) {
		Map<Integer, RelationalReader> rMap = new HashMap<Integer, RelationalReader>();
		
		StateMachine m = cepAO.getStateMachine();
		Set<String> types = m.getStateTypeSet();
		
		for (LogicalSubscription s : cepAO.getSubscribedToSource()) {
			String name = cepAO.getInputTypeName(s.getSinkInPort());
			if (name == null){
				SDFSchema schema = s.getSchema();
				name = schema.getURI();
				if (!types.contains(name)){
					throw new IllegalArgumentException("Type "+name+" no input for Operator");
				}
			}
			rMap.put(s.getSinkInPort(), new RelationalReader(s.getSchema(),name));
		}
		IComplexEventFactory complexEventFactory = new RelationalCreator();
		CepOperator cepPO = null;
		try {
			cepPO = new CepOperator(m, cepAO.getSecondStateMachine(), rMap,
					complexEventFactory, false, new TIInputStreamSyncArea(cepAO
							.getSubscribedToSource().size()),
					new TITransferArea(cepAO.getSubscribedToSource().size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cepPO.setOutputSchema(cepAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig
				.getTransformationHelper().replace(cepAO, cepPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(cepAO);
	}

	@Override
	public boolean isExecutable(CepAO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("relational")) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
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

