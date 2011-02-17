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
package de.uniol.inf.is.odysseus.p2p.splitting.nosplitting;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
import de.uniol.inf.is.odysseus.p2p.splitting.base.SplittingExecutionHandler;

public class NoSplitting extends
		AbstractSplittingStrategy {

	@Override
	public ArrayList<ILogicalOperator> splitPlan(ILogicalOperator plan) {
		ArrayList<ILogicalOperator> splitList = new ArrayList<ILogicalOperator>();
		P2PSinkAO p2psink = new P2PSinkAO(AdvertisementTools.createSocketAdvertisement().toString());
		plan.subscribeSink(p2psink, 0, 0, plan.getOutputSchema());
		splitList.add(p2psink);

		return splitList;
	}

	@Override
	public String getName() {
		return "NoSplitting";
	}

	@Override
	public void initializeService() {
		IExecutionHandler<NoSplitting> handler = new SplittingExecutionHandler<NoSplitting>();
		handler.setPeer(getPeer());
		handler.setFunction(this);
		getPeer().bindExecutionHandler(handler);
	}

}
