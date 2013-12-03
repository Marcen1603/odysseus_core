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
//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.agg;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.basefunctions.PartialAggregate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.functions.MinMax;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//
//public class SPARQLMinMax extends MinMax<NodeList> {
//
//	int[] attrList = new int[1];
////	private SDFSchema schema;
//
//	public SPARQLMinMax(int pos, boolean isMax) {
//		super(isMax);
//		attrList[0] = pos;
////		this.schema = new SDFSchema();
////		SDFAttribute attr = null;
////		if (isMax){
////			attr = new SDFAttribute("MAX("+a.getURI(false)+")");
////		}else{
////			attr = new SDFAttribute("MIN("+a.getURI(false)+")");
////		}
////		attr.setDatatype(SDFDatatypeFactory.getDatatype(SDFDatatypes.Number));
////		this.schema.add(attr);		
//	}
//	
//	@Override
//	public PartialAggregate<NodeList> init(NodeList in) {
//		NodeList in_new = new NodeList();
//		in_new.add(in.get(attrList[0]));
//		return super.init(in_new);
//	}
//	
//	@Override
//	public PartialAggregate<NodeList> merge(PartialAggregate<NodeList> p, NodeList toMerge, boolean createNew) {
//		NodeList toMerge_new = new NodeList();
//		toMerge_new.add(toMerge.get(attrList[0]));
//		return super.merge(p, toMerge_new, createNew);
//	}
//	
//	@Override
//	public NodeList evaluate(PartialAggregate<NodeList> p) {
//		return super.evaluate(p);
//	}
//
//
//}
