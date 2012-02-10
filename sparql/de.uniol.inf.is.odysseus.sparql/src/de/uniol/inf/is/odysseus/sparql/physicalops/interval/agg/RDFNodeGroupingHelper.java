//package de.uniol.inf.is.odysseus.sparql.physicalops.interval.agg;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import de.uniol.inf.is.odysseus.queryexecution.po.base.agg.GroupingHelper;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.agg.basefunctions.Evaluator;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.agg.basefunctions.Initializer;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.agg.basefunctions.Merger;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.container.FESortedPair;
//import de.uniol.inf.is.odysseus.queryexecution.po.base.object.container.PairMap;
//import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.AggregateAO;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.AggregateFunction;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
//
//public class RDFNodeGroupingHelper extends GroupingHelper<NodeList> {
//
//	Map<NodeList, Integer> keyMap = null;
//	Map<Integer, NodeList> tupleMap = null;
//	int maxId = 0;
//	int[] gRestrict = null;
//	private List<SDFAttribute> grAttribs;
//	private Map<FESortedPair<SDFAttribute, AggregateFunction>, Integer> aggrOutputPos = new HashMap<FESortedPair<SDFAttribute,AggregateFunction>, Integer>();
//	private Map<SDFAttribute, Integer> groupOutputPos = new HashMap<SDFAttribute, Integer>();
//	// Da Initializer, Evaluator und Merger auf der selben Klasse basieren, reicht hier eine Map
//	static private Map<FESortedPair<SDFAttribute, AggregateFunction>, Evaluator<NodeList>> fMap = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Evaluator<NodeList>>();
//	
//	public RDFNodeGroupingHelper(AggregateAO aggregateAO) {
//		super(aggregateAO);
//	}
//
//	@Override
//	public int getGroupID(NodeList elem) {
//		// Wenn es keine Gruppierungen gibt, ist der Schluessel immer gleich 0
//		if (gRestrict == null || gRestrict.length == 0) return 0;
//		// Ansonsten das Tupel auf die Gruppierungsattribute einschraenken
//		NodeList gTuple = elem.restrict(gRestrict, null);
//		// Gibt es diese Kombination schon?
//		Integer id = keyMap.get(gTuple);
//		// Wenn nicht, neu eintragen
//		if (id == null){
//			id = ++maxId;
//			keyMap.put(gTuple, id);	
//			tupleMap.put(id,gTuple);
//		}		
//		return id;
//	}
//	
//	@Override
//	public void init() {
//		grAttribs = getAggregateAO().getGroupingAttributes();
//		if (grAttribs != null && grAttribs.size() > 0){
//			gRestrict = new int[grAttribs.size()];
//			for (int i=0;i<grAttribs.size();i++){
//				System.out.println("grAttribs.get(i): " + grAttribs.get(i));
//				gRestrict[i] = getAggregateAO().getInputSchema().indexOf(grAttribs.get(i));
//			}
//		}
//		maxId = 0;
//		keyMap = new HashMap<NodeList, Integer>();
//		tupleMap = new HashMap<Integer, NodeList>();
//	}
//
//	private int getOutputPos(FESortedPair<SDFAttribute, AggregateFunction> p){
//		Integer pos = aggrOutputPos.get(p); 
//		if (pos == null){
//			SDFSchema outputSchema = getAggregateAO().getOutputSchema();
//			Map<AggregateFunction, SDFAttribute> funcs = getAggregateAO().getAggregationFunctions(p.getE1());
//			SDFAttribute outAttr = funcs.get(p.getE2());
//			pos = outputSchema.indexOf(outAttr);
//			aggrOutputPos.put(p,pos);
//		}
//		return pos;		
//	}
//	
//	private int getOutputPos(SDFAttribute attribute){
//		Integer pos = groupOutputPos.get(attribute);
//		if (pos == null){
//			pos = getAggregateAO().getOutputSchema().indexOf(attribute);
//			groupOutputPos.put(attribute, pos);
//		}
//		return pos;
//	}
//	
//	@Override
//	public NodeList createOutputElement(Integer groupID, PairMap<SDFAttribute, AggregateFunction, NodeList, ?> r) {
//		// TODO sollte eine ComparableList<ComparableNode> werden
//		NodeList returnTuple = new NodeList(getAggregateAO().getOutputSchema().size());
//		// die liste muss mit null gefuellt werden, da sonst die Size nicht erhoeht wird und damit
//		// die set methode nicht funktioniert
//		for(int i = 0; i<getAggregateAO().getOutputSchema().size(); i++){
//			returnTuple.add(null);
//		}
//		
//		//RelationalTuple returnTuple = new RelationalTuple(aggregateAO.getOutputSchema());
//		
//		// in r stecken alle Aggregate drin
//		// notwendig: Finde die Ziel-Position in dem returnTuple
//		// ermittelt sich aus dem Attribute und der Aggregatfunktio
//		for(Entry<FESortedPair<SDFAttribute, AggregateFunction>, NodeList> e: r.entrySet()){
//			int pos = getOutputPos(e.getKey());
//			returnTuple.set(pos, e.getValue().get(0));
//		}
//		
//		// Jetzt die Gruppierungsattribute
//		NodeList gruppAttr = tupleMap.get(groupID);
//		int groupTupPos = 0;
//		for (SDFAttribute ga: getAggregateAO().getGroupingAttributes()){
//			int pos = getOutputPos(ga);
//			returnTuple.set(pos, gruppAttr.get(groupTupPos++));
//			
//		}
//		System.out.println("Aggregationstupel: " + returnTuple.toString());
//		return returnTuple;
//	}
//
//	private Evaluator<NodeList> createAggFunction(AggregateFunction key, int pos){
//		Evaluator<NodeList> aggFunc = null;
//		switch (key){
//		case AVG:		
//			aggFunc = new SPARQLAvgSum(pos, true);
//			break;
//		case COUNT:
//			aggFunc = new SPARQLCount(pos);
//			break;
//		case MAX:
//			aggFunc = new SPARQLMinMax(pos, true);
//			break;
//		case MIN:
//			aggFunc = new SPARQLMinMax(pos, false);
//			break;
//		case SUM:
//			aggFunc = new SPARQLAvgSum(pos, false);
//			break;
//		default:
//			throw new IllegalArgumentException("No such Aggregationfunction");
//		}
//		return aggFunc;
//	}
//	
//	@Override
//	public Evaluator<NodeList> getEvaluatorAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p) {
//		Evaluator<NodeList> eval = fMap.get(p);
//		if (eval == null){
//			eval = createAggFunction(p.getE2(), getAggregateAO().getInputSchema().indexOf(p.getE1()));
//			fMap.put(p, eval);			
//		}		
//		return (Evaluator<NodeList>)eval;
//	}
//
//	@Override
//	public Initializer<NodeList> getInitAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p) {
//		// Zur Zeit keine unterschiedlichen Aggregationsfunktionen
//		return (Initializer<NodeList>)getEvaluatorAggFunction(p);
//	}
//
//	@Override
//	public Merger<NodeList> getMergerAggFunction(FESortedPair<SDFAttribute, AggregateFunction> p) {
//		return (Merger<NodeList>)getEvaluatorAggFunction(p);
//	}
//
//}
