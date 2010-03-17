package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.GroupingHelper;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class RelationalTupleGroupingHelper<T extends IMetaAttribute> extends
		GroupingHelper<RelationalTuple<T>> {

	Map<RelationalTuple<T>, Integer> keyMap = null;
	Map<Integer, RelationalTuple<T>> tupleMap = null;
	int maxId = 0;
	int[] gRestrict = null;
	private List<SDFAttribute> grAttribs;
	private Map<FESortedPair<SDFAttribute, AggregateFunction>, Integer> aggrOutputPos = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Integer>();
	private Map<SDFAttribute, Integer> groupOutputPos = new HashMap<SDFAttribute, Integer>();
	final private SDFAttributeList inputSchema;
	final private SDFAttributeList outputSchema;
	final private Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations;
	// Da Initializer, Evaluator und Merger auf der selben Klasse basieren,
	// reicht hier eine Map
	//TODO lieber eine factory uebergeben, die e.g. immer die selben instanzen liefert, dann kann auf einiges
	//gecaste verzichtet werden, weil die typinformationen vorhanden sind
	static private Map<FESortedPair<SDFAttribute, AggregateFunction>, IEvaluator<RelationalTuple<?>>> fMap = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, IEvaluator<RelationalTuple<?>>>();

	public RelationalTupleGroupingHelper(SDFAttributeList inputSchema, SDFAttributeList outputSchema, List<SDFAttribute> groupingAttributes, Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations) {
		super();
		this.grAttribs = groupingAttributes;
		this.inputSchema = inputSchema;
		this.outputSchema = outputSchema;
		this.aggregations = aggregations;
	}

	@Override
	public int getGroupID(RelationalTuple<T> elem) {
		// Wenn es keine Gruppierungen gibt, ist der Schl�ssel immer gleich 0
		if (gRestrict == null || gRestrict.length == 0)
			return 0;
		// Ansonsten das Tupel auf die Gruppierungsattribute einschr�nken
		RelationalTuple<T> gTuple = elem.restrict(gRestrict, true);
		// Gibt es diese Kombination schon?
		Integer id = keyMap.get(gTuple);
		// Wenn nicht, neu eintragen
		if (id == null) {
			id = ++maxId;
			keyMap.put(gTuple, id);
			tupleMap.put(id, gTuple);
		}
		return id;
	}

	@Override
	public void init() {
		if (grAttribs != null && grAttribs.size() > 0) {
			gRestrict = new int[grAttribs.size()];
			for (int i = 0; i < grAttribs.size(); i++) {
				gRestrict[i] = inputSchema.indexOf(
						grAttribs.get(i));
			}
		}
		maxId = 0;
		keyMap = new HashMap<RelationalTuple<T>, Integer>();
		tupleMap = new HashMap<Integer, RelationalTuple<T>>();
	}

	private int getOutputPos(FESortedPair<SDFAttribute, AggregateFunction> p) {
		Integer pos = aggrOutputPos.get(p);
		if (pos == null) {
			Map<AggregateFunction, SDFAttribute> funcs = aggregations.get(p.getE1());
			SDFAttribute outAttr = funcs.get(p.getE2());
			pos = outputSchema.indexOf(outAttr);
			aggrOutputPos.put(p, pos);
		}
		return pos;
	}

	private int getOutputPos(SDFAttribute attribute) {
		Integer pos = groupOutputPos.get(attribute);
		if (pos == null) {
			pos = outputSchema.indexOf(attribute);
			groupOutputPos.put(attribute, pos);
		}
		return pos;
	}

	@Override
	public RelationalTuple<T> createOutputElement(Integer groupID,
			PairMap<SDFAttribute, AggregateFunction, RelationalTuple<T>, ?> r) {
		// ANDRE: do not use schema in a relational tuple
//		RelationalTuple<T> returnTuple = new RelationalTuple<T>(outputSchema);
		RelationalTuple<T> returnTuple = new RelationalTuple<T>(outputSchema.size()); 
		
		// in r stecken alle Aggregate drin
		// notwendig: Finde die Ziel-Position in dem returnTuple
		// ermittelt sich aus dem Attribute und der Aggregatfunktio
		for (Entry<FESortedPair<SDFAttribute, AggregateFunction>, RelationalTuple<T>> e : r
				.entrySet()) {
			int pos = getOutputPos(e.getKey());
			returnTuple.setAttribute(pos, e.getValue().getAttribute(0));
		}

		// Jetzt die Gruppierungsattribute
		RelationalTuple<T> gruppAttr = tupleMap.get(groupID);
		int groupTupPos = 0;
		for (SDFAttribute ga : grAttribs) {
			int pos = getOutputPos(ga);
			returnTuple
					.setAttribute(pos, gruppAttr.getAttribute(groupTupPos++));

		}
		return returnTuple;
	}

	private IEvaluator<RelationalTuple<?>> createAggFunction(AggregateFunction key,
			int pos) {
		IEvaluator<RelationalTuple<?>> aggFunc = null;
		switch (key) {
		case AVG:
			aggFunc = new RelationalAvgSum(pos, true);
			break;
		case COUNT:
			aggFunc = new RelationalCount();
			break;
		case MAX:
			aggFunc = new RelationalMinMax(pos, true);
			break;
		case MIN:
			aggFunc = new RelationalMinMax(pos, false);
			break;
		case SUM:
			aggFunc = new RelationalAvgSum(pos, false);
			break;
		default:
			throw new IllegalArgumentException("No such Aggregationfunction");
		}
		return aggFunc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IEvaluator<RelationalTuple<T>> getEvaluatorAggFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p) {
		IEvaluator<RelationalTuple<?>> eval = fMap.get(p);
		if (eval == null) {
			eval = createAggFunction(p.getE2(), inputSchema.indexOf(p.getE1()));
			fMap.put(p, eval);
		}
		return (IEvaluator<RelationalTuple<T>>) (IEvaluator) eval;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IInitializer<RelationalTuple<T>> getInitAggFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p) {
		// Zur Zeit keine unterschiedlichen Aggregationsfunktionen
		return (IInitializer<RelationalTuple<T>>) getEvaluatorAggFunction(p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IMerger<RelationalTuple<T>> getMergerAggFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p) {
		return (IMerger<RelationalTuple<T>>) (IMerger) getEvaluatorAggFunction(p);
	}
}
