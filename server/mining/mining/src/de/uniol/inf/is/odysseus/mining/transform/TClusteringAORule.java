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

package de.uniol.inf.is.odysseus.mining.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatencyTimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;
import de.uniol.inf.is.odysseus.mining.clustering.WekaClusterer;
import de.uniol.inf.is.odysseus.mining.logicaloperator.ClusteringAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.ClusteringKMeansPO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.ClusteringPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen Created at: 14.05.2012
 */
public class TClusteringAORule extends AbstractTransformationRule<ClusteringAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(ClusteringAO operator, TransformationConfiguration config) throws RuleException {
		AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> po;
		String algorithm = operator.getClustererName().toUpperCase();

		switch (algorithm) {
		case "KMEANS":
			int k = Integer.parseInt(operator.getOptions().get("k"));
			po = new ClusteringKMeansPO<>(k, operator.getInputSchema(0), operator.getAttributePositions());
			break;
		case "WEKA":			
			IClusterer<ILatencyTimeInterval> clusterer = new WekaClusterer();
			clusterer.setOptions(operator.getOptions());
			clusterer.init(operator.getInputSchema(0));
			po = new ClusteringPO(clusterer);
			break;
		default:
			throw new IllegalArgumentException("Unknwon clustering algorithm");			
		}

		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(ClusteringAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ClusteringAO -> ClusteringPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
