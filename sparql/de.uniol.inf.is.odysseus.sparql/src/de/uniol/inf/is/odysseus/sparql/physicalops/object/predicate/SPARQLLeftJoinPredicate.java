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
//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.object.predicate;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.join.object.merge.LeftMergeFunction;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.AndPredicate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.interval.join.object.NodeListLeftMergeFunction;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//
//public class SPARQLLeftJoinPredicate<M extends IClone> extends AndPredicate<NodeList<M>> {
//	
//	LeftMergeFunction<NodeList<M>> dataMerge;
//	
//	public SPARQLLeftJoinPredicate(NodeListCompatiblePredicate<M> compatible, SPARQLFilterPredicate<M> filter, NodeListLeftMergeFunction<M> dataMerge){
//		super(compatible, filter);
//		this.dataMerge = dataMerge;
//	}
//	
//	public boolean evaluate(NodeList<M> left, NodeList<M> right) {
//		return getLeft().evaluate(left, right) && 
//				this.getRight().evaluate(this.dataMerge.merge(left, right));
//	}
//}
