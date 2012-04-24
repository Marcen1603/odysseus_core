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
package de.uniol.inf.is.odysseus.core.server.util;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

/**
 * This visitor finds all roots of a physical plan
 * that have no owner. These are the roots of the
 * current query. If they have already an owner,
 * then they belong to another query, so that
 * they will not be returned.
 * @author André Bolles
 *
 */
public class FindQueryRootsVisitor<P extends IPhysicalOperator> implements IGraphNodeVisitor<P, ArrayList<P>>{

	ArrayList<P> foundRoots;
	
	public FindQueryRootsVisitor(){
		this.foundRoots = new ArrayList<P>();
	}
	
	@Override
	public void afterFromSinkToSourceAction(P sink, P source) {
	}

	@Override
	public void afterFromSourceToSinkAction(P source, P sink) {
	}

	@Override
	public void beforeFromSinkToSourceAction(P sink, P source) {
		
	}

	@Override
	public void beforeFromSourceToSinkAction(P source, P sink) {
	}

	@Override
	public ArrayList<P> getResult() {
		// TODO Auto-generated method stub
		return this.foundRoots;
	}

	@Override
	public void nodeAction(P node) {
		// if the sink is only a sink or is a pipe and has no following
		// operators than it is a root. If this root has no owner,
		// it belongs to the current query and is not used by other
		// queries through plan sharing.
		if((node.isSink() && !node.isSource() || (node.isSource() && ((ISource<?>)node).getSubscriptions().isEmpty())) &&  !node.hasOwner()){
			this.foundRoots.add(node);
		}
		
	}

}
