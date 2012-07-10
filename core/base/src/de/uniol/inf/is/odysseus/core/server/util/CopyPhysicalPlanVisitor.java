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
package de.uniol.inf.is.odysseus.core.server.util;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;

/**
 * Copies every physical operator in a query plan and connects them with
 * subscriptions like in the given plan. Result is the root of the copied plan.
 * 
 * @author Tobias Witt
 * 
 */
public class CopyPhysicalPlanVisitor implements
		INodeVisitor<IPhysicalOperator, IPhysicalOperator> {

	private Stack<IPhysicalOperator> last;
	private Stack<IPhysicalOperator> lastOld;
	private boolean errorsOccured;
	private IPhysicalOperator root;

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(CopyPhysicalPlanVisitor.class);
		}
		return _logger;
	}

	public CopyPhysicalPlanVisitor() {
		this.last = new Stack<IPhysicalOperator>();
		this.lastOld = new Stack<IPhysicalOperator>();
		this.errorsOccured = false;
		this.root = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void ascendAction(IPhysicalOperator to) {
		ISource<?> source = (ISource<?>) this.last.pop();
		ISink sink = (ISink) this.last.peek();
		ISource<?> oldSource = (ISource<?>) this.lastOld.pop();
		ISink<?> oldSink = (ISink<?>) to;
		if (source == null || sink == null) {
			return;
		}
		PhysicalSubscription<?> sub = null;
		for (PhysicalSubscription<?> s : oldSink.getSubscribedToSource()) {
			if (s.getTarget() == oldSource) {
				sub = s;
				break;
			}
		}
		getLogger().debug(
				"subscribe " + sink.getName() + " to " + source.getName());
		if (sub != null) {
			source.subscribeSink(sink, sub.getSinkInPort(),
					sub.getSourceOutPort(), sub.getSchema());
		}
	}

	@Override
	public void descendAction(IPhysicalOperator to) {
	}

	@Override
	public IPhysicalOperator getResult() {
		return errorsOccured ? null : this.root;
	}

	@Override
	public void nodeAction(IPhysicalOperator op) {
		getLogger().debug("copy " + op.getName());
		this.last.push(op.clone());
		if (this.root == null) {
			this.root = this.last.peek();
		}
		this.lastOld.push(op);
	}

}
