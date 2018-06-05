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
package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
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
public class FindSinksLogicalVisitor<P extends ILogicalOperator> implements IGraphNodeVisitor<P, List<P>>{

	ArrayList<P> foundRoots;
	
	public FindSinksLogicalVisitor(){
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
	public List<P> getResult() {
		return this.foundRoots;
	}

	@Override
	public void nodeAction(P node) {	
		if(node.getSubscriptions().isEmpty()){
			this.foundRoots.add(node);
		}		
	}

}
