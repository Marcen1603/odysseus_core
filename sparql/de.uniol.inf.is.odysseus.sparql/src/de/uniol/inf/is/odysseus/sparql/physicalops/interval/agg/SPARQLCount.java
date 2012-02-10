//package de.uniol.inf.is.odysseus.sparql.physicalops.interval.agg;
//
//import com.hp.hpl.jena.graph.Node;
//
//import de.uniol.inf.is.odysseus.queryexecution.po.base.agg.basefunctions.PartialAggregate;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.agg.functions.Count;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.agg.functions.CountPartialAggregate;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
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
