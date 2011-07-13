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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.LoggerFactory;

public class RestructHelper {
	public static Collection<ILogicalOperator> removeOperator(
			UnaryLogicalOp remove, boolean reserveOutputSchema) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> fathers = remove.getSubscriptions();
		LogicalSubscription child = remove.getSubscribedToSource(0);
		// remove Connection between child and op
		remove.unsubscribeFromSource(child);
		// Subscribe Child to every father of op
		for (LogicalSubscription father : fathers) {
			remove.unsubscribeSink(father);
			child.getTarget().subscribeSink(
					father.getTarget(),
					father.getSinkInPort(),
					child.getSourceOutPort(),
					reserveOutputSchema ? remove.getOutputSchema() : child
							.getTarget().getOutputSchema());
			ret.add(father.getTarget());
		}
		// prevents duplicate entry if child.getTarget=father.getTarget
		if (!ret.contains(child.getTarget())) {
			ret.add(child.getTarget());
		}
//		for (LogicalSubscription a : child.getTarget().getSubscriptions()) {
//			LoggerFactory.getLogger(RestructHelper.class).debug(
//					"New subplan after remove: " + a.getTarget());
//		}
		return ret;
	}

	/**
	 * Insert an operator in the tree at some special point and update all
	 * subscriptions i.e. the new Operator gets all subscriptions currently
	 * bound to the after operator (looking from root!) and create a new
	 * subscription from toInsert to after
	 * 
	 * @param toInsert
	 *            Operator that should be inserted as child of the after
	 *            operator
	 * @param after
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperator(
			ILogicalOperator toInsert, ILogicalOperator after, int sinkInPort,
			int toInsertsinkInPort, int toInsertsourceOutPort) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		LogicalSubscription source = after.getSubscribedToSource(sinkInPort);
		ret.add(source.getTarget());
		after.unsubscribeFromSource(source);
		source.getTarget()
				.subscribeSink(toInsert, toInsertsinkInPort,
						source.getSourceOutPort(),
						source.getTarget().getOutputSchema());
		toInsert.subscribeSink(after, sinkInPort, toInsertsourceOutPort,
				toInsert.getOutputSchema());
		ret.add(after);
		return ret;
	}

	public static Collection<ILogicalOperator> simpleOperatorSwitch(
			UnaryLogicalOp father, UnaryLogicalOp son) {
		son.unsubscribeSink(son.getSubscription());

		LogicalSubscription toDown = son.getSubscribedToSource(0);
		son.unsubscribeFromSource(toDown);

		LogicalSubscription toUp = father.getSubscription();
		father.unsubscribeSink(toUp);

		father.subscribeToSource(toDown.getTarget(), 0,
				toDown.getSourceOutPort(), toDown.getSchema());
		father.subscribeSink(son, 0, 0, father.getOutputSchema());

		son.subscribeSink(toUp.getTarget(), toUp.getSinkInPort(), 0,
				son.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = new ArrayList<ILogicalOperator>(
				2);
		toUpdate.add(toDown.getTarget());
		toUpdate.add(toUp.getTarget());
		return toUpdate;
	}

}
