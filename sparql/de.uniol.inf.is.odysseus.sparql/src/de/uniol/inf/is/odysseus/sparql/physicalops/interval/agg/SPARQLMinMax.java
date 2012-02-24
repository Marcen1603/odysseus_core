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
