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
package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.object.predicates;

import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.TimeInterval;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.predicate.NodeListEqualPredicate;

public class NodeListEqualTIPredicate extends AbstractPredicate<NodeList<ITimeInterval>>{

	private static final NodeListEqualTIPredicate instance = new NodeListEqualTIPredicate();
	
	public boolean evaluate(NodeList<ITimeInterval> elem){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(NodeList<ITimeInterval> left, NodeList<ITimeInterval> right){
		return TimeInterval.overlaps(left.getMetadata(), right.getMetadata()) ? NodeListEqualPredicate.getInstance().evaluate(left, right) : false;
	}
	
	public NodeListEqualTIPredicate clone(){
		return instance;
	}
	
	public static NodeListEqualTIPredicate getInstance(){
		return instance;
	}
}
