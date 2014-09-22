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
//import com.hp.hpl.jena.graph.Node;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.basefunctions.PartialAggregate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.functions.Count;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.functions.CountPartialAggregate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//
//public class SPARQLCount extends Count<NodeList> {
//
//	//private SDFSchema schema;
//	
//	public SPARQLCount(int pos){
////		this.schema = new SDFSchema();
////		SDFAttribute attr = new SDFAttribute("COUNT("+a.getURI(false)+")");
////		attr.setDatatype(SDFDatatypeFactory.getDatatype(SDFDatatypes.Number));
////		this.schema.add(attr);		
//	}
//	
//	@Override
//	public PartialAggregate<NodeList> init(NodeList in) {
//		// Der Wert interessiert bei Count nicht
//		return super.init(null);
//	}
//	
//	@Override
//	public PartialAggregate<NodeList> merge(PartialAggregate<NodeList> p, NodeList toMerge, boolean createNew) {
//		// Der Wert interessiert bei Count nicht
//		return super.merge(p, null, createNew);
//	}
//	
//	public NodeList evaluate(PartialAggregate p) {
//		CountPartialAggregate pa = (CountPartialAggregate) p;
//		NodeList r = new NodeList();
//		r.add(Node.createLiteral("" + pa.getCount()));
//		return r;
//	}
//
//
//}
