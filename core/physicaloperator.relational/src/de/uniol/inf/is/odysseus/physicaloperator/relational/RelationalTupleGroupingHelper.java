/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.collection.FESortedPair;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.GroupingHelper;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class RelationalTupleGroupingHelper<T extends IMetaAttribute> extends
        GroupingHelper<RelationalTuple<T>, RelationalTuple<T>> {

    Map<RelationalTuple<T>, Integer> keyMap = null;
    Map<Integer, RelationalTuple<T>> tupleMap = null;
    int maxId = 0;
    int[] gRestrict = null;
    private final List<SDFAttribute> grAttribs;
    private final Map<FESortedPair<SDFAttributeList, AggregateFunction>, Integer> aggrOutputPos = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, Integer>();
    private final Map<SDFAttribute, Integer> groupOutputPos = new HashMap<SDFAttribute, Integer>();
    final private SDFAttributeList inputSchema;
    final private SDFAttributeList outputSchema;
    final private Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations;
    // Da Initializer, Evaluator und Merger auf der selben Klasse basieren,
    // reicht hier eine Map
    // TODO lieber eine factory uebergeben, die e.g. immer die selben instanzen
    // liefert, dann kann auf einiges
    // gecaste verzichtet werden, weil die typinformationen vorhanden sind
    static private Map<FESortedPair<SDFAttributeList, AggregateFunction>, IEvaluator<RelationalTuple<?>,RelationalTuple<?>>> fMap = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, IEvaluator<RelationalTuple<?>,RelationalTuple<?>>>();

    public RelationalTupleGroupingHelper(SDFAttributeList inputSchema,
            SDFAttributeList outputSchema,
            List<SDFAttribute> groupingAttributes,
            Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations) {
        super();
        this.grAttribs = groupingAttributes;
        this.inputSchema = inputSchema;
        this.outputSchema = outputSchema;
        this.aggregations = aggregations;
    }

    @Override
    public Integer getGroupID(RelationalTuple<T> elem) {
        // Wenn es keine Gruppierungen gibt, ist der Schl�ssel immer gleich 0
        if (gRestrict == null || gRestrict.length == 0)
            return Integer.valueOf(0);;
        // Ansonsten das Tupel auf die Gruppierungsattribute einschr�nken
        RelationalTuple<T> gTuple = elem.restrict(gRestrict, true);
        // Gibt es diese Kombination schon?
        Integer id = keyMap.get(gTuple);
        // Wenn nicht, neu eintragen
        if (id == null) {
        	id = ++maxId;
            keyMap.put(gTuple, id);
            tupleMap.put(id, gTuple);
      //  	System.out.println("Created new Group "+id+" from "+gTuple+" with input "+elem);      
        }
        /*else{
      		System.out.println("Using Group with ID "+id+" for input "+gTuple+" "+elem);

        }*/
        return id;
    }

    @Override
    public void init() {
        if (grAttribs != null && grAttribs.size() > 0) {
            gRestrict = new int[grAttribs.size()];
            for (int i = 0; i < grAttribs.size(); i++) {
                gRestrict[i] = inputSchema.indexOf(grAttribs.get(i));
            }
        }
        maxId = 0;
        keyMap = new HashMap<RelationalTuple<T>, Integer>();
        tupleMap = new HashMap<Integer, RelationalTuple<T>>();
    }

    private int getOutputPos(FESortedPair<SDFAttributeList, AggregateFunction> p) {
        Integer pos = aggrOutputPos.get(p);
        if (pos == null) {
            Map<AggregateFunction, SDFAttribute> funcs = aggregations.get(p
                    .getE1());
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
    public RelationalTuple<T> createOutputElement(
            Integer groupID,
            PairMap<SDFAttributeList, AggregateFunction, RelationalTuple<T>, ?> r) {
        // ANDRE: do not use schema in a relational tuple
        // RelationalTuple<T> returnTuple = new
        // RelationalTuple<T>(outputSchema);
        RelationalTuple<T> returnTuple = new RelationalTuple<T>(outputSchema
                .size());

        // in r stecken alle Aggregate drin
        // notwendig: Finde die Ziel-Position in dem returnTuple
        // ermittelt sich aus dem Attribute und der Aggregatfunktio
        for (Entry<FESortedPair<SDFAttributeList, AggregateFunction>, RelationalTuple<T>> e : r
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

    private IEvaluator<RelationalTuple<?>, RelationalTuple<?>> createAggFunction(
            AggregateFunction key, int[] pos) {
        IEvaluator<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
        if ((key.getName().equalsIgnoreCase("AVG"))
                || (key.getName().equalsIgnoreCase("SUM"))) {
            aggFunc = RelationalAvgSum.getInstance(pos[0], (key.getName()
                    .equalsIgnoreCase("AVG")) ? true : false);
        }
        else if (key.getName().equalsIgnoreCase("COUNT")) {
            aggFunc = RelationalCount.getInstance();
        }
        else if ((key.getName().equalsIgnoreCase("MIN"))
                || (key.getName().equalsIgnoreCase("MAX"))) {
            aggFunc = RelationalMinMax.getInstance(pos[0], (key.getName()
                    .equalsIgnoreCase("MAX")) ? true : false);
        }else if((key.getName().equalsIgnoreCase("NEST"))){
        	aggFunc = new RelationalNest(pos);
        }
        else if (key.getName().equalsIgnoreCase("BEAN")) {
            aggFunc = new AggregationBean(pos, key.getProperty("resource"));
        }
        else if (key.getName().equalsIgnoreCase("SCRIPT")) {
            aggFunc = new AggregationJSR223(pos, key.getProperty("resource"));
        }
        else {
            throw new IllegalArgumentException("No such Aggregationfunction");
        }
        return aggFunc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IEvaluator<RelationalTuple<T>, RelationalTuple<T>> getEvaluatorAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p) {
        IEvaluator<RelationalTuple<?>, RelationalTuple<?>> eval = fMap.get(p);
        if (eval == null) {
            int[] posArray = new int[p.getE1().size()];
            for (int i = 0; i < p.getE1().size(); ++i) {
                SDFAttribute attr = p.getE1().get(i);
                posArray[i] = inputSchema.indexOf(attr);
            }
            eval = createAggFunction(p.getE2(), posArray);
        }
        return (IEvaluator) eval;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IInitializer<RelationalTuple<T>> getInitAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p) {
        // Zur Zeit keine unterschiedlichen Aggregationsfunktionen
        return (IInitializer<RelationalTuple<T>>) getEvaluatorAggFunction(p);
    }

    @SuppressWarnings("unchecked")
    @Override
    public IMerger<RelationalTuple<T>> getMergerAggFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p) {
        return (IMerger) getEvaluatorAggFunction(p);
    }
}
