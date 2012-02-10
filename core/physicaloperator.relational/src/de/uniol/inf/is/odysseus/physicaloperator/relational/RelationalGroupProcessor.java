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

import de.uniol.inf.is.odysseus.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

public class RelationalGroupProcessor<T extends IMetaAttribute> implements
        IGroupProcessor<RelationalTuple<T>, RelationalTuple<T>> {

    Map<RelationalTuple<T>, Integer> keyMap = null;
    Map<Integer, RelationalTuple<T>> tupleMap = null;
    int maxId = 0;
    int[] gRestrict = null;
    private final List<SDFAttribute> grAttribs;
    private final Map<FESortedClonablePair<SDFSchema, AggregateFunction>, Integer> aggrOutputPos = new HashMap<FESortedClonablePair<SDFSchema, AggregateFunction>, Integer>();
    private final Map<SDFAttribute, Integer> groupOutputPos = new HashMap<SDFAttribute, Integer>();
    final private SDFSchema inputSchema;
    final private SDFSchema outputSchema;
    final private Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations;
 
    public RelationalGroupProcessor(SDFSchema inputSchema,
            SDFSchema outputSchema,
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

    private int getOutputPos(FESortedClonablePair<SDFSchema, AggregateFunction> p) {
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
            PairMap<SDFSchema, AggregateFunction, RelationalTuple<T>, ?> r) {
        RelationalTuple<T> returnTuple = new RelationalTuple<T>(outputSchema
                .size());

        // in r stecken alle Aggregate drin
        // notwendig: Finde die Ziel-Position in dem returnTuple
        // ermittelt sich aus dem Attribute und der Aggregatfunktio
        for (Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, RelationalTuple<T>> e : r
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

}
