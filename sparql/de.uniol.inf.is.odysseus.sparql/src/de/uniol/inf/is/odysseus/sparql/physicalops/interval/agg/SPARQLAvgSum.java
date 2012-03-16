//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.agg;
//
//import com.hp.hpl.jena.graph.Node;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.basefunctions.Evaluator;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.basefunctions.Initializer;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.basefunctions.Merger;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.basefunctions.PartialAggregate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.agg.functions.AvgSumPartialAggregate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//
//public class SPARQLAvgSum implements Evaluator<NodeList>, Initializer<NodeList>, Merger<NodeList>{
//
//	private int pos;
//	//private SDFSchema schema;
//	boolean isAvg;
//	
//	public SPARQLAvgSum(int pos, boolean isAvg){
//		this.pos = pos;
//		//this.schema = new SDFSchema();
//		this.isAvg = isAvg;
////		SDFAttribute attr = null;
////		if (isAvg){
////			attr = new SDFAttribute("AVG("+a.getURI(false)+")");
////		}else{
////			attr = new SDFAttribute("SUM("+a.getURI(false)+")");
////		}
////		attr.setDatatype(SDFDatatypeFactory.getDatatype(SDFDatatypes.Number));
////		this.schema.add(attr);		
//	}
//	
//	public PartialAggregate<NodeList> init(NodeList in) {
//		AvgSumPartialAggregate<NodeList> pa = new AvgSumPartialAggregate<NodeList>(Double.parseDouble((String)in.get(pos).getLiteralValue()),1);
//		return pa;
//	}
//
//	public PartialAggregate<NodeList> merge(PartialAggregate<NodeList> p, NodeList toMerge, boolean createNew) {
//		AvgSumPartialAggregate<NodeList> pa = null;
//		if (createNew){
//			AvgSumPartialAggregate<NodeList> h = (AvgSumPartialAggregate<NodeList>) p;			
//			pa = new AvgSumPartialAggregate<NodeList>(h.getAggValue(), h.getCount());
//			
//		}else{
//			pa = (AvgSumPartialAggregate<NodeList>) p;
//		}
//		Double newAggValue = pa.getAggValue().doubleValue() + Double.parseDouble((String)toMerge.get(pos).getLiteralValue()); 
//		pa.setAggValue(newAggValue, pa.getCount()+1);
//		return pa;
//	}
//	
//	public NodeList evaluate(PartialAggregate p) {
//		AvgSumPartialAggregate pa = (AvgSumPartialAggregate) p;
//		//Tuple r = new RelationalTuple(schema);
//		NodeList r = new NodeList();
//		if (isAvg){
//			Node n = Node.createLiteral("" + pa.getAggValue().doubleValue()/pa.getCount());
//			
//			r.add(n);
//		}else{
//			Node n = Node.createLiteral("" + pa.getAggValue().doubleValue());
//			r.add(n);
//		}
//		return r;
//	}
//
//
//}
